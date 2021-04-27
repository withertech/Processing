package com.withertech.processing.blocks.pipe;


import com.withertech.processing.init.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileFluidPipe extends TileEntity
{
	public TileFluidPipe()
	{
		super(ModTiles.FLUID_PIPE.get());
	}

	public String getPipeNetworkData()
	{
		if (world == null) return "world is null";

		NetFluidPipe net = NetManFluidPipe.get(world, pos);
		return net != null ? net.toString() : "null";
	}

	public NetFluidPipe getPipeNetwork()
	{
		if (world == null) return null;

		return NetManFluidPipe.get(world, pos);
	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT compound)
	{
		super.read(state, compound);
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT compound)
	{
		return super.write(compound);
	}

	@Override
	public void remove()
	{
		if (world != null)
		{
			NetManFluidPipe.update(world, pos);
		}
		super.remove();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if (world != null && !removed && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side != null)
		{
			LazyOptional<NetFluidPipe> networkOptional = NetManFluidPipe.getLazy(world, pos);
			if (networkOptional.isPresent())
			{
				return networkOptional.orElseThrow(IllegalStateException::new).getConnection(pos, side).getLazyOptional().cast();
			}
		}
		return LazyOptional.empty();
	}

}
