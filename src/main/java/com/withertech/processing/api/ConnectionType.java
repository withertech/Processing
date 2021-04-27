package com.withertech.processing.api;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

public enum ConnectionType implements IStringSerializable
{
	NONE,
	IN,
	OUT,
	BOTH;

	@Nonnull
	@Override
	public String getString()
	{
		return name().toLowerCase(Locale.ROOT);
	}

	public boolean canReceive()
	{
		return this == IN || this == BOTH;
	}

	public boolean canExtract()
	{
		return this == OUT || this == BOTH;
	}
}
