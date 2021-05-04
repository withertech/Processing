package com.withertech.processing.data.loot;

import com.withertech.processing.init.ModFactoryBlocks;
import com.withertech.processing.init.Registration;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModBlockLootTables extends BlockLootTables
{
	@Override
	protected void addTables()
	{
		Registration.BLOCKS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(block -> block.asItem() != Items.AIR)
				.forEach(this::registerDropSelfLootTable);

		Arrays.stream(ModFactoryBlocks.values()).forEach(blocks -> Arrays.stream(MachineTier.values()).forEach(tier -> registerDropSelfLootTable(blocks.getBlock(tier))));
	}

	@Nonnull
	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return Stream.concat(Registration.BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block.asItem() != Items.AIR), Arrays.stream(ModFactoryBlocks.values()).flatMap(modFactoryBlocks -> Arrays.stream(MachineTier.values()).map(modFactoryBlocks::getBlock))).collect(Collectors.toList());
	}
}
