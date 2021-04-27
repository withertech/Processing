package com.withertech.processing.client.model.block.furnace;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.furnace.ElectricFurnaceTile;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ElectricFurnaceModel extends AnimatedGeoModel<ElectricFurnaceTile>
{
	@Override
	public ResourceLocation getModelLocation(ElectricFurnaceTile electricFurnaceTile)
	{
		return Processing.getId("geo/block/furnace.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(ElectricFurnaceTile electricFurnaceTile)
	{
		switch (electricFurnaceTile.getMachineTier())
		{
			case BASIC:
				return Processing.getId("textures/block/basic_furnace.png");
			case STANDARD:
				return Processing.getId("textures/block/furnace.png");
			default:
				return null;

		}
	}

	@Override
	public ResourceLocation getAnimationFileLocation(ElectricFurnaceTile electricFurnaceTile)
	{
		return Processing.getId("animations/block/furnace.animation.json");
	}
}
