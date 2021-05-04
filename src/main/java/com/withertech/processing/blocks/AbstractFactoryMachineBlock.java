package com.withertech.processing.blocks;

import com.google.common.collect.Maps;
import com.withertech.processing.Processing;
import com.withertech.processing.api.Face;
import com.withertech.processing.api.IWrenchable;
import com.withertech.processing.blocks.pipe.FluidPipeTile;
import com.withertech.processing.init.ModBlocks;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public abstract class AbstractFactoryMachineBlock extends AbstractMachineBlock implements IWrenchable
{
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");
	public static final BooleanProperty BACK = BooleanProperty.create("back");
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	public static final BooleanProperty DOWN = BooleanProperty.create("down");
	public static final Map<Face, BooleanProperty> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Face.class), (directions) ->
	{
		directions.put(Face.RIGHT, RIGHT);
		directions.put(Face.BACK, BACK);
		directions.put(Face.LEFT, LEFT);
		directions.put(Face.DOWN, DOWN);
	});

	public AbstractFactoryMachineBlock(MachineTier tier, Properties properties)
	{
		super(tier, properties);
		this.setDefaultState(this.getDefaultState()
				.with(RIGHT, false)
				.with(BACK, false)
				.with(LEFT, false)
				.with(DOWN, false));
	}

	private static boolean createConnection(IBlockReader worldIn, BlockPos pos, Face face, Direction facing)
	{
		Direction side = Face.getDirectionFromFace(face, facing);
		assert side != null;
		TileEntity tileEntity = worldIn.getTileEntity(pos.offset(side));
		if (tileEntity != null)
		{
			IItemHandler item = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite()).orElse(tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null));
			return item != null;
		}
		return false;
	}

	private static <T> T getAdjacentValue(Iterable<T> p_195959_0_, @Nullable T p_195959_1_)
	{
		return Util.getElementAfter(p_195959_0_, p_195959_1_);
	}

	private static <T extends Comparable<T>> BlockState cycleProperty(BlockState state, Property<T> propertyIn)
	{
		T value = getAdjacentValue(propertyIn.getAllowedValues(), state.get(propertyIn));
		return state.with(propertyIn, value);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack)
	{
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos.up(), ModBlocks.DUMMY.get().getDefaultState());
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.onBlockHarvested(worldIn, pos, state, player);
		worldIn.destroyBlock(pos.up(), false);
	}

	@Override
	public ActionResultType onWrench(ItemUseContext context)
	{
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		BlockState state = world.getBlockState(pos);

		Direction side = context.getFace();
		Face face = Face.getFaceFromDirection(side, state);

		Processing.LOGGER.debug("onWrench: {}", side);
		if (face != Face.FRONT && side != Direction.UP)
		{
			TileEntity other = world.getTileEntity(pos.offset(side));
			if (!(other instanceof FluidPipeTile))
			{
				BlockState state1 = cycleProperty(state, FACING_TO_PROPERTY_MAP.get(face));
				world.setBlockState(pos, state1, 18);
				world.notifyNeighborsOfStateChange(pos, state1.getBlock());
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	public BlockState makeConnections(IBlockReader worldIn, BlockPos pos, Direction facing)
	{
		return this.getDefaultState()
				.with(DOWN, createConnection(worldIn, pos, Face.DOWN, facing))
				.with(RIGHT, createConnection(worldIn, pos, Face.RIGHT, facing))
				.with(BACK, createConnection(worldIn, pos, Face.BACK, facing))
				.with(LEFT, createConnection(worldIn, pos, Face.LEFT, facing));
	}

	@Override
	protected void fillStateContainer(@Nonnull StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(RIGHT, BACK, LEFT, DOWN);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{

		return this.makeConnections(context.getWorld(), context.getPos(), context.getPlacementHorizontalFacing().getOpposite()).with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
}
