package com.withertech.processing.blocks;

import com.withertech.processing.init.ModItems;
import com.withertech.processing.util.IRestorableTileEntity;
import com.withertech.processing.util.MachineTier;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMachineBlock extends AbstractFurnaceBlock
{
	protected final MachineTier tier;

	public AbstractMachineBlock(MachineTier tier, Properties properties)
	{
		super(properties);
		this.tier = tier;
	}

	private static int calcRedstoneFromInventory(AbstractMachineBaseTileEntity inv)
	{
		// Copied from Container.calcRedstoneFromInventory
		int slotsFilled = 0;
		float fillRatio = 0.0F;

		for (int i = 0; i < inv.getSizeInventory() - inv.getMachineTier().getUpgradeSlots(); ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			if (!itemstack.isEmpty())
			{
				fillRatio += (float) itemstack.getCount() / Math.min(inv.getInventoryStackLimit(), itemstack.getMaxStackSize());
				++slotsFilled;
			}
		}

		fillRatio = fillRatio / (float) inv.getSizeInventory();
		return MathHelper.floor(fillRatio * 14.0F) + (slotsFilled > 0 ? 1 : 0);
	}

	public MachineTier getTier()
	{
		return this.tier;
	}

	@Override
	protected void interactWith(World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof INamedContainerProvider && !player.getHeldItemMainhand().isItemEqual(ModItems.WRENCH.get().getDefaultInstance()))
		{
			player.openContainer((INamedContainerProvider) tileEntity);
		}
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public List<ItemStack> getDrops(@Nonnull BlockState state, LootContext.Builder builder)
	{
		TileEntity tileEntity = builder.get(LootParameters.BLOCK_ENTITY);
		List<ItemStack> result = new ArrayList<>();
		if (tileEntity instanceof IRestorableTileEntity)
		{
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			CompoundNBT tagCompound = new CompoundNBT();
			((IRestorableTileEntity) tileEntity).writeRestorableToNBT(tagCompound);

			stack.setTag(tagCompound);
			result.add(stack);
		}
		return result;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IRestorableTileEntity)
		{
			CompoundNBT tagCompound = stack.getTag();
			if (tagCompound != null)
			{
				((IRestorableTileEntity) tileEntity).readRestorableFromNBT(tagCompound);
			}
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockClicked(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player)
	{
		super.onBlockClicked(state, worldIn, pos, player);
		if (player.isCrouching() && player.getHeldItemMainhand().isItemEqual(ModItems.WRENCH.get().getDefaultInstance()))
		{
			if (worldIn.getTileEntity(pos) instanceof IRestorableTileEntity)
			{
				((IRestorableTileEntity) Objects.requireNonNull(worldIn.getTileEntity(pos))).setWrenched(true);
			}
			worldIn.destroyBlock(pos, true);
		}
	}

	@Override
	public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof IInventory)
			{
				if (tileentity instanceof IRestorableTileEntity)
				{
					if (!((IRestorableTileEntity) tileentity).isWrenched())
					{
						InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
						worldIn.updateComparatorOutputLevel(pos, this);
					}
				} else
				{
					InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
					worldIn.updateComparatorOutputLevel(pos, this);
				}

			}


			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getComparatorInputOverride(@Nonnull BlockState blockState, World worldIn, @Nonnull BlockPos pos)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof AbstractMachineBaseTileEntity)
		{
			return calcRedstoneFromInventory((AbstractMachineBaseTileEntity) tileEntity);
		}
		return super.getComparatorInputOverride(blockState, worldIn, pos);
	}
}
