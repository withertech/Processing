package com.withertech.processing.blocks;

import com.withertech.processing.api.Face;
import com.withertech.processing.inventory.BigItemStackHandler;
import com.withertech.processing.util.MCMathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class LockableBigInventoryTileEntity extends LockableTileEntity
{

	private BigItemStackHandler handler;
	public LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler).cast();

	protected LockableBigInventoryTileEntity(TileEntityType<?> typeIn, int inventorySize, int stackLimit)
	{
		super(typeIn);
		this.handler = new BigItemStackHandler(inventorySize, stackLimit);
	}

	@Override
	public int getSizeInventory()
	{
		return handler.getContents().size();
	}

	public BigItemStackHandler getHandler()
	{
		return handler;
	}

	@Override
	public boolean isEmpty()
	{
		return handler.isEmpty();

	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return handler.getStackInSlot(index);
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(handler.getContents(), index, count);
	}

	@Nonnull
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(handler.getContents(), index);
	}

	@Override
	public void setInventorySlotContents(int index, @Nonnull ItemStack stack)
	{
		handler.setStackInSlot(index, stack);
	}

	@Override
	public boolean isUsableByPlayer(@Nonnull PlayerEntity player)
	{
		return world != null && world.getTileEntity(pos) == this && MCMathUtils.distanceSq(player, pos) <= 64;
	}

	@Override
	public void clear()
	{
		handler.getContents().clear();
	}

	@Override
	public void read(@Nonnull BlockState stateIn, @Nonnull CompoundNBT tags)
	{
		super.read(stateIn, tags);
		handler.deserializeNBT(tags);
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT tags)
	{
		super.write(tags);
		tags.merge(handler.serializeNBT());
		return tags;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT tags = getUpdateTag();
		tags.merge(handler.serializeNBT());
		return new SUpdateTileEntityPacket(pos, 1, tags);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		super.onDataPacket(net, packet);
		handler.deserializeNBT(packet.getNbtCompound());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if (!this.removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (this.getBlockState().get(AbstractFactoryMachineBlock.FACING_TO_PROPERTY_MAP.get(Face.getFaceFromDirection(side, this.getBlockState()))))
				return optional.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void remove()
	{
		super.remove();
		optional.invalidate();
	}
}
