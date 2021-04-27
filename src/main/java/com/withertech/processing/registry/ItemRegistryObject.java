package com.withertech.processing.registry;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

public class ItemRegistryObject<T extends Item> extends RegistryObjectWrapper<T> implements IItemProvider
{
	public ItemRegistryObject(RegistryObject<T> item)
	{
		super(item);
	}

	@Nonnull
	@Override
	public Item asItem()
	{
		return registryObject.get();
	}
}
