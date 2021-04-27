package com.withertech.processing.data.client;

import com.withertech.processing.Processing;
import com.withertech.processing.init.ModBlocks;
import com.withertech.processing.init.ModMetals;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ModBlockStateProvider extends BlockStateProvider
{
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
	{
		super(gen, Processing.MODID, exFileHelper);
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "Processing - Block Models, BlockItem Models, And BlockStates";
	}

	@Override
	protected void registerStatesAndModels()
	{
		horizontalBlock(ModBlocks.BASIC_FURNACE.get(), models().orientableWithBottom("basic_furnace",
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package_top")
		));
		horizontalBlock(ModBlocks.FURNACE.get(), models().orientableWithBottom("furnace",
				new ResourceLocation("processing", "block/package"),
				new ResourceLocation("processing", "block/package"),
				new ResourceLocation("processing", "block/package"),
				new ResourceLocation("processing", "block/package_top")
		));
		Arrays.stream(ModMetals.values()).forEach(metal ->
		{
			metal.getOre().ifPresent(this::simpleBlock);
			metal.getStorageBlock().ifPresent(this::simpleBlock);
		});
		simpleBlock(ModBlocks.FLUID_TANK.get(), models().getExistingFile(modLoc("fluid_tank")));
	}


}
