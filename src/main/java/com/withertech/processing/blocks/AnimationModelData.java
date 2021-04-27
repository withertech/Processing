package com.withertech.processing.blocks;

import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;

public class AnimationModelData extends ModelDataMap
{
	public final ModelProperty<IModelTransform> ANIMATION_PROPERTY = Properties.AnimationProperty;

	@Override
	public boolean hasProperty(ModelProperty<?> prop)
	{
		return prop == ANIMATION_PROPERTY || super.hasProperty(prop);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getData(ModelProperty<T> prop)
	{
		if (prop == ANIMATION_PROPERTY)
			return (T) this;
		return super.getData(prop);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T setData(ModelProperty<T> prop, T data)
	{
		if (prop == ANIMATION_PROPERTY)
			return (T) this;
		return super.setData(prop, data);
	}
}
