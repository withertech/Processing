package com.withertech.processing.blocks.cell;

import com.withertech.processing.init.ModTiles;
import com.withertech.processing.util.IRestorableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidTankTile extends TileEntity implements IRestorableTileEntity
{
	private final FluidTank tank = new FluidTank(10000)
	{
		@Override
		protected void onContentsChanged()
		{
			assert world != null;
			BlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			markDirty();
		}
	};

	private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

	public FluidTankTile()
	{
		super(ModTiles.FLUID_TANK.get());
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbtTag = super.getUpdateTag();
		CompoundNBT tankNBT = new CompoundNBT();
		tank.writeToNBT(tankNBT);
		nbtTag.put("tank", tankNBT);
		return nbtTag;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		tank.readFromNBT(packet.getNbtCompound().getCompound("tank"));
	}

	public ActionResultType activate(PlayerEntity player, Hand hand)
	{
		FluidUtil.interactWithFluidHandler(player, hand, this.tank);
		return ActionResultType.FAIL;
	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT tagCompound)
	{
		super.read(state, tagCompound);
		readRestorableFromNBT(tagCompound);
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound)
	{
		writeRestorableToNBT(compound);
		return super.write(compound);
	}

	@Override
	public boolean isWrenched()
	{
		return false;
	}

	@Override
	public void setWrenched(boolean wrenched)
	{

	}

	@Override
	public void readRestorableFromNBT(CompoundNBT compound)
	{
		tank.readFromNBT(compound.getCompound("tank"));
	}

	@Override
	public void writeRestorableToNBT(CompoundNBT compound)
	{
		CompoundNBT tankNBT = new CompoundNBT();
		tank.writeToNBT(tankNBT);
		compound.put("tank", tankNBT);
	}


	public FluidTank getTank()
	{
		return this.tank;
	}


	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return holder.cast();
		return super.getCapability(capability, facing);
	}

}
