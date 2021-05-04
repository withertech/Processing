package com.withertech.processing.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class ResourceBlockItem extends BlockItem
{
	private final ResourceItemType type;
	public ResourceBlockItem(Block blockIn, Properties builder, ResourceItemType type)
	{
		super(blockIn, builder);
		this.type = type;
	}

	public ResourceItemType getType()
	{
		return type;
	}
}
