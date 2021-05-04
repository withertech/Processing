package com.withertech.processing.init;

import com.withertech.processing.blocks.AbstractFactoryMachineBlock;
import com.withertech.processing.blocks.crusher.CrusherBlock;
import com.withertech.processing.blocks.furnace.FurnaceBlock;
import com.withertech.processing.blocks.press.PressBlock;
import com.withertech.processing.items.FactoryBlockItem;
import com.withertech.processing.util.MachineTier;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.function.Supplier;

public enum ModFactoryBlocks
{
	CRUSHER(
			() -> new CrusherBlock(MachineTier.BASIC),
			() -> new CrusherBlock(MachineTier.ADVANCED),
			() -> new CrusherBlock(MachineTier.ELITE),
			() -> new CrusherBlock(MachineTier.ULTIMATE)
	),
	PRESS(
			() -> new PressBlock(MachineTier.BASIC),
			() -> new PressBlock(MachineTier.ADVANCED),
			() -> new PressBlock(MachineTier.ELITE),
			() -> new PressBlock(MachineTier.ULTIMATE)
	),
	FURNACE(
			() -> new FurnaceBlock(MachineTier.BASIC),
			() -> new FurnaceBlock(MachineTier.ADVANCED),
			() -> new FurnaceBlock(MachineTier.ELITE),
			() -> new FurnaceBlock(MachineTier.ULTIMATE)
	)
	;

	private final Supplier<AbstractFactoryMachineBlock> basicSupplier;
	private final Supplier<AbstractFactoryMachineBlock> advancedSupplier;
	private final Supplier<AbstractFactoryMachineBlock> eliteSupplier;
	private final Supplier<AbstractFactoryMachineBlock> ultimateSupplier;

	private RegistryObject<AbstractFactoryMachineBlock> basicBlock;
	private RegistryObject<AbstractFactoryMachineBlock> advancedBlock;
	private RegistryObject<AbstractFactoryMachineBlock> eliteBlock;
	private RegistryObject<AbstractFactoryMachineBlock> ultimateBlock;

	private RegistryObject<FactoryBlockItem> basicItem;
	private RegistryObject<FactoryBlockItem> advancedItem;
	private RegistryObject<FactoryBlockItem> eliteItem;
	private RegistryObject<FactoryBlockItem> ultimateItem;


	ModFactoryBlocks(Supplier<AbstractFactoryMachineBlock> basicSupplier, Supplier<AbstractFactoryMachineBlock> advancedSupplier, Supplier<AbstractFactoryMachineBlock> eliteSupplier, Supplier<AbstractFactoryMachineBlock> ultimateSupplier)
	{
		this.basicSupplier = basicSupplier;
		this.advancedSupplier = advancedSupplier;
		this.eliteSupplier = eliteSupplier;
		this.ultimateSupplier = ultimateSupplier;
	}
	private static RegistryObject<AbstractFactoryMachineBlock> registerBlock(String name, Supplier<AbstractFactoryMachineBlock> block)
	{
		return Registration.BLOCKS.register(name, block);
	}
	private static RegistryObject<FactoryBlockItem> registerItem(String name, Supplier<AbstractFactoryMachineBlock> block)
	{
		return Registration.ITEMS.register(name, () -> new FactoryBlockItem(block.get(), new Item.Properties().group(ModGroups.MACHINES_ITEM_GROUP)));
	}
	public String getName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}

	public static void registerBlocks()
	{
		for (ModFactoryBlocks block : ModFactoryBlocks.values())
		{
			block.basicBlock = registerBlock(MachineTier.BASIC.getName() + "_" + block.getName(), block.basicSupplier);
			block.advancedBlock = registerBlock(MachineTier.ADVANCED.getName() + "_" + block.getName(), block.advancedSupplier);
			block.eliteBlock = registerBlock(MachineTier.ELITE.getName() + "_" + block.getName(), block.eliteSupplier);
			block.ultimateBlock = registerBlock(MachineTier.ULTIMATE.getName() + "_" + block.getName(), block.ultimateSupplier);
		}
	}

	public static void registerItems()
	{
		for (ModFactoryBlocks block : ModFactoryBlocks.values())
		{
			block.basicItem = registerItem(MachineTier.BASIC.getName() + "_" + block.getName(), block.basicBlock);
			block.advancedItem = registerItem(MachineTier.ADVANCED.getName() + "_" + block.getName(), block.advancedBlock);
			block.eliteItem = registerItem(MachineTier.ELITE.getName() + "_" + block.getName(), block.eliteBlock);
			block.ultimateItem = registerItem(MachineTier.ULTIMATE.getName() + "_" + block.getName(), block.ultimateBlock);
		}
	}

	@Nonnull
	public AbstractFactoryMachineBlock getBlock(MachineTier tier)
	{
		switch (tier)
		{
			case BASIC: return basicBlock.get();
			case ADVANCED: return advancedBlock.get();
			case ELITE: return eliteBlock.get();
			case ULTIMATE: return ultimateBlock.get();
		}
		return basicBlock.get();
	}
	@Nonnull
	public FactoryBlockItem getItem(MachineTier tier)
	{
		switch (tier)
		{
			case BASIC: return basicItem.get();
			case ADVANCED: return advancedItem.get();
			case ELITE: return eliteItem.get();
			case ULTIMATE: return ultimateItem.get();
		}
		return basicItem.get();
	}

}
