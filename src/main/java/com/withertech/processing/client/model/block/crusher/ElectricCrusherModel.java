package com.withertech.processing.client.model.block.crusher;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.crusher.ElectricCrusherTile;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ElectricCrusherModel extends AnimatedGeoModel<ElectricCrusherTile>
{
	@Override
	public ResourceLocation getModelLocation(ElectricCrusherTile electricCrusherTile)
	{
		return Processing.getId("geo/block/crusher.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(ElectricCrusherTile electricCrusherTile)
	{
		switch (electricCrusherTile.getMachineTier())
		{
			case BASIC:
				return Processing.getId("textures/block/basic_crusher.png");
			case ADVANCED:
				return Processing.getId("textures/block/advanced_crusher.png");
			case ULTIMATE:
				return Processing.getId("textures/block/ultimate_crusher.png");
			default:
				return null;

		}
	}

	@Override
	public ResourceLocation getAnimationFileLocation(ElectricCrusherTile electricCrusherTile)
	{
		return Processing.getId("animations/block/crusher.animation.json");
	}
}
