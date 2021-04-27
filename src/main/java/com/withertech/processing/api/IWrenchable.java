package com.withertech.processing.api;

import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

@FunctionalInterface
public interface IWrenchable
{
	ActionResultType onWrench(ItemUseContext context);
}
