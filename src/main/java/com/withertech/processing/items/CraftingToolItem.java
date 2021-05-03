package com.withertech.processing.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CraftingToolItem extends Item
{
	public CraftingToolItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		ItemStack stack = itemStack.copy();
		stack.setDamage(stack.getDamage() + 1);
		return stack;
	}
}
