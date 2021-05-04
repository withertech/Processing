package com.withertech.processing.blocks;

import com.withertech.processing.api.IWrenchable;
import com.withertech.processing.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class DummyBlock extends Block implements IWrenchable
{

	public DummyBlock()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(6, 20).sound(SoundType.METAL).notSolid());
	}

	public boolean propagatesSkylightDown(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos)
	{
		return true;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	public BlockRenderType getRenderType(@Nonnull BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}

	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(@Nonnull BlockState state, @Nonnull IBlockReader worldIn, @Nonnull BlockPos pos)
	{
		return 1.0F;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, IBlockReader worldIn, BlockPos pos, @Nonnull ISelectionContext context)
	{
		return worldIn.getBlockState(pos.down()).getBlock().getShape(worldIn.getBlockState(pos.down()), worldIn, pos.down(), context).withOffset(0, -1, 0);
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos.down());
		if (tileEntity instanceof AbstractMachineTileEntity && !player.getHeldItemMainhand().isItemEqual(ModItems.WRENCH.get().getDefaultInstance()))
		{
			player.openContainer((INamedContainerProvider) tileEntity);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockHarvested(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player)
	{
		super.onBlockHarvested(worldIn, pos, state, player);
		worldIn.destroyBlock(pos.down(), !player.isCreative());
	}

	@Nonnull
	@Override
	public Item asItem()
	{
		return Items.AIR;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, @Nonnull BlockState state)
	{
		return new ItemStack(worldIn.getBlockState(pos.up()).getBlock());
	}

	@Override
	public ActionResultType onWrench(ItemUseContext context)
	{
		PlayerEntity player = context.getPlayer();
		if (player == null) return ActionResultType.PASS;

		World world = context.getWorld();
		BlockPos pos = context.getPos().down();
		BlockState state = world.getBlockState(pos);
		if (player.isCrouching() && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && state.getBlock() instanceof AbstractFactoryMachineBlock)
		{
			BlockState state1 = state.rotate(world, pos, Rotation.CLOCKWISE_90);
			world.setBlockState(pos, state1, 18);
			return ActionResultType.SUCCESS;
		}
		return null;
	}
}
