package com.withertech.processing.config;

import com.withertech.processing.items.MachineUpgrades;
import net.minecraftforge.common.ForgeConfigSpec;

public class UpgradeConfig
{
	private final ForgeConfigSpec.DoubleValue upgradeValue;
	private final ForgeConfigSpec.DoubleValue energyUsage;
	public UpgradeConfig(MachineUpgrades upgrade, ForgeConfigSpec.Builder builder)
	{
		this.upgradeValue = builder.comment("Upgrade Value").defineInRange(upgrade.getName() + ".upgradeValue", upgrade.getDefaultConfig().getUpgradeValue(), -1000.0f, 1000.0f);
		this.energyUsage = builder.comment("Energy Usage Multiplier").defineInRange(upgrade.getName() + ".energyUsage", upgrade.getDefaultConfig().getUpgradeValue(), -1000.0f, 1000.0f);
	}

	public float getUpgradeValue()
	{
		return (float) (double) upgradeValue.get();
	}

	public float getEnergyUsage()
	{
		return (float) (double) energyUsage.get();
	}

	public void setUpgradeValue(float upgradeValue)
	{
		this.upgradeValue.set((double) upgradeValue);
	}

	public void setEnergyUsage(float energyUsage)
	{
		this.energyUsage.set((double) energyUsage);
	}
}
