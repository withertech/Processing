package com.withertech.processing.blocks;

import com.withertech.processing.util.MathUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

import javax.annotation.Nonnull;

public class AbstractEnergyStorageContainer<T extends AbstractEnergyInventoryTileEntity> extends Container
{
	protected final T tileEntity;
	protected final IIntArray fields;

	protected AbstractEnergyStorageContainer(ContainerType<?> type, int id, T tileEntityIn, IIntArray fieldsIn)
	{
		super(type, id);
		this.tileEntity = tileEntityIn;
		this.fields = fieldsIn;

		trackIntArray(this.fields);
	}

	@Override
	public boolean canInteractWith(@Nonnull PlayerEntity playerIn)
	{
		// TODO
		return true;
	}

	public T getTileEntity()
	{
		return tileEntity;
	}

	public IIntArray getFields()
	{
		return fields;
	}

	public int getEnergyStored()
	{
		int upper = fields.get(1) & 0xFFFF;
		int lower = fields.get(0) & 0xFFFF;
		return (upper << 16) + lower;
	}

	public int getMaxEnergyStored()
	{
		int upper = fields.get(3) & 0xFFFF;
		int lower = fields.get(2) & 0xFFFF;
		return (upper << 16) + lower;
	}

	public int getEnergyBarHeight()
	{
		int max = getMaxEnergyStored();
		int energyClamped = MathUtils.clamp(getEnergyStored(), 0, max);
		return max > 0 ? 50 * energyClamped / max : 0;
	}
}
