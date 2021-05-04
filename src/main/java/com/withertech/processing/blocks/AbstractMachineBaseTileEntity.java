package com.withertech.processing.blocks;

import com.withertech.processing.api.RedstoneMode;
import com.withertech.processing.items.ItemMachineUpgrade;
import com.withertech.processing.util.EnumUtils;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbstractMachineBaseTileEntity extends AbstractEnergyInventoryTileEntity
{
	public static final int FIELDS_COUNT = 5;
	protected final MachineTier tier;

	protected RedstoneMode redstoneMode = RedstoneMode.IGNORED;

	protected final IIntArray fields = new IIntArray()
	{
		@Override
		public int get(int index)
		{
			switch (index)
			{
				//Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
				case 0:
					// Energy lower bytes
					return AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF;
				case 1:
					// Energy upper bytes
					return (AbstractMachineBaseTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
				case 2:
					// Max energy lower bytes
					return AbstractMachineBaseTileEntity.this.getMaxEnergyStored() & 0xFFFF;
				case 3:
					// Max energy upper bytes
					return (AbstractMachineBaseTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
				case 4:
					return AbstractMachineBaseTileEntity.this.redstoneMode.ordinal();
				default:
					return 0;
			}
		}

		@Override
		public void set(int index, int value)
		{
			if (index == 4)
			{
				AbstractMachineBaseTileEntity.this.redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
			}
		}

		@Override
		public int size()
		{
			return FIELDS_COUNT;
		}
	};

	protected AbstractMachineBaseTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract, MachineTier tier)
	{
		super(typeIn, inventorySize + tier.getUpgradeSlots(), 64 * tier.getStorageMultiplier(), maxEnergy, maxReceive, maxExtract);
		this.tier = tier;
	}

	public RedstoneMode getRedstoneMode()
	{
		return redstoneMode;
	}

	public void setRedstoneMode(RedstoneMode redstoneMode)
	{
		this.redstoneMode = redstoneMode;
	}

	public MachineTier getMachineTier()
	{
		return tier;
	}

	public int getUpgradeCount(IItemProvider upgradeItem)
	{
		int count = 0;
		for (int i = getSizeInventory() - tier.getUpgradeSlots(); i < getSizeInventory(); ++i)
		{
			ItemStack stack = getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == upgradeItem.asItem())
			{
				count += stack.getCount();
			}
		}
		return count;
	}

	protected float getUpgradesEnergyMultiplier()
	{
		float cost = 1f;
		for (int i = getSizeInventory() - tier.getUpgradeSlots(); i < getSizeInventory(); ++i)
		{
			ItemStack stack = getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemMachineUpgrade)
			{
				cost += stack.getCount() * ((ItemMachineUpgrade) stack.getItem()).getUpgrade().getEnergyUsageMultiplier();
			}
		}
		return cost;
	}

	@Override
	public IIntArray getFields()
	{
		return fields;
	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT tags)
	{
		super.read(state, tags);
		this.redstoneMode = EnumUtils.byOrdinal(tags.getByte("RedstoneMode"), RedstoneMode.IGNORED);
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT tags)
	{
		super.write(tags);
		tags.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
		return tags;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		super.onDataPacket(net, packet);
		CompoundNBT tags = packet.getNbtCompound();
		this.redstoneMode = EnumUtils.byOrdinal(tags.getByte("RedstoneMode"), RedstoneMode.IGNORED);
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tags = super.getUpdateTag();
		tags.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
		return tags;
	}
}
