package com.withertech.processing.util;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public final class InventoryUtils
{
	private InventoryUtils()
	{
		throw new IllegalAccessError("Utility class");
	}

	/**
	 * Gets the total number of matching items in all slots in the inventory.
	 *
	 * @param inventory  The inventory
	 * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
	 * @return The number of items in all matching item stacks
	 */
	public static int getTotalCount(IInventory inventory, Predicate<ItemStack> ingredient)
	{
		int total = 0;
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty() && ingredient.test(stack))
			{
				total += stack.getCount();
			}
		}
		return total;
	}

	/**
	 * Consumes (removes) items from the inventory. This is useful for machines, which may have
	 * multiple input slots and recipes that consume multiple of one item.
	 *
	 * @param inventory  The inventory
	 * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
	 * @param amount     The total number of items to remove
	 */
	public static void consumeItems(IInventory inventory, Predicate<ItemStack> ingredient, int amount)
	{
		int amountLeft = amount;
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (!stack.isEmpty() && ingredient.test(stack))
			{
				int toRemove = Math.min(amountLeft, stack.getCount());

				stack.shrink(toRemove);
				if (stack.isEmpty())
				{
					inventory.setInventorySlotContents(i, ItemStack.EMPTY);
				}

				amountLeft -= toRemove;
				if (amountLeft == 0)
				{
					return;
				}
			}
		}
	}

	public static boolean canItemsStack(ItemStack a, ItemStack b)
	{
		// Determine if the item stacks can be merged
		if (a.isEmpty() || b.isEmpty()) return true;
		return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
	}

	public static boolean mergeItem(IInventory inventory, ItemStack stack, int slot)
	{
		ItemStack current = inventory.getStackInSlot(slot);
		if (current.isEmpty())
		{
			inventory.setInventorySlotContents(slot, stack);
			return true;
		} else if (canItemsStack(stack, current))
		{
			current.grow(stack.getCount());
			return true;
		}
		return false;
	}

	public static Collection<Slot> createPlayerSlots(PlayerInventory playerInventory, int startX, int startY)
	{
		Collection<Slot> list = new ArrayList<>();
		// Backpack
		for (int y = 0; y < 3; ++y)
		{
			for (int x = 0; x < 9; ++x)
			{
				list.add(new Slot(playerInventory, x + y * 9 + 9, startX + x * 18, startY + y * 18));
			}
		}
		// Hotbar
		for (int x = 0; x < 9; ++x)
		{
			list.add(new Slot(playerInventory, x, 8 + x * 18, startY + 58));
		}
		return list;
	}

	public static boolean isFilledFluidContainer(ItemStack stack)
	{
		Item item = stack.getItem();
		return (item instanceof BucketItem && ((BucketItem) item).getFluid() != Fluids.EMPTY)
				/*|| (item instanceof CanisterItem && !((CanisterItem) item).getFluid(stack).isEmpty())*/;
	}

	public static boolean isEmptyFluidContainer(ItemStack stack)
	{
		Item item = stack.getItem();
		return (item instanceof BucketItem && ((BucketItem) item).getFluid() == Fluids.EMPTY)
				/*|| (item instanceof CanisterItem && ((CanisterItem) item).getFluid(stack).isEmpty())*/;
	}

//	public static boolean canFluidsStack(FluidStack stack, FluidStack output) {
//		return output.isEmpty() || (output.isFluidEqual(stack) && output.getAmount() + stack.getAmount() <= RefineryTileEntity.TANK_CAPACITY);
//	}
}
