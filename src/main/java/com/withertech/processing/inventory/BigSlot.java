package com.withertech.processing.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class BigSlot extends SlotItemHandler
{
	private final int index;

	public BigSlot(BigItemStackHandler itemHandler, int index, int xPosition, int yPosition)
	{
		super(itemHandler, index, xPosition, yPosition);
		this.index = index;
	}

	@Override
	public boolean canTakeStack(PlayerEntity playerIn)
	{
		return true;
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack)
	{
		return ((BigItemStackHandler) this.getItemHandler()).getStackLimit(this.index, stack);
	}

	@Override
	public boolean isSameInventory(Slot other)
	{
		return other instanceof BigSlot && ((BigSlot) other).getItemHandler() == this.getItemHandler();
	}
}