package com.withertech.processing.util;


import com.withertech.processing.Processing;
import net.minecraft.util.ResourceLocation;

public final class Constants
{
	public static final ResourceLocation ALLOY_SMELTING = Processing.getId("alloy_smelting");
	public static final ResourceLocation COMPRESSING = Processing.getId("compressing");
	public static final ResourceLocation CRUSHING = Processing.getId("crushing");
	public static final ResourceLocation PRESSING = Processing.getId("pressing");
	public static final ResourceLocation DRYING = Processing.getId("drying");
	public static final ResourceLocation INFUSING = Processing.getId("infusing");
	public static final ResourceLocation MIXING = Processing.getId("mixing");
	public static final ResourceLocation REFINING = Processing.getId("refining");
	public static final ResourceLocation SOLIDIFYING = Processing.getId("solidifying");

	// Machine upgrades
	public static final int UPGRADES_PER_SLOT = 1;
	public static final float UPGRADE_PROCESSING_SPEED_AMOUNT = 0.5f;
	public static final float UPGRADE_SECONDARY_OUTPUT_AMOUNT = 0.1f;
	public static final float UPGRADE_ENERGY_EFFICIENCY_AMOUNT = -0.15f;
	public static final int UPGRADE_RANGE_AMOUNT = 2;

	private Constants()
	{
		throw new IllegalAccessError("Utility class");
	}
}
