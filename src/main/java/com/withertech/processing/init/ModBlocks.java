package com.withertech.processing.init;

import com.withertech.processing.blocks.ChassisBlock;
import com.withertech.processing.blocks.DummyBlock;
import com.withertech.processing.blocks.cell.FluidTankBlock;
import com.withertech.processing.blocks.pipe.FluidPipeBlock;
import com.withertech.processing.items.ChassisBlockItem;
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

	static
	{
		ModMetals.registerBlocks();
		ModGems.registerBlocks();
		ModFactoryBlocks.registerBlocks();
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
		Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(ModGroups.MACHINES_ITEM_GROUP)));
		return ret;
	}

	private static <T extends ChassisBlock> RegistryObject<T> registerChassis(String name, Supplier<T> block)
	{
		RegistryObject<T> ret = registerNoItem(name, block);
		Registration.ITEMS.register(name, () -> new ChassisBlockItem(ret.get(), new Item.Properties().group(ModGroups.MACHINES_ITEM_GROUP)));
		return ret;
	}
}
