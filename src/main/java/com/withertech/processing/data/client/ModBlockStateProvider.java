package com.withertech.processing.data.client;

import com.withertech.processing.Processing;
import com.withertech.processing.api.ConnectionType;
import com.withertech.processing.blocks.AbstractMachineBlock;
import com.withertech.processing.blocks.ChassisBlock;
import com.withertech.processing.blocks.pipe.FluidPipeBlock;
import com.withertech.processing.init.*;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

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
		Arrays.stream(ModFactoryBlocks.values()).forEach(factory ->
		{
			Arrays.stream(MachineTier.values()).map(factory::getBlock).forEach( block -> tieredHorizontalBlock(block, block.getTier()));
		});

		Registration.BLOCKS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(block -> block instanceof ChassisBlock)
				.forEach(block -> tieredBlock(block, ((ChassisBlock) block).getTier()));

		Arrays.stream(ModMetals.values()).forEach(metal ->
		{
			metal.getOre().ifPresent(this::simpleBlock);
			metal.getStorageBlock().ifPresent(this::simpleBlock);
		});

		Arrays.stream(ModGems.values()).forEach(gem ->
		{
			gem.getOre().ifPresent(this::simpleBlock);
			gem.getStorageBlock().ifPresent(this::simpleBlock);
		});

		simpleBlock(ModBlocks.FLUID_TANK.get(), models().getExistingFile(modLoc("fluid_tank")));
		simpleBlock(ModBlocks.DUMMY.get());

		fluidPipe();

	}

	private void tieredHorizontalBlock(Block block, MachineTier tier)
	{
		horizontalBlock(block, models().orientableWithBottom(block.getRegistryName().getPath(),
				new ResourceLocation("processing", "block/" + tier.getName() + "_package"),
				new ResourceLocation("processing", "block/" + tier.getName() + "_package"),
				new ResourceLocation("processing", "block/" + tier.getName() + "_package"),
				new ResourceLocation("processing", "block/" + tier.getName() + "_package_top")
		));
	}

	private void tieredBlock(Block block, MachineTier tier)
	{
		simpleBlock(block, models().cubeTop(block.getRegistryName().getPath(),
				new ResourceLocation("processing", "block/" + tier.getName() + "_package"),
				new ResourceLocation("processing", "block/" + tier.getName() + "_package_top")
		));
	}

	private void fluidPipe()
	{
		getMultipartBuilder(ModBlocks.FLUID_PIPE.get())
				.part()
				.modelFile(models().getExistingFile(modLoc("fluid_pipe")))
				.addModel()
				.end()
				.part()
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_in")))
				.addModel()
				.condition(FluidPipeBlock.NORTH, ConnectionType.IN)
				.end()
				.part()
				.rotationY(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_in")))
				.addModel()
				.condition(FluidPipeBlock.EAST, ConnectionType.IN)
				.end()
				.part()
				.rotationY(180)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_in")))
				.addModel()
				.condition(FluidPipeBlock.SOUTH, ConnectionType.IN)
				.end()
				.part()
				.rotationY(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_in")))
				.addModel()
				.condition(FluidPipeBlock.WEST, ConnectionType.IN)
				.end()
				.part()
				.rotationX(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_in")))
				.addModel()
				.condition(FluidPipeBlock.UP, ConnectionType.IN)
				.end()
				.part()
				.rotationX(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_in")))
				.addModel()
				.condition(FluidPipeBlock.DOWN, ConnectionType.IN)
				.end()

				.part()
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_out")))
				.addModel()
				.condition(FluidPipeBlock.NORTH, ConnectionType.OUT)
				.end()
				.part()
				.rotationY(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_out")))
				.addModel()
				.condition(FluidPipeBlock.EAST, ConnectionType.OUT)
				.end()
				.part()
				.rotationY(180)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_out")))
				.addModel()
				.condition(FluidPipeBlock.SOUTH, ConnectionType.OUT)
				.end()
				.part()
				.rotationY(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_out")))
				.addModel()
				.condition(FluidPipeBlock.WEST, ConnectionType.OUT)
				.end()
				.part()
				.rotationX(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_out")))
				.addModel()
				.condition(FluidPipeBlock.UP, ConnectionType.OUT)
				.end()
				.part()
				.rotationX(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_out")))
				.addModel()
				.condition(FluidPipeBlock.DOWN, ConnectionType.OUT)
				.end()

				.part()
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_both")))
				.addModel()
				.condition(FluidPipeBlock.NORTH, ConnectionType.BOTH)
				.end()
				.part()
				.rotationY(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_both")))
				.addModel()
				.condition(FluidPipeBlock.EAST, ConnectionType.BOTH)
				.end()
				.part()
				.rotationY(180)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_both")))
				.addModel()
				.condition(FluidPipeBlock.SOUTH, ConnectionType.BOTH)
				.end()
				.part()
				.rotationY(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_both")))
				.addModel()
				.condition(FluidPipeBlock.WEST, ConnectionType.BOTH)
				.end()
				.part()
				.rotationX(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_both")))
				.addModel()
				.condition(FluidPipeBlock.UP, ConnectionType.BOTH)
				.end()
				.part()
				.rotationX(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_both")))
				.addModel()
				.condition(FluidPipeBlock.DOWN, ConnectionType.BOTH)
				.end()

				.part()
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_none")))
				.addModel()
				.condition(FluidPipeBlock.NORTH, ConnectionType.NONE)
				.end()
				.part()
				.rotationY(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_none")))
				.addModel()
				.condition(FluidPipeBlock.EAST, ConnectionType.NONE)
				.end()
				.part()
				.rotationY(180)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_none")))
				.addModel()
				.condition(FluidPipeBlock.SOUTH, ConnectionType.NONE)
				.end()
				.part()
				.rotationY(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_none")))
				.addModel()
				.condition(FluidPipeBlock.WEST, ConnectionType.NONE)
				.end()
				.part()
				.rotationX(270)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_none")))
				.addModel()
				.condition(FluidPipeBlock.UP, ConnectionType.NONE)
				.end()
				.part()
				.rotationX(90)
				.modelFile(models().getExistingFile(modLoc("fluid_pipe_side_none")))
				.addModel()
				.condition(FluidPipeBlock.DOWN, ConnectionType.NONE)
				.end();
	}


}
