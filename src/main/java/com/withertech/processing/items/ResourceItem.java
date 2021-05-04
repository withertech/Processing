package com.withertech.processing.items;

import net.minecraft.item.Item;
public class ResourceItem extends Item
{
	private final ResourceItemType type;
	public ResourceItem(Properties properties, ResourceItemType type)
	{
		super(properties);
		this.type = type;
	}

	public ResourceItemType getType()
	{
		return type;
	}
}
