package com.withertech.processing.blocks.pipe;

import com.google.common.collect.Maps;
import com.withertech.processing.Processing;
import com.withertech.processing.api.ConnectionType;
import com.withertech.processing.api.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class FluidPipeBlock extends SixWayBlock implements IWrenchable
{
	public static final EnumProperty<ConnectionType> NORTH = EnumProperty.create("north", ConnectionType.class);
	public static final EnumProperty<ConnectionType> EAST = EnumProperty.create("east", ConnectionType.class);
	public static final EnumProperty<ConnectionType> SOUTH = EnumProperty.create("south", ConnectionType.class);
	public static final EnumProperty<ConnectionType> WEST = EnumProperty.create("west", ConnectionType.class);
	public static final EnumProperty<ConnectionType> UP = EnumProperty.create("up", ConnectionType.class);
	public static final EnumProperty<ConnectionType> DOWN = EnumProperty.create("down", ConnectionType.class);
	public static final Map<Direction, EnumProperty<ConnectionType>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (map) ->
	{
		map.put(Direction.NORTH, NORTH);
		map.put(Direction.EAST, EAST);
		map.put(Direction.SOUTH, SOUTH);
		map.put(Direction.WEST, WEST);
		map.put(Direction.UP, UP);
		map.put(Direction.DOWN, DOWN);
	});

	public FluidPipeBlock()
	{
		super(0.15f, Properties.create(Material.IRON)
				.hardnessAndResistance(25, 1200)
				.harvestLevel(3)
				.sound(SoundType.METAL)
				.harvestTool(ToolType.PICKAXE)
				.notSolid());
		this.setDefaultState(this.stateContainer.getBaseState()
				.with(NORTH, ConnectionType.NONE)
				.with(EAST, ConnectionType.NONE)
				.with(SOUTH, ConnectionType.NONE)
				.with(WEST, ConnectionType.NONE)
				.with(UP, ConnectionType.NONE)
				.with(DOWN, ConnectionType.NONE));
	}

	@Nullable
	private static Direction getClickedConnection(Vector3d relative)
	{
		if (relative.x < 0.25)
			return Direction.WEST;
		if (relative.x > 0.75)
			return Direction.EAST;
		if (relative.y < 0.25)
			return Direction.DOWN;
		if (relative.y > 0.75)
			return Direction.UP;
		if (relative.z < 0.25)
			return Direction.NORTH;
		if (relative.z > 0.75)
			return Direction.SOUTH;
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> BlockState cycleProperty(BlockState state, Property<T> propertyIn)
	{
		T value = getAdjacentValue(propertyIn.getAllowedValues(), state.get(propertyIn));
		if (value == ConnectionType.NONE || value == ConnectionType.BOTH)
			value = (T) ConnectionType.IN;
		return state.with(propertyIn, value);
	}

	private static <T> T getAdjacentValue(Iterable<T> p_195959_0_, @Nullable T p_195959_1_)
	{
		return Util.getElementAfter(p_195959_0_, p_195959_1_);
	}

	private static ConnectionType createConnection(IBlockReader worldIn, BlockPos pos, Direction side, ConnectionType current)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos.offset(side));
		if (tileEntity instanceof TileFluidPipe)
		{
			return ConnectionType.BOTH;
		} else if (tileEntity != null)
		{
			IFluidHandler fluid = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()).orElse(tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null));
			if (fluid != null)
			{
				return current == ConnectionType.NONE ? ConnectionType.OUT : current;
			}
		}
		return ConnectionType.NONE;
	}

	public static ConnectionType getConnection(BlockState state, Direction side)
	{
		return state.get(FACING_TO_PROPERTY_MAP.get(side));
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileFluidPipe();
	}

	@Override
	public ActionResultType onWrench(ItemUseContext context)
	{
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		BlockState state = world.getBlockState(pos);
		Vector3d relative = context.getHitVec().subtract(pos.getX(), pos.getY(), pos.getZ());
		Processing.LOGGER.debug("onWrench: {}", relative);

		Direction side = getClickedConnection(relative);
		if (side != null)
		{
			TileEntity other = world.getTileEntity(pos.offset(side));
			if (!(other instanceof TileFluidPipe))
			{
				BlockState state1 = cycleProperty(state, FACING_TO_PROPERTY_MAP.get(side));
				world.setBlockState(pos, state1, 18);
				NetManFluidPipe.update(world, pos);
				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.makeConnections(context.getWorld(), context.getPos());
	}

	public BlockState makeConnections(IBlockReader worldIn, BlockPos pos)
	{
		return this.getDefaultState()
				.with(DOWN, createConnection(worldIn, pos, Direction.DOWN, ConnectionType.NONE))
				.with(UP, createConnection(worldIn, pos, Direction.UP, ConnectionType.NONE))
				.with(NORTH, createConnection(worldIn, pos, Direction.NORTH, ConnectionType.NONE))
				.with(EAST, createConnection(worldIn, pos, Direction.EAST, ConnectionType.NONE))
				.with(SOUTH, createConnection(worldIn, pos, Direction.SOUTH, ConnectionType.NONE))
				.with(WEST, createConnection(worldIn, pos, Direction.WEST, ConnectionType.NONE));
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid)
	{
//		if (world.getTileEntity(pos) instanceof TileFluidPipe)
//		{
//			FluidStack stack = NetManFluidPipe.get(world, pos).getFluidInTank(1).copy();
//			NetManFluidPipe.update(world, pos, stack);
//		}
		return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}

	@Nonnull
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(@Nonnull BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, IWorld worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos)
	{
		if (worldIn.getTileEntity(facingPos) instanceof TileFluidPipe)
		{
			NetManFluidPipe.update(worldIn, currentPos);
		}

		EnumProperty<ConnectionType> property = FACING_TO_PROPERTY_MAP.get(facing);
		ConnectionType current = stateIn.get(property);
		return stateIn.with(property, createConnection(worldIn, currentPos, facing, current));
	}

	@Override
	protected int getShapeIndex(@Nonnull BlockState state)
	{
		int i = 0;

		for (int j = 0; j < Direction.values().length; ++j)
		{
			if (state.get(FACING_TO_PROPERTY_MAP.get(Direction.values()[j])) != ConnectionType.NONE)
			{
				i |= 1 << j;
			}
		}

		return i;
	}
}
