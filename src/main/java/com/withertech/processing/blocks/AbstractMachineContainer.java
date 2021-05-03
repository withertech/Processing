package com.withertech.processing.blocks;

import com.withertech.processing.inventory.BigSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractMachineContainer<T extends AbstractMachineTileEntity<?>> extends AbstractMachineBaseContainer<T>
{
	protected AbstractMachineContainer(ContainerType<?> containerTypeIn, int id, T tileEntityIn, IIntArray fieldsIn)
	{
		super(containerTypeIn, id, tileEntityIn, fieldsIn);
	}

	public static boolean canAddItemToSlot(@Nullable Slot slot, @Nonnull ItemStack stack, boolean stackSizeMatters)
	{
		boolean flag = slot == null || !slot.getHasStack();
		if (slot != null)
		{
			ItemStack slotStack = slot.getStack();

			if (!flag && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(slotStack, stack))
			{
				return slotStack.getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slot.getItemStackLimit(slotStack);
			}
		}
		return flag;
	}

	public int getProgress()
	{
		return fields.get(5);
	}

	public int getProcessTime()
	{
		return fields.get(6);
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		PlayerInventory PlayerInventory = player.inventory;

		if (clickTypeIn == ClickType.QUICK_CRAFT)
		{
			int j1 = this.dragEvent;
			this.dragEvent = getDragEvent(dragType);

			if ((j1 != 1 || this.dragEvent != 2) && j1 != this.dragEvent)
			{
				this.resetDrag();
			} else if (PlayerInventory.getItemStack().isEmpty())
			{
				this.resetDrag();
			} else if (this.dragEvent == 0)
			{
				this.dragMode = extractDragMode(dragType);

				if (isValidDragMode(this.dragMode, player))
				{
					this.dragEvent = 1;
					this.dragSlots.clear();
				} else
				{
					this.resetDrag();
				}
			} else if (this.dragEvent == 1)
			{
				Slot slot7 = this.inventorySlots.get(slotId);
				ItemStack mouseStack = PlayerInventory.getItemStack();

				if (slot7 != null && AbstractMachineContainer.canAddItemToSlot(slot7, mouseStack, true) && slot7.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7))
				{
					this.dragSlots.add(slot7);
				}
			} else if (this.dragEvent == 2)
			{
				if (!this.dragSlots.isEmpty())
				{
					ItemStack mouseStackCopy = PlayerInventory.getItemStack().copy();
					int k1 = PlayerInventory.getItemStack().getCount();

					for (Slot dragSlot : this.dragSlots)
					{
						ItemStack mouseStack = PlayerInventory.getItemStack();

						if (dragSlot != null && AbstractMachineContainer.canAddItemToSlot(dragSlot, mouseStack, true) && dragSlot.isItemValid(mouseStack) && (this.dragMode == 2 || mouseStack.getCount() >= this.dragSlots.size()) && this.canDragIntoSlot(dragSlot))
						{
							ItemStack itemstack14 = mouseStackCopy.copy();
							int j3 = dragSlot.getHasStack() ? dragSlot.getStack().getCount() : 0;
							computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
							int k3 = dragSlot.getItemStackLimit(itemstack14);

							if (itemstack14.getCount() > k3)
							{
								itemstack14.setCount(k3);
							}

							k1 -= itemstack14.getCount() - j3;
							dragSlot.putStack(itemstack14);
						}
					}

					mouseStackCopy.setCount(k1);
					PlayerInventory.setItemStack(mouseStackCopy);
				}

				this.resetDrag();
			} else
			{
				this.resetDrag();
			}
		} else if (this.dragEvent != 0)
		{
			this.resetDrag();
		} else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1))
		{
			if (slotId == -999)
			{
				if (!PlayerInventory.getItemStack().isEmpty())
				{
					if (dragType == 0)
					{
						player.dropItem(PlayerInventory.getItemStack(), true);
						PlayerInventory.setItemStack(ItemStack.EMPTY);
					}

					if (dragType == 1)
					{
						player.dropItem(PlayerInventory.getItemStack().split(1), true);
					}
				}
			} else if (clickTypeIn == ClickType.QUICK_MOVE)
			{
				if (slotId < 0)
				{
					return ItemStack.EMPTY;
				}

				Slot slot5 = this.inventorySlots.get(slotId);

				if (slot5 == null || !slot5.canTakeStack(player))
				{
					return ItemStack.EMPTY;
				}

				for (ItemStack itemstack7 = this.transferStackInSlot(player, slotId); !itemstack7.isEmpty() && ItemStack.areItemsEqual(slot5.getStack(), itemstack7); itemstack7 = this.transferStackInSlot(player, slotId))
				{
					itemstack = itemstack7.copy();
				}
			} else
			{
				if (slotId < 0)
				{
					return ItemStack.EMPTY;
				}

				Slot slot6 = this.inventorySlots.get(slotId);

				if (slot6 != null)
				{
					ItemStack slotStack = slot6.getStack();
					ItemStack mouseStack = PlayerInventory.getItemStack();

					if (!slotStack.isEmpty())
					{
						itemstack = slotStack.copy();
					}

					if (slotStack.isEmpty())
					{
						if (!mouseStack.isEmpty() && slot6.isItemValid(mouseStack))
						{
							int i3 = dragType == 0 ? mouseStack.getCount() : 1;

							if (i3 > slot6.getItemStackLimit(mouseStack))
							{
								i3 = slot6.getItemStackLimit(mouseStack);
							}

							slot6.putStack(mouseStack.split(i3));
						}
					} else if (slot6.canTakeStack(player))
					{
						if (mouseStack.isEmpty())
						{
							if (slotStack.isEmpty())
							{
								slot6.putStack(ItemStack.EMPTY);
								PlayerInventory.setItemStack(ItemStack.EMPTY);
							} else
							{
								int toMove;
								if (slot6 instanceof BigSlot)
								{
									if (slotStack.getMaxStackSize() < slotStack.getCount())
										toMove = dragType == 0 ? slotStack.getMaxStackSize() : (slotStack.getMaxStackSize() + 1) / 2;
									else toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
								} else
								{
									toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
								}
								//int toMove = dragType == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
								PlayerInventory.setItemStack(slot6.decrStackSize(toMove));

								if (slotStack.isEmpty())
								{
									slot6.putStack(ItemStack.EMPTY);
								}

								slot6.onTake(player, PlayerInventory.getItemStack());
							}
						} else if (slot6.isItemValid(mouseStack))
						{
							if (slotStack.getItem() == mouseStack.getItem() && ItemStack.areItemStackTagsEqual(slotStack, mouseStack))
							{
								int k2 = dragType == 0 ? mouseStack.getCount() : 1;

								if (k2 > slot6.getItemStackLimit(mouseStack) - slotStack.getCount())
								{
									k2 = slot6.getItemStackLimit(mouseStack) - slotStack.getCount();
								}

								mouseStack.shrink(k2);
								slotStack.grow(k2);
							} else if (mouseStack.getCount() <= slot6.getItemStackLimit(mouseStack) && slotStack.getCount() <= slotStack.getMaxStackSize())
							{
								slot6.putStack(mouseStack);
								PlayerInventory.setItemStack(slotStack);
							}
						} else if (slotStack.getItem() == mouseStack.getItem() && mouseStack.getMaxStackSize() > 1 && ItemStack.areItemStackTagsEqual(slotStack, mouseStack) && !slotStack.isEmpty())
						{
							int j2 = slotStack.getCount();

							if (j2 + mouseStack.getCount() <= mouseStack.getMaxStackSize())
							{
								mouseStack.grow(j2);
								slotStack = slot6.decrStackSize(j2);

								if (slotStack.isEmpty())
								{
									slot6.putStack(ItemStack.EMPTY);
								}

								slot6.onTake(player, PlayerInventory.getItemStack());
							}
						}
					}

					slot6.onSlotChanged();
				}
			}
		} else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9)
		{
			//just don't do anything as swapping slots is unsupported
      /*
      Slot slot4 = this.inventorySlots.get(slotId);
      ItemStack itemstack6 = PlayerInventory.getStackInSlot(dragType);
      ItemStack slotStack = slot4.getStack();
      if (!itemstack6.isEmpty() || !slotStack.isEmpty()) {
        if (itemstack6.isEmpty()) {
          if (slot4.canTakeStack(player)) {
            int maxAmount = slotStack.getMaxStackSize();
            if (slotStack.getCount() > maxAmount) {
              ItemStack newSlotStack = slotStack.copy();
              ItemStack takenStack = newSlotStack.split(maxAmount);
              PlayerInventory.setInventorySlotContents(dragType, takenStack);
              //slot4.onSwapCraft(takenStack.getCount());
              slot4.putStack(newSlotStack);
              slot4.onTake(player, takenStack);
            } else {
              PlayerInventory.setInventorySlotContents(dragType, slotStack);
              //slot4.onSwapCraft(slotStack.getCount());
              slot4.putStack(ItemStack.EMPTY);
              slot4.onTake(player, slotStack);
            }
          }
        } else if (slotStack.isEmpty()) {
          if (slot4.isItemValid(itemstack6)) {
            int l1 = slot4.getItemStackLimit(itemstack6);
            if (itemstack6.getCount() > l1) {
              slot4.putStack(itemstack6.split(l1));
            } else {
              slot4.putStack(itemstack6);
              PlayerInventory.setInventorySlotContents(dragType, ItemStack.EMPTY);
            }
          }
        } else if (slot4.canTakeStack(player) && slot4.isItemValid(itemstack6)) {
          int i2 = slot4.getItemStackLimit(itemstack6);
          if (itemstack6.getCount() > i2) {
            slot4.putStack(itemstack6.split(i2));
            slot4.onTake(player, slotStack);
            if (!PlayerInventory.addItemStackToInventory(slotStack)) {
              player.dropItem(slotStack, true);
            }
          } else {
            slot4.putStack(itemstack6);
            if (slotStack.getCount() > slotStack.getMaxStackSize()) {
              ItemStack remainder = slotStack.copy();
              PlayerInventory.setInventorySlotContents(dragType, remainder.split(slotStack.getMaxStackSize()));
              if (!PlayerInventory.addItemStackToInventory(remainder)) {
                player.dropItem(remainder, true);
              }
            } else {
              PlayerInventory.setInventorySlotContents(dragType, slotStack);
            }
            slot4.onTake(player, slotStack);
          }
        }
      }*/
		} else if (clickTypeIn == ClickType.CLONE && player.abilities.isCreativeMode && PlayerInventory.getItemStack().isEmpty() && slotId >= 0)
		{
			Slot slot3 = this.inventorySlots.get(slotId);

			if (slot3 != null && slot3.getHasStack())
			{
				ItemStack itemstack5 = slot3.getStack().copy();
				itemstack5.setCount(itemstack5.getMaxStackSize());
				PlayerInventory.setItemStack(itemstack5);
			}
		} else if (clickTypeIn == ClickType.THROW && PlayerInventory.getItemStack().isEmpty() && slotId >= 0)
		{
			Slot slot2 = this.inventorySlots.get(slotId);

			if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(player))
			{
				ItemStack itemstack4 = slot2.decrStackSize(dragType == 0 ? 1 : slot2.getStack().getCount());
				slot2.onTake(player, itemstack4);
				player.dropItem(itemstack4, true);
			}
		} else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0)
		{
			Slot slot = this.inventorySlots.get(slotId);
			ItemStack mouseStack = PlayerInventory.getItemStack();

			if (!mouseStack.isEmpty() && (slot == null || !slot.getHasStack() || !slot.canTakeStack(player)))
			{
				int i = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
				int j = dragType == 0 ? 1 : -1;

				for (int k = 0; k < 2; ++k)
				{
					for (int l = i; l >= 0 && l < this.inventorySlots.size() && mouseStack.getCount() < mouseStack.getMaxStackSize(); l += j)
					{
						Slot slot1 = this.inventorySlots.get(l);

						if (slot1.getHasStack() && AbstractMachineContainer.canAddItemToSlot(slot1, mouseStack, true) && slot1.canTakeStack(player) && this.canMergeSlot(mouseStack, slot1))
						{
							ItemStack itemstack2 = slot1.getStack();

							if (k != 0 || itemstack2.getCount() < slot1.getItemStackLimit(itemstack2))
							{
								int i1 = Math.min(mouseStack.getMaxStackSize() - mouseStack.getCount(), itemstack2.getCount());
								ItemStack itemstack3 = slot1.decrStackSize(i1);
								mouseStack.grow(i1);

								if (itemstack3.isEmpty())
								{
									slot1.putStack(ItemStack.EMPTY);
								}

								slot1.onTake(player, itemstack3);
							}
						}
					}
				}
			}

			this.detectAndSendChanges();
		}

		if (itemstack.getCount() > 64)
		{
			itemstack = itemstack.copy();
			itemstack.setCount(64);
		}

		return itemstack;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection)
		{
			i = endIndex - 1;
		}

		while (!stack.isEmpty())
		{
			if (reverseDirection)
			{
				if (i < startIndex) break;
			} else
			{
				if (i >= endIndex) break;
			}

			Slot slot = this.inventorySlots.get(i);
			ItemStack itemstack = slot.getStack();

			if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && ItemStack.areItemStackTagsEqual(stack, itemstack))
			{
				int j = itemstack.getCount() + stack.getCount();
				int maxSize = slot.getItemStackLimit(itemstack);

				if (j <= maxSize)
				{
					stack.setCount(0);
					itemstack.setCount(j);
					slot.onSlotChanged();
					flag = true;
				} else if (itemstack.getCount() < maxSize)
				{
					stack.shrink(maxSize - itemstack.getCount());
					itemstack.setCount(maxSize);
					slot.onSlotChanged();
					flag = true;
				}
			}

			i += (reverseDirection) ? -1 : 1;
		}

		if (!stack.isEmpty())
		{
			if (reverseDirection) i = endIndex - 1;
			else i = startIndex;

			while (true)
			{
				if (reverseDirection)
				{
					if (i < startIndex) break;
				} else
				{
					if (i >= endIndex) break;
				}

				Slot slot1 = this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				if (itemstack1.isEmpty() && slot1.isItemValid(stack))
				{
					if (stack.getCount() > slot1.getItemStackLimit(stack))
					{
						slot1.putStack(stack.split(slot1.getItemStackLimit(stack)));
					} else
					{
						slot1.putStack(stack.split(stack.getCount()));
					}

					slot1.onSlotChanged();
					flag = true;
					break;
				}

				i += (reverseDirection) ? -1 : 1;
			}
		}

		return flag;
	}
}
