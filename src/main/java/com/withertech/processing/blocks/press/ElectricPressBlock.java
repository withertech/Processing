package com.withertech.processing.blocks.press;

import com.withertech.processing.blocks.AbstractFactoryMachineBlock;
import com.withertech.processing.init.MachineType;
import com.withertech.processing.init.ModItems;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class ElectricPressBlock extends AbstractFactoryMachineBlock
{
	private static final VoxelShape SHAPE = VoxelShapes.or(Block.makeCuboidShape(2, 0, 2, 14, 25, 14));

	public ElectricPressBlock(MachineTier tier)
	{
		super(tier, Properties.create(Material.IRON).hardnessAndResistance(6, 20).sound(SoundType.METAL).notSolid());
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos, @Nonnull ISelectionContext context)
	{
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public VoxelShape getRayTraceShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, BlockPos pos, @Nonnull ISelectionContext context)
	{
		return SHAPE;
	}

	@Nonnull
	@SuppressWarnings("deprecation")
	@Override
	public BlockRenderType getRenderType(@Nonnull BlockState state)
	{
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	protected void interactWith(World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof ElectricPressTile && !player.getHeldItemMainhand().isItemEqual(ModItems.WRENCH.get().getDefaultInstance()))
		{
			player.openContainer((INamedContainerProvider) tileentity);
//            player.addStat(Stats.INTERACT_WITH_BLAST_FURNACE);
		}
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn)
	{
		return MachineType.PRESS.getTileEntityType(tier).create();
	}

	@Override
	public void animateTick(BlockState stateIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random rand)
	{
		// TODO: Unique sound and particles? Copied from BlastFurnaceBlock.
		if (stateIn.get(LIT))
		{
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = pos.getY();
			double d2 = (double) pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D)
			{
				worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.get(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : d4;
			double d6 = rand.nextDouble() * 9.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : d4;
			worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
}
