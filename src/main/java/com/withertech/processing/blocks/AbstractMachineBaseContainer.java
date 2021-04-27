package com.withertech.processing.blocks;

import com.withertech.processing.api.RedstoneMode;
import com.withertech.processing.inventory.SlotMachineUpgrade;
import com.withertech.processing.util.EnumUtils;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public class AbstractMachineBaseContainer<T extends AbstractMachineBaseTileEntity> extends AbstractEnergyStorageContainer<T>
{
	protected AbstractMachineBaseContainer(ContainerType<?> type, int id, T tileEntityIn, IIntArray fieldsIn)
	{
		super(type, id, tileEntityIn, fieldsIn);
	}

	protected final void addUpgradeSlots()
	{
		int upgradeSlots = this.tileEntity.tier.getUpgradeSlots();
		int inventorySize = this.tileEntity.getSizeInventory();
		for (int i = 0; i < upgradeSlots; ++i)
		{
			int index = inventorySize - upgradeSlots + i;
			int xPosition = 6 + 18 * i;
			this.addSlot(new SlotMachineUpgrade(this.tileEntity, index, xPosition, -13));
		}
	}

	public RedstoneMode getRedstoneMode()
	{
		return EnumUtils.byOrdinal(fields.get(4), RedstoneMode.IGNORED);
	}

	public void setRedstoneMode(RedstoneMode mode)
	{
		fields.set(4, mode.ordinal());
	}
}
