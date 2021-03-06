package com.withertech.processing.config;

import com.withertech.processing.init.ModOres;
import net.minecraftforge.common.ForgeConfigSpec;

public class OreConfig
{
	private final ForgeConfigSpec.BooleanValue masterSwitch;
	private final ForgeConfigSpec.BooleanValue enabled;
	private final ForgeConfigSpec.IntValue veinCount;
	private final ForgeConfigSpec.IntValue veinSize;
	private final ForgeConfigSpec.IntValue minHeight;
	private final ForgeConfigSpec.IntValue maxHeight;

	public OreConfig(ModOres ore, ForgeConfigSpec.Builder builder, ForgeConfigSpec.BooleanValue masterSwitch)
	{
		this.masterSwitch = masterSwitch;
		this.enabled = builder
				.comment("Enable ore generation")
				.define(ore.getName() + ".enable", ore.getDefaultOreConfigs().getEnabled());
		this.veinCount = builder
				.comment("Number of veins per chunk")
				.defineInRange(ore.getName() + ".veinCount", ore.getDefaultOreConfigs().getVeinCount(), 0, Integer.MAX_VALUE);
		this.veinSize = builder
				.comment("Size of veins")
				.defineInRange(ore.getName() + ".veinSize", ore.getDefaultOreConfigs().getVeinSize(), 0, 100);
		this.minHeight = builder
				.comment("Minimum Y-coordinate (base height) of veins")
				.defineInRange(ore.getName() + ".minHeight", ore.getDefaultOreConfigs().getMinHeight(), 0, 255);
		this.maxHeight = builder
				.comment("Maximum Y-coordinate (highest level) of veins")
				.defineInRange(ore.getName() + ".maxHeight", ore.getDefaultOreConfigs().getMaxHeight(), 0, 255);
	}


	public boolean isEnabled()
	{
		return masterSwitch.get() && getEnabled() && getVeinCount() > 0 && getVeinSize() > 0;
	}


	public boolean getEnabled()
	{
		return enabled.get();
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled.set(enabled);
	}

	public int getVeinCount()
	{
		return veinCount.get();
	}

	public void setVeinCount(int veinCount)
	{
		this.veinCount.set(veinCount);
	}

	public int getVeinSize()
	{
		return veinSize.get();
	}

	public void setVeinSize(int veinSize)
	{
		this.veinSize.set(veinSize);
	}

	public int getMinHeight()
	{
		return minHeight.get();
	}

	public void setMinHeight(int minHeight)
	{
		this.minHeight.set(minHeight);
	}

	public int getMaxHeight()
	{
		return maxHeight.get();
	}

	public void setMaxHeight(int maxHeight)
	{
		this.maxHeight.set(maxHeight);
	}
}
