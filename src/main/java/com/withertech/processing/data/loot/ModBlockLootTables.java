package com.withertech.processing.data.loot;

import com.withertech.processing.init.Registration;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class ModBlockLootTables extends BlockLootTables
{
	@Override
	protected void addTables()
	{
		Registration.BLOCKS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(block -> block.asItem() != Items.AIR)
				.forEach(this::registerDropSelfLootTable);
	}

	@Nonnull
	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.asItem() != Items.AIR).collect(Collectors.toList());
	}
}
