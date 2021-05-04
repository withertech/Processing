package com.withertech.processing.blocks.crusher;

import com.withertech.processing.blocks.AbstractMachineContainer;
import com.withertech.processing.blocks.AbstractMachineTileEntity;
import com.withertech.processing.init.MachineType;
import com.withertech.processing.inventory.BigSlot;
import com.withertech.processing.util.InventoryUtils;
import com.withertech.processing.util.MachineTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

import javax.annotation.Nonnull;

public class CrusherContainer extends AbstractMachineContainer<CrusherTile>
{
	public CrusherContainer(int id, PlayerInventory playerInventory, MachineTier tier)
	{
		this(id, playerInventory, MachineType.CRUSHER.create(tier), new IntArray(AbstractMachineTileEntity.FIELDS_COUNT));
	}

	public CrusherContainer(int id, PlayerInventory playerInventory, CrusherTile tileEntity, IIntArray fieldsIn)
	{
		super(MachineType.CRUSHER.getContainerType(tileEntity.getMachineTier()), id, tileEntity, fieldsIn);

		this.addSlot(new BigSlot(this.tileEntity.getHandler(), 0, 26, 35)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return tileEntity.isIngredient(stack);
			}
		});
		this.addSlot(new Slot(this.tileEntity, 1, 80, 35)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});
		this.addSlot(new Slot(this.tileEntity, 2, 98, 35)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});
		this.addSlot(new Slot(this.tileEntity, 3, 116, 35)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});
		this.addSlot(new Slot(this.tileEntity, 4, 134, 35)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}
		});

		InventoryUtils.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

		this.addUpgradeSlots();
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(@Nonnull PlayerEntity playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			final int inventorySize = 5;
			final int playerInventoryEnd = inventorySize + 27;
			final int playerHotbarEnd = playerInventoryEnd + 9;

			if (index > 0 && index < inventorySize)
			{
				if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, true))
				{
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index != 0)
			{
				if (this.isCrushingIngredient(itemstack1))
				{
					if (!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (index < playerInventoryEnd)
				{
					if (!this.mergeItemStack(itemstack1, playerInventoryEnd, playerHotbarEnd, false))
					{
						return ItemStack.EMPTY;
					}
				} else if (index < playerHotbarEnd && !this.mergeItemStack(itemstack1, inventorySize, playerInventoryEnd, false))
				{
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, false))
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	private boolean isCrushingIngredient(ItemStack stack)
	{
		// TODO
		return true;
	}
}
