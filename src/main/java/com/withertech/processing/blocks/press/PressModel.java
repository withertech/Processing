package com.withertech.processing.blocks.press;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.press.PressTile;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PressModel extends AnimatedGeoModel<PressTile>
{
	@Override
	public ResourceLocation getModelLocation(PressTile pressTile)
	{
		return Processing.getId("geo/block/press.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(PressTile pressTile)
	{
		switch (pressTile.getMachineTier())
		{
			case BASIC:
				return Processing.getId("textures/block/basic_press.png");
			case ADVANCED:
				return Processing.getId("textures/block/advanced_press.png");
			case ELITE:
				return Processing.getId("textures/block/elite_press.png");
			case ULTIMATE:
				return Processing.getId("textures/block/ultimate_press.png");
			default:
				return null;

		}
	}

	@Override
	public ResourceLocation getAnimationFileLocation(PressTile pressTile)
	{
		return Processing.getId("animations/block/press.animation.json");
	}
}
