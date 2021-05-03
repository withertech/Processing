package com.withertech.processing.inventory;

import com.withertech.processing.items.ItemMachineUpgrade;
import com.withertech.processing.util.Constants;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class UpgradeSlot extends Slot
{
	public UpgradeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack.getItem() instanceof ItemMachineUpgrade;
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack)
	{
		return Constants.UPGRADES_PER_SLOT;
	}
}
