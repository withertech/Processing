package com.withertech.processing.init;

import com.withertech.processing.blocks.IBlockProvider;
import com.withertech.processing.config.Config;
import com.withertech.processing.config.OreConfig;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.util.Lazy;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public enum ModOres implements IBlockProvider
{
	COPPER(() -> ModMetals.COPPER, 3, 1, new DefaultOreConfigs(8, 8, 40, 90)),
	TIN(() -> ModMetals.TIN, 3, 1, new DefaultOreConfigs(8, 8, 20, 80)),
	SILVER(() -> ModMetals.SILVER, 4, 2, new DefaultOreConfigs(4, 8, 0, 40)),
	LEAD(() -> ModMetals.LEAD, 4, 2, new DefaultOreConfigs(4, 8, 0, 30)),
	NICKEL(() -> ModMetals.NICKEL, 5, 2, new DefaultOreConfigs(1, 6, 0, 24)),
	PLATINUM(() -> ModMetals.PLATINUM, 5, 2, new DefaultOreConfigs(1, 8, 5, 20)),
	ZINC(() -> ModMetals.ZINC, 3, 1, new DefaultOreConfigs(4, 8, 20, 60)),
	BISMUTH(() -> ModMetals.BISMUTH, 3, 1, new DefaultOreConfigs(4, 8, 16, 64)),
	BAUXITE(() -> ModMetals.ALUMINUM, 4, 1, new DefaultOreConfigs(6, 8, 15, 50)),
	URANIUM(() -> ModMetals.URANIUM, 6, 2, new DefaultOreConfigs(1, 4, 0, 18)),
	;

	private final Supplier<ModMetals> metal;
	private final DefaultOreConfigs defaultOreConfigs;
	private final int hardness;
	private final int harvestLevel;
	private final Lazy<ConfiguredFeature<?, ?>> configuredFeature = Lazy.of(() ->
	{
		OreConfig config = this.getConfig().orElse(null);
		assert config != null;
		int bottom = config.getMinHeight();
		return Feature.ORE
				.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, this.asBlockState(), config.getVeinSize()))
				.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(bottom, bottom, config.getMaxHeight())))
				.square()
				.count(config.getVeinCount());
	});

	ModOres(Supplier<ModMetals> metal, int hardness, int harvestLevel, DefaultOreConfigs defaultOreConfigs)
	{
		this.metal = metal;
		this.defaultOreConfigs = defaultOreConfigs;
		this.hardness = hardness;
		this.harvestLevel = harvestLevel;
	}

	public String getName()
	{
		return name().toLowerCase(Locale.ROOT);
	}

	public int getHardness()
	{
		return hardness;
	}

	public int getHarvestLevel()
	{
		return harvestLevel;
	}

	public DefaultOreConfigs getDefaultOreConfigs()
	{
		return defaultOreConfigs;
	}

	public Optional<OreConfig> getConfig()
	{
		return Config.getOreConfig(this);
	}

	public ConfiguredFeature<?, ?> getConfiguredFeature()
	{
		return configuredFeature.get();
	}

	@Override
	public Block asBlock()
	{
		return metal.get().getOre().orElse(null);
	}

	public static class DefaultOreConfigs
	{
		private final int veinCount;
		private final int veinSize;
		private final int minHeight;
		private final int maxHeight;

		public DefaultOreConfigs(int veinCount, int veinSize, int minHeight, int maxHeight)
		{
			this.veinCount = veinCount;
			this.veinSize = veinSize;
			this.minHeight = minHeight;
			this.maxHeight = maxHeight;
		}

		public int getVeinCount()
		{
			return veinCount;
		}

		public int getVeinSize()
		{
			return veinSize;
		}

		public int getMinHeight()
		{
			return minHeight;
		}

		public int getMaxHeight()
		{
			return maxHeight;
		}
	}
}
