package com.withertech.processing.init;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.cell.BlockFluidTank;
import com.withertech.processing.blocks.crusher.ElectricCrusherBlock;
import com.withertech.processing.blocks.furnace.ElectricFurnaceBlock;
import com.withertech.processing.blocks.pipe.BlockFluidPipe;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks
{
	public static final RegistryObject<BlockFluidTank> FLUID_TANK = register("fluid_tank", BlockFluidTank::new);
	public static final RegistryObject<BlockFluidPipe> FLUID_PIPE = register("fluid_pipe", BlockFluidPipe::new);
	public static final RegistryObject<ElectricCrusherBlock> BASIC_CRUSHER = register("basic_crusher", () -> new ElectricCrusherBlock(MachineTier.BASIC));
	public static final RegistryObject<ElectricCrusherBlock> ADVANCED_CRUSHER = register("advanced_crusher", () -> new ElectricCrusherBlock(MachineTier.ADVANCED));
	public static final RegistryObject<ElectricCrusherBlock> ULTIMATE_CRUSHER = register("ultimate_crusher", () -> new ElectricCrusherBlock(MachineTier.ULTIMATE));
	public static final RegistryObject<ElectricFurnaceBlock> BASIC_FURNACE = register("basic_furnace", () -> new ElectricFurnaceBlock(MachineTier.BASIC));
	public static final RegistryObject<ElectricFurnaceBlock> ADVANCED_FURNACE = register("advanced_furnace", () -> new ElectricFurnaceBlock(MachineTier.ADVANCED));
	public static final RegistryObject<ElectricFurnaceBlock> ULTIMATE_FURNACE = register("ultimate_furnace", () -> new ElectricFurnaceBlock(MachineTier.ULTIMATE));

	static
	{
		ModMetals.registerBlocks();
	}

	public static void register()
	{
	}

	private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block)
	{
		return Registration.BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block)
	{
		RegistryObject<T> ret = registerNoItem(name, block);
		Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(Processing.ITEM_GROUP)));
		return ret;
	}
}
