package com.withertech.processing.config;

import com.withertech.processing.init.ModOres;
import com.withertech.processing.util.MachineTier;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Locale;

public class TierConfig
{
	private final ForgeConfigSpec.IntValue upgradeSlots;
	private final ForgeConfigSpec.IntValue energyCapacity;
	private final ForgeConfigSpec.DoubleValue processingSpeed;

	public TierConfig(MachineTier tier, ForgeConfigSpec.Builder builder)
	{
		this.upgradeSlots = builder.comment("Number of upgrade slots for tier").defineInRange(tier.getName() + ".upgradeSlots", tier.getDefaultTier().getUpgradeSlots(), 0, 9);
		this.energyCapacity = builder.comment("Max energy storage for tier").defineInRange(tier.getName() + ".energyCapacity", tier.getDefaultTier().getEnergyCapacity(), 0, 1_000_000_000);
		this.processingSpeed = builder.comment("Base processing speed for tier").defineInRange(tier.getName() + ".processingSpeed", tier.getDefaultTier().getProcessingSpeed(), 0.0f, 1000.0f);
	}

	public int getUpgradeSlots()
	{
		return upgradeSlots.get();
	}

	public int getEnergyCapacity()
	{
		return energyCapacity.get();
	}

	public float getProcessingSpeed()
	{
		return (float)(double) processingSpeed.get();
	}

	public void setUpgradeSlots(int upgradeSlots)
	{
		this.upgradeSlots.set(upgradeSlots);
	}

	public void setEnergyCapacity(int energyCapacity)
	{
		this.energyCapacity.set(energyCapacity);
	}

	public void setProcessingSpeed(float processingSpeed)
	{
		this.processingSpeed.set((double) processingSpeed);
	}
}
