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
		horizontalBlock(ModBlocks.BASIC_CRUSHER.get(), models().orientableWithBottom("basic_crusher",
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package_top")
		));
		horizontalBlock(ModBlocks.ADVANCED_CRUSHER.get(), models().orientableWithBottom("advanced_crusher",
				new ResourceLocation("processing", "block/advanced_package"),
				new ResourceLocation("processing", "block/advanced_package"),
				new ResourceLocation("processing", "block/advanced_package"),
				new ResourceLocation("processing", "block/advanced_package_top")
		));
		horizontalBlock(ModBlocks.ULTIMATE_CRUSHER.get(), models().orientableWithBottom("ultimate_crusher",
				new ResourceLocation("processing", "block/ultimate_package"),
				new ResourceLocation("processing", "block/ultimate_package"),
				new ResourceLocation("processing", "block/ultimate_package"),
				new ResourceLocation("processing", "block/ultimate_package_top")
		));
		horizontalBlock(ModBlocks.BASIC_FURNACE.get(), models().orientableWithBottom("basic_furnace",
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package"),
				new ResourceLocation("processing", "block/basic_package_top")
		));
		horizontalBlock(ModBlocks.ADVANCED_FURNACE.get(), models().orientableWithBottom("advanced_furnace",
				new ResourceLocation("processing", "block/advanced_package"),
				new ResourceLocation("processing", "block/advanced_package"),
				new ResourceLocation("processing", "block/advanced_package"),
				new ResourceLocation("processing", "block/advanced_package_top")
		));
		horizontalBlock(ModBlocks.ULTIMATE_FURNACE.get(), models().orientableWithBottom("ultimate_furnace",
				new ResourceLocation("processing", "block/ultimate_package"),
				new ResourceLocation("processing", "block/ultimate_package"),
				new ResourceLocation("processing", "block/ultimate_package"),
				new ResourceLocation("processing", "block/ultimate_package_top")
		));
		Arrays.stream(ModMetals.values()).forEach(metal ->
		{
			metal.getOre().ifPresent(this::simpleBlock);
			metal.getStorageBlock().ifPresent(this::simpleBlock);
		});
		simpleBlock(ModBlocks.FLUID_TANK.get(), models().getExistingFile(modLoc("fluid_tank")));
	}


}
