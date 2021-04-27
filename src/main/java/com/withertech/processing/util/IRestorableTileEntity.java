package com.withertech.processing.util;

import net.minecraft.nbt.CompoundNBT;

public interface IRestorableTileEntity
{

	boolean isWrenched();

	void setWrenched(boolean wrenched);

	void readRestorableFromNBT(CompoundNBT compound);

	void writeRestorableToNBT(CompoundNBT compound);
}
