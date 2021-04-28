package com.withertech.processing.blocks.furnace;

import com.google.common.collect.ImmutableList;
import com.withertech.processing.api.RedstoneMode;
import com.withertech.processing.blocks.AbstractMachineTileEntity;
import com.withertech.processing.init.MachineType;
import com.withertech.processing.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ElectricFurnaceTile extends AbstractMachineTileEntity<AbstractCookingRecipe> implements IAnimatable, IRestorableTileEntity
{
	// Energy constant
	public static final int MAX_ENERGY = 50_000;
	public static final int MAX_RECEIVE = 500;
	public static final int ENERGY_USED_PER_TICK = 30;
	// Inventory constants
	private static final int INVENTORY_SIZE = 2;
	private static final int[] SLOTS_INPUT = {0};
	private static final int[] SLOTS_OUTPUT = {1};
	private static final int[] SLOTS_ALL = {0, 1};
//	@SyncVariable(name = "deployed")
	public boolean deployed = false;
	private final AnimationFactory factory = new AnimationFactory(this);
	private boolean wrenched = false;
	private int tickCount = 0;

	public ElectricFurnaceTile()
	{
		this(MachineTier.ADVANCED);
	}

	public ElectricFurnaceTile(MachineTier tier)
	{
		super(MachineType.FURNACE.getTileEntityType(tier), INVENTORY_SIZE, tier);
	}

	@Override
	protected int getEnergyUsedPerTick()
	{
		return ENERGY_USED_PER_TICK;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected int[] getOutputSlots()
	{
		return SLOTS_OUTPUT;
	}

	@Override
	@Nullable
	protected AbstractCookingRecipe getRecipe()
	{
		if (world == null) return null;

		RecipeManager recipeManager = world.getRecipeManager();
		Optional<BlastingRecipe> optional = recipeManager.getRecipe(IRecipeType.BLASTING, this, world);
		if (optional.isPresent()) return optional.get();

		Optional<FurnaceRecipe> optional1 = recipeManager.getRecipe(IRecipeType.SMELTING, this, world);
		return optional1.orElse(null);
	}

	@Override
	protected int getProcessTime(AbstractCookingRecipe recipe)
	{
		return recipe.getCookTime();
	}

	@Override
	protected Collection<ItemStack> getProcessResults(AbstractCookingRecipe recipe)
	{
		return Collections.singleton(recipe.getCraftingResult(this));
	}

	@Nonnull
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	public int[] getSlotsForFace(@Nonnull Direction side)
	{
		return SLOTS_ALL;
	}

	@Override
	public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nullable Direction direction)
	{
		return index == 0;
	}

	@Override
	public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction)
	{
		return index == 1;
	}

	@Nonnull
	@Override
	protected ITextComponent getDefaultName()
	{
		return TextUtil.translate("container", this.getMachineTier().name().toLowerCase(Locale.ROOT) + "_furnace");
	}

	@Nonnull
	@Override
	protected Container createMenu(int id, @Nonnull PlayerInventory playerInventory)
	{
		return new ElectricFurnaceContainer(id, playerInventory, this, this.fields);
	}

	List<String> getDebugText()
	{
		return ImmutableList.of(
				"progress = " + progress,
				"processTime = " + processTime,
				"energy = " + getEnergyStored() + " FE / " + getMaxEnergyStored() + " FE",
				"ENERGY_USED_PER_TICK = " + ENERGY_USED_PER_TICK,
				"MAX_RECEIVE = " + MAX_RECEIVE
		);
	}

	private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		AnimationController<?> controller = event.getController();
		controller.transitionLengthTicks = 0;

		if ((controller.getCurrentAnimation() != null && controller.getCurrentAnimation().animationName.equals("furnace.animation.deploy")) || controller.isJustStarting)
		{
			if (!this.deployed)
			{
				controller.setAnimation(new AnimationBuilder().addAnimation("furnace.animation.deploy", false).addAnimation("furnace.animation.idle", true));

				return PlayState.CONTINUE;
			}
		}

		if (event.getAnimatable().getBlockState().get(ElectricFurnaceBlock.LIT))
		{
			controller.setAnimation(new AnimationBuilder().addAnimation("furnace.animation.run", true));
		} else
		{
			controller.setAnimation(new AnimationBuilder().addAnimation("furnace.animation.idle", true));
		}
		return PlayState.CONTINUE;
	}

	@Override
	public void tick()
	{
		super.tick();
		if (tickCount < 80 && tickCount != -1)
		{
			tickCount++;
		} else
		{
			tickCount = -1;
		}
		if (tickCount == 80)
		{
			if (!this.deployed)
			{
				this.deployed = true;
				this.markDirty();
			}
		}


	}

	@Override
	public void read(@Nonnull BlockState state, @Nonnull CompoundNBT tags)
	{
		super.read(state, tags);
		SyncVariable.Helper.readSyncVars(this, tags);
		this.deployed = tags.getBoolean("deployed");
	}

	@Nonnull
	@Override
	public CompoundNBT write(@Nonnull CompoundNBT tags)
	{
		super.write(tags);
		SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
		tags.putBoolean("deployed", this.deployed);
		return tags;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}

	@Nonnull
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT tags = super.getUpdateTag();
		SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
		tags.putBoolean("deployed", this.deployed);
		return tags;
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet)
	{
		SyncVariable.Helper.readSyncVars(this, packet.getNbtCompound());
		this.deployed = packet.getNbtCompound().getBoolean("deployed");
	}

	@Override
	public boolean isWrenched()
	{
		return this.wrenched;
	}

	@Override
	public void setWrenched(boolean wrenched)
	{
		this.wrenched = wrenched;
	}

	@Override
	public void readRestorableFromNBT(CompoundNBT compound)
	{
		SyncVariable.Helper.readSyncVars(this, compound);
		readEnergy(compound);
		this.deployed = compound.getBoolean("deployed");
		this.redstoneMode = EnumUtils.byOrdinal(compound.getByte("RedstoneMode"), RedstoneMode.IGNORED);
		items = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, items);
	}

	@Override
	public void writeRestorableToNBT(CompoundNBT compound)
	{
		SyncVariable.Helper.writeSyncVars(this, compound, SyncVariable.Type.WRITE);
		writeEnergy(compound);
		compound.putBoolean("deployed", this.deployed);
		compound.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
		ItemStackHelper.saveAllItems(compound, items);
	}

	public static class Basic extends ElectricFurnaceTile
	{
		public Basic()
		{
			super(MachineTier.BASIC);
		}
	}

	public static class Advanced extends ElectricFurnaceTile
	{
		public Advanced()
		{
			super(MachineTier.ADVANCED);
		}
	}
	public static class Ultimate extends ElectricFurnaceTile
	{
		public Ultimate()
		{
			super(MachineTier.ULTIMATE);
		}
	}


}
