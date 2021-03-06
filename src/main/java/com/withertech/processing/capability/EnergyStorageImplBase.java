package com.withertech.processing.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyStorageImplBase extends EnergyStorage implements ICapabilityProvider
{
	private final LazyOptional<EnergyStorageImplBase> lazy;

	public EnergyStorageImplBase(int capacity, int maxReceive, int maxExtract)
	{
		super(capacity, maxReceive, maxExtract, 0);
		this.lazy = LazyOptional.of(() -> this);
	}
	public void setCapacity(int c)
	{
		this.capacity = c;
		if(this.energy > c)
			this.energy = c;
	}
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		return CapabilityEnergy.ENERGY.orEmpty(cap, lazy.cast());
	}

	public void invalidate()
	{
		this.lazy.invalidate();
	}
}
