package com.withertech.processing.init;

import com.withertech.processing.Processing;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class OreGeneration
{

	private static final Map<ModOres, ConfiguredFeature<?, ?>> overworldOres = new EnumMap<>(ModOres.class);
	private static final Map<ModOres, ConfiguredFeature<?, ?>> netherOres = new EnumMap<>(ModOres.class);
	private static final Map<ModOres, ConfiguredFeature<?, ?>> endOres = new EnumMap<>(ModOres.class);

	public static void registerOres()
	{
		//BASE_STONE_OVERWORLD is for generating in stone, granite, diorite, and andesite
		//NETHERRACK is for generating in netherrack
		//BASE_STONE_NETHER is for generating in netherrack, basalt and blackstone

		//Overworld Ore Register
		for (ModOres ore : ModOres.values())
		{
			overworldOres.put(ore, register(ore.getName(), ore.getConfiguredFeature()));
		}

//		//Nether Ore Register
//		netherOres.add(register("flame_crystal_ore", Feature.ORE.withConfiguration(new OreFeatureConfig(
//				OreFeatureConfig.FillerBlockType.NETHERRACK, RegistryHandlerBlocks.FLAME_CRYSTAL_ORE.get().getDefaultState(), 4))
//				.range(48).square()
//				.func_242731_b(64)));
//
//		//The End Ore Register
//		endOres.add(register("air_block", Feature.ORE.withConfiguration(new OreFeatureConfig(
//				new BlockMatchRuleTest(Blocks.END_STONE), RegistryHandlerBlocks.AIR_CRYSTAL_BLOCK.get().getDefaultState(), 4)) //Vein Size
//				.range(128).square() //Spawn height start
//				.func_242731_b(64))); //Chunk spawn frequency
	}

	@SubscribeEvent
	public static void gen(BiomeLoadingEvent event)
	{
		BiomeGenerationSettingsBuilder generation = event.getGeneration();
//		if(event.getCategory().equals(Biome.Category.NETHER)){
//			for(ConfiguredFeature<?, ?> ore : netherOres){
//				if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
//			}
//		}
//		if(event.getCategory().equals(Biome.Category.THEEND)){
//			for(ConfiguredFeature<?, ?> ore : endOres){
//				if (ore != null) generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore);
//			}
//		}
		overworldOres.entrySet().stream().filter(ore -> ore != null && ore.getKey().getConfig().isEnabled()).forEach(ore -> generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ore.getValue()));
	}

	private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature)
	{
		return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Processing.MODID + ":" + name, configuredFeature);
	}

}
