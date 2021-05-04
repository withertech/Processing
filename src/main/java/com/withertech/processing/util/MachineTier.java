package com.withertech.processing.util;

import com.withertech.processing.config.Config;
import com.withertech.processing.config.TierConfig;

import java.util.Locale;

public enum MachineTier
{
	BASIC(new DefaultMachineTier(2, 1_000, 1, 1, 4.0f)),
	ADVANCED(new DefaultMachineTier(4, 10_000, 2, 2, 8.0f)),
	ELITE(new DefaultMachineTier(6, 100_000, 4, 4, 16.0f)),
	ULTIMATE(new DefaultMachineTier(8, 1_000_000, 8, 8, 32.0f)),
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

	public DefaultMachineTier getDefaultConfig()
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

	public int getStorageMultiplier()
	{
		return getConfig().getStorageMultiplier();
	}

	public int getOperationsPerTick()
	{
		return getConfig().getOperationsPerTick();
	}

	public float getProcessingSpeed()
	{
		return getConfig().getProcessingSpeed();
	}

	public static class DefaultMachineTier
	{
		private final int upgradeSlots;
		private final int energyCapacity;
		private final int storageMultiplier;
		private final int operationsPerTick;
		private final float processingSpeed;

		public DefaultMachineTier(int upgradeSlots, int energyCapacity, int storageMultiplier, int operationsPerTick, float processingSpeed)
		{
			this.upgradeSlots = upgradeSlots;
			this.energyCapacity = energyCapacity;
			this.storageMultiplier = storageMultiplier;
			this.operationsPerTick = operationsPerTick;
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

		public int getStorageMultiplier()
		{
			return storageMultiplier;
		}

		public int getOperationsPerTick()
		{
			return operationsPerTick;
		}

		public float getProcessingSpeed()
		{
			return processingSpeed;
		}
	}
}
