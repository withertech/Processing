package com.withertech.processing.items;

import com.withertech.processing.api.Face;
import com.withertech.processing.api.IWrenchable;
import com.withertech.processing.blocks.AbstractPortedMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemWrench extends Item
{

	public ItemWrench(Properties properties)
	{
		super(properties);
	}

	private static <T extends Comparable<T>> BlockState cycleProperty(BlockState state, Property<T> propertyIn)
	{
		return state.with(propertyIn, getAdjacentValue(propertyIn.getAllowedValues(), state.get(propertyIn)));
	}

	private static <T> T getAdjacentValue(Iterable<T> p_195959_0_, @Nullable T p_195959_1_)
	{
		return Util.getElementAfter(p_195959_0_, p_195959_1_);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		PlayerEntity player = context.getPlayer();
		if (player == null) return ActionResultType.PASS;

		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof IWrenchable)
		{
			ActionResultType result = ((IWrenchable) state.getBlock()).onWrench(context);
			if (result != ActionResultType.PASS)
			{
				return result;
			}
		}

		if (player.isCrouching() && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && state.getBlock() instanceof AbstractPortedMachineBlock)
		{
			Direction side = context.getFace();
			Face face = Face.getFaceFromDirection(side, state);
			if (face == Face.UP)
			{
				BlockState state1 = state.rotate(world, pos, Rotation.CLOCKWISE_90);
				world.setBlockState(pos, state1, 18);
				return ActionResultType.SUCCESS;
			}
		}

		return super.onItemUse(context);
	}

}