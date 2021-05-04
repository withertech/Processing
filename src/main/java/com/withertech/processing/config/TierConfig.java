package com.withertech.processing.config;

import com.withertech.processing.util.MachineTier;
import net.minecraftforge.common.ForgeConfigSpec;

public class TierConfig
{
	private final ForgeConfigSpec.IntValue upgradeSlots;
	private final ForgeConfigSpec.IntValue energyCapacity;
	private final ForgeConfigSpec.IntValue storageMultiplier;
	private final ForgeConfigSpec.IntValue operationsPerTick;
	private final ForgeConfigSpec.DoubleValue processingSpeed;

	public TierConfig(MachineTier tier, ForgeConfigSpec.Builder builder)
	{
		this.upgradeSlots = builder.comment("Number of upgrade slots for tier").defineInRange(tier.getName() + ".upgradeSlots", tier.getDefaultConfig().getUpgradeSlots(), 0, 9);
		this.energyCapacity = builder.comment("Max energy storage for tier").defineInRange(tier.getName() + ".energyCapacity", tier.getDefaultConfig().getEnergyCapacity(), 0, 1_000_000_000);
		this.storageMultiplier = builder.comment("Input slot max stack size multiplier for tier").defineInRange(tier.getName() + ".storageMultiplier", tier.getDefaultConfig().getStorageMultiplier(), 1, 64);
		this.operationsPerTick = builder.comment("Operations per cycle for tier").defineInRange(tier.getName() + ".operationsPerTick", tier.getDefaultConfig().getOperationsPerTick(), 1, 64);
		this.processingSpeed = builder.comment("Base processing speed for tier").defineInRange(tier.getName() + ".processingSpeed", tier.getDefaultConfig().getProcessingSpeed(), 0.0f, 1000.0f);
	}

	public int getUpgradeSlots()
	{
		return upgradeSlots.get();
	}

	public void setUpgradeSlots(int upgradeSlots)
	{
		this.upgradeSlots.set(upgradeSlots);
	}

	public int getEnergyCapacity()
	{
		return energyCapacity.get();
	}

	public void setEnergyCapacity(int energyCapacity)
	{
		this.energyCapacity.set(energyCapacity);
	}

	public int getStorageMultiplier()
	{
		return storageMultiplier.get();
	}

	public void setStorageMultiplier(int storageMultiplier)
	{
		this.storageMultiplier.set(storageMultiplier);
	}

	public int getOperationsPerTick()
	{
		return operationsPerTick.get();
	}

	public void setOperationsPerTick(int operationsPerTick)
	{
		this.operationsPerTick.set(operationsPerTick);
	}

	public float getProcessingSpeed()
	{
		return (float) (double) processingSpeed.get();
	}

	public void setProcessingSpeed(float processingSpeed)
	{
		this.processingSpeed.set((double) processingSpeed);
	}
}
