package com.withertech.processing.data.loot;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTableProvider extends LootTableProvider
{
	public ModLootTableProvider(DataGenerator dataGeneratorIn)
	{
		super(dataGeneratorIn);
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "Processing - Loot Tables";
	}

	@Nonnull
	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
	{
		return ImmutableList.of(
				Pair.of(ModBlockLootTables::new, LootParameterSets.BLOCK)
		);
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationTracker validationtracker)
	{
		map.forEach((id, table) -> LootTableManager.validateLootTable(validationtracker, id, table));
	}
}