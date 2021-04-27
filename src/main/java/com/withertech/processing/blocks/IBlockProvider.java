package com.withertech.processing.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nonnull;

public interface IBlockProvider extends IItemProvider
{
	/**
	 * Get the block this object represents.
	 *
	 * @return The block, which may be newly constructed
	 */
	Block asBlock();

	/**
	 * Shortcut for getting the default state of the block.
	 *
	 * @return Default block state
	 */
	default BlockState asBlockState()
	{
		return asBlock().getDefaultState();
	}

	@Nonnull
	@Override
	default Item asItem()
	{
		return asBlock().asItem();
	}
}
