package com.withertech.processing.items;

import com.withertech.processing.api.IMachineUpgrade;
import com.withertech.processing.init.Registration;
import com.withertech.processing.registry.ItemRegistryObject;
import com.withertech.processing.util.Constants;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum MachineUpgrades implements IItemProvider, IMachineUpgrade
{
	PROCESSING_SPEED(Constants.UPGRADE_PROCESSING_SPEED_AMOUNT, 0.5f, "+%d%% machine processing speed"),
	OUTPUT_CHANCE(Constants.UPGRADE_SECONDARY_OUTPUT_AMOUNT, 0.25f, "+%d%% chance of secondary outputs"),
	ENERGY_CAPACITY(0, 0.0f, "+%d energy storage", false),
	ENERGY_EFFICIENCY(Constants.UPGRADE_ENERGY_EFFICIENCY_AMOUNT, Constants.UPGRADE_ENERGY_EFFICIENCY_AMOUNT, "Reduces energy used per tick"),
	RANGE(Constants.UPGRADE_RANGE_AMOUNT, 0.15f, "+%d machine work range", false);

	private final float upgradeValue;
	private final float energyUsage;
	private final boolean displayValueAsPercentage;
	private final String desc;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<ItemMachineUpgrade> item;

	MachineUpgrades(float upgradeValue, float energyUsage, String desc)
	{
		this(upgradeValue, energyUsage, desc, true);
	}

	MachineUpgrades(float upgradeValue, float energyUsage, String desc, boolean displayValueAsPercentage)
	{
		this.upgradeValue = upgradeValue;
		this.energyUsage = energyUsage;
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

	@Override
	public float getEnergyUsageMultiplier()
	{
		return energyUsage;
	}

	@Override
	public float getUpgradeValue()
	{
		return upgradeValue;
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
}