package com.withertech.processing.blocks;

import com.withertech.processing.api.Face;
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
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
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

public class DummyBlock extends Block implements IWrenchable
{

	public DummyBlock()
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(6, 20).sound(SoundType.METAL).notSolid());
	}

	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
	{
		return true;
	}

	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.INVISIBLE;
	}

	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
	{
		return 1.0F;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return worldIn.getBlockState(pos.down()).getBlock().getShape(worldIn.getBlockState(pos.down()), worldIn, pos.down(), context).withOffset(0, -1, 0);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos.down());
		if (tileEntity instanceof AbstractMachineTileEntity && !player.getHeldItemMainhand().isItemEqual(ModItems.WRENCH.get().getDefaultInstance()))
		{
			player.openContainer((INamedContainerProvider) tileEntity);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		super.onBlockHarvested(worldIn, pos, state, player);
		worldIn.destroyBlock(pos.down(), !player.isCreative());
	}

	@Override
	public Item asItem()
	{
		return Items.AIR;
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
			Direction side = context.getFace();
			Face face = Face.getFaceFromDirection(side, state);
//			if (face == Face.UP)
//			{
			BlockState state1 = state.rotate(world, pos, Rotation.CLOCKWISE_90);
			world.setBlockState(pos, state1, 18);
			return ActionResultType.SUCCESS;
//			}
		}
		return null;
	}
}
