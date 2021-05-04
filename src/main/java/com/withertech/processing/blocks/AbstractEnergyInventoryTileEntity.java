package com.withertech.processing.blocks;

import com.withertech.processing.api.Face;
import com.withertech.processing.capability.EnergyStorageImpl;
import com.withertech.processing.util.EnergyUtils;
import com.withertech.processing.util.SyncVariable;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public abstract class AbstractEnergyInventoryTileEntity extends LockableBigInventoryTileEntity implements IEnergyHandler, ITickableTileEntity
{
	protected final EnergyStorageImpl energy;
	private final int maxExtract;


	private final IIntArray fields = new IIntArray()
	{
		@Override
		public int get(int index)
		{
			switch (index)
			{
				//Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
				case 0:
					// Energy lower bytes
					return AbstractEnergyInventoryTileEntity.this.getEnergyStored() & 0xFFFF;
				case 1:
					// Energy upper bytes
					return (AbstractEnergyInventoryTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
				case 2:
					// Max energy lower bytes
					return AbstractEnergyInventoryTileEntity.this.getMaxEnergyStored() & 0xFFFF;
				case 3:
					// Max energy upper bytes
					return (AbstractEnergyInventoryTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
				default:
					return 0;
			}
		}

		@Override
		public void set(int index, int value)
		{
			getEnergyImpl().setEnergyDirectly(value);
		}

		@Override
		public int size()
		{
			return 4;
		}
	};

	protected AbstractEnergyInventoryTileEntity(TileEntityType<?> typeIn, int inventorySize, int stackLimit, int maxEnergy, int maxReceive, int maxExtract)
	{
		super(typeIn, inventorySize, stackLimit);
		this.energy = new EnergyStorageImpl(maxEnergy, maxReceive, maxExtract, this);
		this.maxExtract = maxExtract;
	}

	@Override
	public EnergyStorageImpl getEnergyImpl()
	{
		return energy;
	}

	public IIntArray getFields()
	{
		return fields;
	}

	@Override
	public void tick()
	{
		if (world == null || world.isRemote) return;

		if (maxExtract > 0)
		{
			EnergyUtils.trySendToNeighbors(world, pos, this, maxExtract);
		}
	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT tags)
	{
		super.read(state, tags);
		SyncVariable.Helper.readSyncVars(this, tags);
		readEnergy(tags);
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT tags)
	{
		super.write(tags);
		SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
		writeEnergy(tags);
		return tags;
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		super.onDataPacket(net, packet);
		SyncVariable.Helper.readSyncVars(this, packet.getNbtCompound());
		readEnergy(packet.getNbtCompound());
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tags = super.getUpdateTag();
		SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
		writeEnergy(tags);
		return tags;
	}


	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if (!this.removed && cap == CapabilityEnergy.ENERGY)
		{
			if (this.getBlockState().get(AbstractFactoryMachineBlock.FACING_TO_PROPERTY_MAP.get(Face.getFaceFromDirection(side, this.getBlockState()))))
				return getEnergy(side).cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void remove()
	{
		super.remove();
		energy.invalidate();
	}
}
