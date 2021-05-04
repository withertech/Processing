package com.withertech.processing.items;

import com.withertech.processing.api.IMachineUpgrade;
import com.withertech.processing.config.Config;
import com.withertech.processing.config.UpgradeConfig;
import com.withertech.processing.init.Registration;
import com.withertech.processing.registry.ItemRegistryObject;
import com.withertech.processing.util.Constants;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum MachineUpgrades implements IItemProvider, IMachineUpgrade
{
	PROCESSING_SPEED(new DefaultMachineUpgrade(Constants.UPGRADE_PROCESSING_SPEED_AMOUNT, 0.5f), "+%d%% machine processing speed"),
	OUTPUT_CHANCE(new DefaultMachineUpgrade(Constants.UPGRADE_SECONDARY_OUTPUT_AMOUNT, 0.25f), "+%d%% chance of secondary outputs"),
	ENERGY_CAPACITY(new DefaultMachineUpgrade(Constants.UPGRADE_ENERGY_CAPACITY_AMOUNT, 0.0f), "x%d energy storage", false),
	ENERGY_EFFICIENCY(new DefaultMachineUpgrade(Constants.UPGRADE_ENERGY_EFFICIENCY_AMOUNT, Constants.UPGRADE_ENERGY_EFFICIENCY_AMOUNT), "Reduces energy used per tick"),
	RANGE(new DefaultMachineUpgrade(Constants.UPGRADE_RANGE_AMOUNT, 0.15f), "+%d machine work range", false);

	private final DefaultMachineUpgrade upgrade;
	private final boolean displayValueAsPercentage;
	private final String desc;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<ItemMachineUpgrade> item;

	MachineUpgrades(DefaultMachineUpgrade upgrade, String desc)
	{
		this(upgrade, desc, true);
	}

	MachineUpgrades(DefaultMachineUpgrade upgrade, String desc, boolean displayValueAsPercentage)
	{
		this.upgrade = upgrade;
		this.displayValueAsPercentage = displayValueAsPercentage;
		this.desc = desc;
	}

	public static void register()
	{
		for (MachineUpgrades value : values())
		{
			value.item = new ItemRegistryObject<>(Registration.ITEMS.register(value.getName(), () ->
					new ItemMachineUpgrade(value)));
		}
	}

	public DefaultMachineUpgrade getDefaultConfig()
	{
		return this.upgrade;
	}

	public UpgradeConfig getConfig()
	{
		return Config.getUpgradeConfig(this);
	}

	@Override
	public float getEnergyUsageMultiplier()
	{
		return getConfig().getEnergyUsage();
	}

	@Override
	public float getUpgradeValue()
	{
		return getConfig().getUpgradeValue();
	}

	@Override
	public boolean displayValueAsPercentage()
	{
		return displayValueAsPercentage;
	}

	public String getName()
	{
		return name().toLowerCase(Locale.ROOT) + "_upgrade";
	}

	public String getDesc()
	{
		return desc;
	}

	@Nonnull
	@Override
	public Item asItem()
	{
		return item.get();
	}

	public static class DefaultMachineUpgrade
	{
		private final float upgradeValue;
		private final float energyUsage;
		public DefaultMachineUpgrade(float upgradeValue, float energyUsage)
		{
			this.upgradeValue = upgradeValue;
			this.energyUsage = energyUsage;
		}

		public float getUpgradeValue()
		{
			return upgradeValue;
		}

		public float getEnergyUsage()
		{
			return energyUsage;
		}
	}
}