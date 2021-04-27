package com.withertech.processing.data;

import com.withertech.processing.Processing;
import com.withertech.processing.data.client.ModBlockStateProvider;
import com.withertech.processing.data.client.ModItemModelProvider;
import com.withertech.processing.data.lang.ModLangProvider;
import com.withertech.processing.data.loot.ModLootTableProvider;
import com.withertech.processing.data.recipe.ModRecipeProvider;
import com.withertech.processing.data.tag.ModBlockTagsProvider;
import com.withertech.processing.data.tag.ModItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Processing.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
	private DataGenerators()
	{
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, existingFileHelper);
		gen.addProvider(blockTags);
		gen.addProvider(new ModItemTagsProvider(gen, blockTags, existingFileHelper));
		gen.addProvider(new ModRecipeProvider(gen));
		gen.addProvider(new ModLootTableProvider(gen));

		gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
		gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
		gen.addProvider(new ModLangProvider(gen, Processing.MODID, "en_us"));
	}
}
