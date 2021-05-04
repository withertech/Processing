package com.withertech.processing.blocks.furnace;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.furnace.FurnaceTile;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FurnaceModel extends AnimatedGeoModel<FurnaceTile>
{
	@Override
	public ResourceLocation getModelLocation(FurnaceTile furnaceTile)
	{
		return Processing.getId("geo/block/furnace.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(FurnaceTile furnaceTile)
	{
		switch (furnaceTile.getMachineTier())
		{
			case BASIC:
				return Processing.getId("textures/block/basic_furnace.png");
			case ADVANCED:
				return Processing.getId("textures/block/advanced_furnace.png");
			case ELITE:
				return Processing.getId("textures/block/elite_furnace.png");
			case ULTIMATE:
				return Processing.getId("textures/block/ultimate_furnace.png");
			default:
				return null;

		}
	}

	@Override
	public ResourceLocation getAnimationFileLocation(FurnaceTile furnaceTile)
	{
		return Processing.getId("animations/block/furnace.animation.json");
	}
}
