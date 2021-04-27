package com.withertech.processing.blocks.crusher;

import com.withertech.processing.blocks.AbstractMachineBlock;
import com.withertech.processing.init.MachineType;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class ElectricCrusherBlock extends AbstractMachineBlock
{
	public static final BooleanProperty STATIC = net.minecraftforge.common.property.Properties.StaticProperty;

	public ElectricCrusherBlock(MachineTier tier)
	{
		super(tier, Properties.create(Material.IRON).hardnessAndResistance(6, 20).sound(SoundType.METAL));
		this.setDefaultState(this.getDefaultState().with(STATIC, false));
	}


	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder.add(STATIC));
	}

	@Override
	protected void interactWith(World worldIn, @Nonnull BlockPos pos, PlayerEntity player)
	{
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof ElectricCrusherTile)
		{
			player.openContainer((INamedContainerProvider) tileentity);
//            player.addStat(Stats.INTERACT_WITH_BLAST_FURNACE);
		}
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn)
	{
		return MachineType.CRUSHER.getTileEntityType(tier).create();
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
