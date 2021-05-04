package com.withertech.processing.data.tag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.withertech.processing.Processing;
import com.withertech.processing.init.ModGems;
import com.withertech.processing.init.ModItems;
import com.withertech.processing.init.ModMetals;
import com.withertech.processing.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ModItemTagsProvider extends ItemTagsProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	public ModItemTagsProvider(DataGenerator generatorIn, ModBlockTagsProvider blocks, ExistingFileHelper existingFileHelper)
	{
		super(generatorIn, blocks, Processing.MODID, existingFileHelper);
	}

	private static ITag.INamedTag<Item> itemTag(ResourceLocation id)
	{
		return ItemTags.makeWrapperTag(id.toString());
	}

	private static ResourceLocation forgeId(String path)
	{
		return new ResourceLocation("forge", path);
	}

	@Override
	protected void registerTags()
	{
		// Empties
		builder(ModTags.Items.PLATE_PRESS.getName(), ModItems.PLATE_PRESS.get());
		builder(ModTags.Items.ROD_PRESS.getName(), ModItems.ROD_PRESS.get());
		builder(ModTags.Items.GEAR_PRESS.getName(), ModItems.GEAR_PRESS.get());
		builderTag(ModTags.Items.PRESSES.getName(), ModTags.Items.PLATE_PRESS, ModTags.Items.ROD_PRESS, ModTags.Items.GEAR_PRESS);

		for (ModMetals metal : ModMetals.values())
		{
			metal.getOreTag().ifPresent(tag ->
					metal.getOreItemTag().ifPresent(ore ->
							copy(tag, ore)));
			metal.getStorageBlockTag().ifPresent(tag ->
					metal.getStorageBlockItemTag().ifPresent(block ->
							copy(tag, block)));
			metal.getDustTag().ifPresent(tag ->
					metal.getDust().ifPresent(dust ->
							getOrCreateBuilder(tag).add(dust)));
			metal.getIngotTag().ifPresent(tag ->
					metal.getIngot().ifPresent(item ->
							getOrCreateBuilder(tag).add(item)));
			metal.getNuggetTag().ifPresent(tag ->
					metal.getNugget().ifPresent(item ->
							getOrCreateBuilder(tag).add(item)));
			metal.getPlateTag().ifPresent(tag ->
					metal.getPlate().ifPresent(item ->
							getOrCreateBuilder(tag).add(item)));
			metal.getRodTag().ifPresent(tag ->
					metal.getRod().ifPresent(item ->
							getOrCreateBuilder(tag).add(item)));
			metal.getGearTag().ifPresent(tag ->
					metal.getGear().ifPresent(item ->
							getOrCreateBuilder(tag).add(item)));
		}

		for (ModGems gem : ModGems.values())
		{
			gem.getOreTag().ifPresent(tag ->
					gem.getOreItemTag().ifPresent(ore ->
							copy(tag, ore)));
			gem.getStorageBlockTag().ifPresent(tag ->
					gem.getStorageBlockItemTag().ifPresent(block ->
							copy(tag, block)));
			gem.getDustTag().ifPresent(tag ->
					gem.getDust().ifPresent(dust ->
							getOrCreateBuilder(tag).add(dust)));
			gem.getGemTag().ifPresent(tag ->
					gem.getGem().ifPresent(item ->
							getOrCreateBuilder(tag).add(item)));
		}
		copy(Tags.Blocks.ORES, Tags.Items.ORES);
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		groupMetalBuilder(Tags.Items.INGOTS, ModMetals::getIngotTag);
		groupMetalBuilder(Tags.Items.DUSTS, ModMetals::getDustTag);
		groupMetalBuilder(Tags.Items.NUGGETS, ModMetals::getNuggetTag);
		groupMetalBuilder(ModTags.Items.PLATES, ModMetals::getPlateTag);
		groupMetalBuilder(ModTags.Items.RODS, ModMetals::getRodTag);
		groupMetalBuilder(ModTags.Items.GEAR, ModMetals::getGearTag);

		groupGemBuilder(Tags.Items.GEMS, ModGems::getGemTag);
		groupGemBuilder(Tags.Items.DUSTS, ModGems::getDustTag);
	}

	@SafeVarargs
	private final void groupMetalBuilder(ITag.INamedTag<Item> tag, Function<ModMetals, Optional<ITag.INamedTag<Item>>> tagGetter, ITag.INamedTag<Item>... extras)
	{
		Builder<Item> builder = getOrCreateBuilder(tag);
		for (ModMetals metal : ModMetals.values())
		{
			tagGetter.apply(metal).ifPresent(builder::addTag);
		}
		for (ITag.INamedTag<Item> extraTag : extras)
		{
			builder.addTag(extraTag);
		}
	}

	@SafeVarargs
	private final void groupGemBuilder(ITag.INamedTag<Item> tag, Function<ModGems, Optional<ITag.INamedTag<Item>>> tagGetter, ITag.INamedTag<Item>... extras)
	{
		Builder<Item> builder = getOrCreateBuilder(tag);
		for (ModGems metal : ModGems.values())
		{
			tagGetter.apply(metal).ifPresent(builder::addTag);
		}
		for (ITag.INamedTag<Item> extraTag : extras)
		{
			builder.addTag(extraTag);
		}
	}

	private void builder(ResourceLocation id, IItemProvider... items)
	{
		getOrCreateBuilder(itemTag(id)).add(Arrays.stream(items).map(IItemProvider::asItem).toArray(Item[]::new));
	}
	private void builderTag(ResourceLocation id, ITag.INamedTag<Item>... items)
	{
		getOrCreateBuilder(itemTag(id)).addTags(Arrays.stream(items).toArray(ITag.INamedTag[]::new));
	}
	@Nonnull
	@Override
	public String getName()
	{
		return "Processing - Item Tags";
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
				return; //Forge: Allow running this data provider without writing it. Recipe provider needs valid tags.

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
