package com.withertech.processing.blocks.crusher;

import com.withertech.processing.api.RedstoneMode;
import com.withertech.processing.blocks.AbstractMachineTileEntity;
import com.withertech.processing.crafting.recipe.RecipeCrushing;
import com.withertech.processing.init.MachineType;
import com.withertech.processing.init.ModRecipes;
import com.withertech.processing.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.IntStream;

public class ElectricCrusherTile extends AbstractMachineTileEntity<RecipeCrushing> implements IAnimatable, IRestorableTileEntity
{
	// Energy constant
	private static final int MAX_ENERGY = 50_000;
	private static final int MAX_RECEIVE = 500;
	private static final int ENERGY_USED_PER_TICK = 30;
	// Inventory constants
	private static final int INPUT_SLOT_COUNT = 1;
	private static final int OUTPUT_SLOT_COUNT = 4;
	private static final int INVENTORY_SIZE = INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT;
	private static final int[] SLOTS_INPUT = {0};
	private static final int[] SLOTS_OUTPUT = IntStream.range(INPUT_SLOT_COUNT, INVENTORY_SIZE).toArray();
	private static final int[] SLOTS_ALL = IntStream.range(0, INVENTORY_SIZE).toArray();
	private final AnimationFactory factory = new AnimationFactory(this);
	public boolean deployed = false;
	private boolean wrenched = false;
	private int tickCount = 0;

	public ElectricCrusherTile(MachineTier tier)
	{
		super(MachineType.CRUSHER.getTileEntityType(tier), INVENTORY_SIZE, tier);
	}

	@Override
	protected int getEnergyUsedPerTick()
	{
		return ENERGY_USED_PER_TICK;
	}

	@Override
	protected int[] getInputSlots()
	{
		return SLOTS_INPUT;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	protected int[] getOutputSlots()
	{
		return SLOTS_OUTPUT;
	}

	private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event)
	{
		AnimationController<?> controller = event.getController();
		controller.transitionLengthTicks = 0;
		if ((controller.getCurrentAnimation() != null && controller.getCurrentAnimation().animationName.equals("crusher.animation.deploy")) || controller.isJustStarting)
		{
			if (!this.deployed)
			{
				controller.setAnimation(new AnimationBuilder().addAnimation("crusher.animation.deploy", false).addAnimation("crusher.animation.idle", true));

				return PlayState.CONTINUE;
			}
		}

		if (event.getAnimatable().getBlockState().get(ElectricCrusherBlock.LIT))
		{
			controller.setAnimation(new AnimationBuilder().addAnimation("crusher.animation.run", true));
		} else
		{
			controller.setAnimation(new AnimationBuilder().addAnimation("crusher.animation.idle", true));
		}
		return PlayState.CONTINUE;
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

	@Nullable
	@Override
	protected RecipeCrushing getRecipe()
	{
		if (world == null) return null;
		return world.getRecipeManager().getRecipe(ModRecipes.Types.CRUSHING, this, world).orElse(null);
	}

	@Override
	protected int getProcessTime(RecipeCrushing recipe)
	{
		return recipe.getProcessTime();
	}

	@Override
	public boolean isIngredient(ItemStack stack)
	{
		assert Minecraft.getInstance().world != null;
		return Minecraft.getInstance().world.getRecipeManager().getRecipesForType(ModRecipes.Types.CRUSHING).stream().anyMatch(recipeCrushing -> recipeCrushing.getIngredient().test(stack));
	}

	@Override
	protected Collection<ItemStack> getProcessResults(RecipeCrushing recipe)
	{
		return recipe.getResults(this);
	}

	@Override
	protected Collection<ItemStack> getPossibleProcessResult(RecipeCrushing recipe)
	{
		return recipe.getPossibleResults(this);
	}


	@Nonnull
	@Override
	protected ITextComponent getDefaultName()
	{
		IFormattableTextComponent name = TextUtil.translate("container", this.getMachineTier().getName() + "_crusher");
		switch (getMachineTier())
		{
			case BASIC:
				return name.mergeStyle(TextFormatting.DARK_GREEN);
			case ADVANCED:
				return name.mergeStyle(TextFormatting.DARK_RED);
			case ELITE:
				return name.mergeStyle(TextFormatting.DARK_AQUA);
			case ULTIMATE:
				return name.mergeStyle(TextFormatting.DARK_PURPLE);
			default:
				return name;
		}
	}

	@Nonnull
	@Override
	protected Container createMenu(int id, @Nonnull PlayerInventory playerInventory)
	{
		return new ElectricCrusherContainer(id, playerInventory, this, this.fields);
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
		getHandler().deserializeNBT(compound);
	}

	@Override
	public void writeRestorableToNBT(CompoundNBT compound)
	{
		SyncVariable.Helper.writeSyncVars(this, compound, SyncVariable.Type.WRITE);
		writeEnergy(compound);
		compound.putBoolean("deployed", this.deployed);
		compound.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
		compound.merge(getHandler().serializeNBT());
	}

	public static class Basic extends ElectricCrusherTile
	{
		public Basic()
		{
			super(MachineTier.BASIC);
		}
	}

	public static class Advanced extends ElectricCrusherTile
	{
		public Advanced()
		{
			super(MachineTier.ADVANCED);
		}
	}

	public static class Elite extends ElectricCrusherTile
	{
		public Elite()
		{
			super(MachineTier.ELITE);
		}
	}

	public static class Ultimate extends ElectricCrusherTile
	{
		public Ultimate()
		{
			super(MachineTier.ULTIMATE);
		}
	}
}
