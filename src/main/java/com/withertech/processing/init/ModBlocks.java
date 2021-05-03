package com.withertech.processing.init;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.AbstractFactoryMachineBlock;
import com.withertech.processing.blocks.ChassisBlock;
import com.withertech.processing.blocks.DummyBlock;
import com.withertech.processing.blocks.cell.FluidTankBlock;
import com.withertech.processing.blocks.crusher.ElectricCrusherBlock;
import com.withertech.processing.blocks.furnace.ElectricFurnaceBlock;
import com.withertech.processing.blocks.pipe.FluidPipeBlock;
import com.withertech.processing.blocks.press.ElectricPressBlock;
import com.withertech.processing.items.ChassisBlockItem;
import com.withertech.processing.items.FactoryBlockItem;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks
{
	public static final RegistryObject<FluidTankBlock> FLUID_TANK = register("fluid_tank", FluidTankBlock::new);
	public static final RegistryObject<FluidPipeBlock> FLUID_PIPE = register("fluid_pipe", FluidPipeBlock::new);

	public static final RegistryObject<DummyBlock> DUMMY = registerNoItem("dummy", DummyBlock::new);

	public static final RegistryObject<ChassisBlock> BASIC_CHASSIS = registerChassis("basic_chassis", () -> new ChassisBlock(MachineTier.BASIC));
	public static final RegistryObject<ChassisBlock> ADVANCED_CHASSIS = registerChassis("advanced_chassis", () -> new ChassisBlock(MachineTier.ADVANCED));
	public static final RegistryObject<ChassisBlock> ELITE_CHASSIS = registerChassis("elite_chassis", () -> new ChassisBlock(MachineTier.ELITE));
	public static final RegistryObject<ChassisBlock> ULTIMATE_CHASSIS = registerChassis("ultimate_chassis", () -> new ChassisBlock(MachineTier.ULTIMATE));

	public static final RegistryObject<ElectricCrusherBlock> BASIC_CRUSHER = registerFactory("basic_crusher", () -> new ElectricCrusherBlock(MachineTier.BASIC));
	public static final RegistryObject<ElectricCrusherBlock> ADVANCED_CRUSHER = registerFactory("advanced_crusher", () -> new ElectricCrusherBlock(MachineTier.ADVANCED));
	public static final RegistryObject<ElectricCrusherBlock> ELITE_CRUSHER = registerFactory("elite_crusher", () -> new ElectricCrusherBlock(MachineTier.ELITE));
	public static final RegistryObject<ElectricCrusherBlock> ULTIMATE_CRUSHER = registerFactory("ultimate_crusher", () -> new ElectricCrusherBlock(MachineTier.ULTIMATE));

	public static final RegistryObject<ElectricPressBlock> BASIC_PRESS = registerFactory("basic_press", () -> new ElectricPressBlock(MachineTier.BASIC));
	public static final RegistryObject<ElectricPressBlock> ADVANCED_PRESS = registerFactory("advanced_press", () -> new ElectricPressBlock(MachineTier.ADVANCED));
	public static final RegistryObject<ElectricPressBlock> ELITE_PRESS = registerFactory("elite_press", () -> new ElectricPressBlock(MachineTier.ELITE));
	public static final RegistryObject<ElectricPressBlock> ULTIMATE_PRESS = registerFactory("ultimate_press", () -> new ElectricPressBlock(MachineTier.ULTIMATE));

	public static final RegistryObject<ElectricFurnaceBlock> BASIC_FURNACE = registerFactory("basic_furnace", () -> new ElectricFurnaceBlock(MachineTier.BASIC));
	public static final RegistryObject<ElectricFurnaceBlock> ADVANCED_FURNACE = registerFactory("advanced_furnace", () -> new ElectricFurnaceBlock(MachineTier.ADVANCED));
	public static final RegistryObject<ElectricFurnaceBlock> ELITE_FURNACE = registerFactory("elite_furnace", () -> new ElectricFurnaceBlock(MachineTier.ELITE));
	public static final RegistryObject<ElectricFurnaceBlock> ULTIMATE_FURNACE = registerFactory("ultimate_furnace", () -> new ElectricFurnaceBlock(MachineTier.ULTIMATE));

	static
	{
		ModMetals.registerBlocks();
		ModGems.registerBlocks();
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
		Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(Processing.MACHINES_ITEM_GROUP)));
		return ret;
	}

	private static <T extends AbstractFactoryMachineBlock> RegistryObject<T> registerFactory(String name, Supplier<T> block)
	{
		RegistryObject<T> ret = registerNoItem(name, block);
		Registration.ITEMS.register(name, () -> new FactoryBlockItem(ret.get(), new Item.Properties().group(Processing.MACHINES_ITEM_GROUP)));
		return ret;
	}

	private static <T extends ChassisBlock> RegistryObject<T> registerChassis(String name, Supplier<T> block)
	{
		RegistryObject<T> ret = registerNoItem(name, block);
		Registration.ITEMS.register(name, () -> new ChassisBlockItem(ret.get(), new Item.Properties().group(Processing.MACHINES_ITEM_GROUP)));
		return ret;
	}
}
