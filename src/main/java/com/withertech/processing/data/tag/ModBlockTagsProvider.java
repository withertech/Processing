package com.withertech.processing.data.tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.withertech.processing.Processing;
import com.withertech.processing.init.ModMetals;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;


public class ModBlockTagsProvider extends BlockTagsProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper)
	{
		super(generatorIn, Processing.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags()
	{

		for (ModMetals metal : ModMetals.values())
		{
			metal.getOreTag().ifPresent(tag ->
					metal.getOre().ifPresent(ore ->
							getOrCreateBuilder(tag).add(ore)));
			metal.getStorageBlockTag().ifPresent(tag ->
					metal.getStorageBlock().ifPresent(block ->
							getOrCreateBuilder(tag).add(block)));
		}

		groupBuilder(Tags.Blocks.ORES, ModMetals::getOreTag);
		groupBuilder(Tags.Blocks.STORAGE_BLOCKS, ModMetals::getStorageBlockTag);
	}

	private void groupBuilder(ITag.INamedTag<Block> tag, Function<ModMetals, Optional<ITag.INamedTag<Block>>> tagGetter)
	{
		Builder<Block> builder = getOrCreateBuilder(tag);
		for (ModMetals metal : ModMetals.values())
		{
			tagGetter.apply(metal).ifPresent(builder::addTag);
		}
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "Processing - Block Tags";
	}

	@Override
	public void act(@Nonnull DirectoryCache cache)
	{
		// Temp fix that removes the broken safety check
		this.tagToBuilder.clear();
		this.registerTags();
		this.tagToBuilder.forEach((p_240524_4_, p_240524_5_) ->
		{
			JsonObject jsonobject = p_240524_5_.serialize();
			Path path = this.makePath(p_240524_4_);
			if (path == null)
				return;

			try
			{
				String s = GSON.toJson(jsonobject);
				String s1 = HASH_FUNCTION.hashUnencodedChars(s).toString();
				if (!Objects.equals(cache.getPreviousHash(path), s1) || !Files.exists(path))
				{
					Files.createDirectories(path.getParent());

					try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path))
					{
						bufferedwriter.write(s);
					}
				}

				cache.recordHash(path, s1);
			} catch (IOException ioexception)
			{
				LOGGER.error("Couldn't save tags to {}", path, ioexception);
			}

		});
	}
}
