package com.withertech.processing.blocks.crusher;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.crusher.CrusherTile;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CrusherModel extends AnimatedGeoModel<CrusherTile>
{
	@Override
	public ResourceLocation getModelLocation(CrusherTile crusherTile)
	{
		return Processing.getId("geo/block/crusher.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CrusherTile crusherTile)
	{
		switch (crusherTile.getMachineTier())
		{
			case BASIC:
				return Processing.getId("textures/block/basic_crusher.png");
			case ADVANCED:
				return Processing.getId("textures/block/advanced_crusher.png");
			case ELITE:
				return Processing.getId("textures/block/elite_crusher.png");
			case ULTIMATE:
				return Processing.getId("textures/block/ultimate_crusher.png");
			default:
				return null;

		}
	}

	@Override
	public ResourceLocation getAnimationFileLocation(CrusherTile crusherTile)
	{
		return Processing.getId("animations/block/crusher.animation.json");
	}
}
