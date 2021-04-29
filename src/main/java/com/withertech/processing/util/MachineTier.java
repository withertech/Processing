package com.withertech.processing.util;

import com.withertech.processing.config.Config;
import com.withertech.processing.config.TierConfig;

import java.util.Locale;

public enum MachineTier
{
	BASIC(new DefaultMachineTier(0, 10_000, 1.0f)),
	ADVANCED(new DefaultMachineTier(4, 50_000, 2.0f)),
	ULTIMATE(new DefaultMachineTier(8, 100_000, 8.0f)),
	;
	private final DefaultMachineTier tier;

	MachineTier(DefaultMachineTier tier)
	{
		this.tier = tier;

	}
	public TierConfig getConfig()
	{
		return Config.getTierConfig(this);
	}


	public String getName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}

	public DefaultMachineTier getDefaultTier()
	{
		return this.tier;
	}

	public int getUpgradeSlots()
	{
		return getConfig().getUpgradeSlots();
	}

	public int getEnergyCapacity()
	{
		return getConfig().getEnergyCapacity();
	}

	public float getProcessingSpeed()
	{
		return getConfig().getProcessingSpeed();
	}

	public static class DefaultMachineTier
	{
		private final int upgradeSlots;
		private final int energyCapacity;
		private final float processingSpeed;

		public DefaultMachineTier(int upgradeSlots, int energyCapacity, float processingSpeed)
		{
			this.upgradeSlots = upgradeSlots;
			this.energyCapacity = energyCapacity;
			this.processingSpeed = processingSpeed;
		}

		public int getUpgradeSlots()
		{
			return upgradeSlots;
		}

		public int getEnergyCapacity()
		{
			return energyCapacity;
		}

		public float getProcessingSpeed()
		{
			return processingSpeed;
		}
	}
}
