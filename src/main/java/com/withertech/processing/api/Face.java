package com.withertech.processing.api;

import com.withertech.processing.blocks.AbstractPortedMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public enum Face
{
	FRONT,
	BACK,
	LEFT,
	RIGHT,
	DOWN,
	UP;

	public static Face getFaceFromDirection(Direction dir, BlockState state)
	{
		switch (state.get(AbstractPortedMachineBlock.FACING))
		{
			case NORTH:
				switch (dir)
				{
					case NORTH:
						return Face.FRONT;
					case SOUTH:
						return Face.BACK;
					case EAST:
						return Face.RIGHT;
					case WEST:
						return Face.LEFT;
					case UP:
						return Face.UP;
					case DOWN:
						return Face.DOWN;
				}
			case SOUTH:
				switch (dir)
				{
					case NORTH:
						return Face.BACK;
					case SOUTH:
						return Face.FRONT;
					case EAST:
						return Face.LEFT;
					case WEST:
						return Face.RIGHT;
					case UP:
						return Face.UP;
					case DOWN:
						return Face.DOWN;
				}
			case EAST:
				switch (dir)
				{
					case NORTH:
						return Face.LEFT;
					case SOUTH:
						return Face.RIGHT;
					case EAST:
						return Face.FRONT;
					case WEST:
						return Face.BACK;
					case UP:
						return Face.UP;
					case DOWN:
						return Face.DOWN;
				}
			case WEST:
				switch (dir)
				{
					case NORTH:
						return Face.RIGHT;
					case SOUTH:
						return Face.LEFT;
					case EAST:
						return Face.BACK;
					case WEST:
						return Face.FRONT;
					case UP:
						return Face.UP;
					case DOWN:
						return Face.DOWN;
				}
			default:
				return null;
		}
	}

	public static Direction getDirectionFromFace(Face face, BlockState state)
	{
		return getDirectionFromFace(face, state.get(AbstractPortedMachineBlock.FACING));
	}

	public static Direction getDirectionFromFace(Face face, Direction facing)
	{
		switch (facing)
		{
			case NORTH:
				switch (face)
				{
					case FRONT:
						return Direction.NORTH;
					case BACK:
						return Direction.SOUTH;
					case RIGHT:
						return Direction.EAST;
					case LEFT:
						return Direction.WEST;
					case UP:
						return Direction.UP;
					case DOWN:
						return Direction.DOWN;
				}
			case SOUTH:
				switch (face)
				{
					case BACK:
						return Direction.NORTH;
					case FRONT:
						return Direction.SOUTH;
					case LEFT:
						return Direction.EAST;
					case RIGHT:
						return Direction.WEST;
					case UP:
						return Direction.UP;
					case DOWN:
						return Direction.DOWN;
				}
			case EAST:
				switch (face)
				{
					case LEFT:
						return Direction.NORTH;
					case RIGHT:
						return Direction.SOUTH;
					case FRONT:
						return Direction.EAST;
					case BACK:
						return Direction.WEST;
					case UP:
						return Direction.UP;
					case DOWN:
						return Direction.DOWN;
				}
			case WEST:
				switch (face)
				{
					case RIGHT:
						return Direction.NORTH;
					case LEFT:
						return Direction.SOUTH;
					case BACK:
						return Direction.EAST;
					case FRONT:
						return Direction.WEST;
					case UP:
						return Direction.UP;
					case DOWN:
						return Direction.DOWN;
				}
			default:
				return null;
		}
	}
}
