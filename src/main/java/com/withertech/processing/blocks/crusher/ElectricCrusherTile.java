package com.withertech.processing.blocks.crusher;

import com.withertech.processing.blocks.AbstractMachineTileEntity;
import com.withertech.processing.crafting.recipe.RecipeCrushing;
import com.withertech.processing.init.MachineType;
import com.withertech.processing.init.ModRecipes;
import com.withertech.processing.util.MachineTier;
import com.withertech.processing.util.TextUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.model.animation.ModelBlockAnimation;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.IntStream;

public class ElectricCrusherTile extends AbstractMachineTileEntity<RecipeCrushing>
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
	private final TimeValues.VariableValue cycleLength = new TimeValues.VariableValue(4);
	private IAnimationStateMachine asm;
	private ModelBlockAnimation arm;
	private final ModelDataMap.Builder builder = new ModelDataMap.Builder();
	private final ModelDataMap modelData = builder.withProperty(Properties.AnimationProperty).build();

	public ElectricCrusherTile()
	{
		this(MachineTier.STANDARD);
	}

	public ElectricCrusherTile(MachineTier tier)
	{
		super(MachineType.CRUSHER.getTileEntityType(tier), INVENTORY_SIZE, tier);
	}

	@Override
	public void onLoad()
	{
//		asm = UnderPressure.loadASM(UnderPressure.getId("asms/block/crusher.json"), ImmutableMap.of("cycle_length", cycleLength));
//		arm = UnderPressure.loadARM(UnderPressure.getId("armatures/block/crusher.json"));
//		arm.getClips().keySet().forEach(s -> UnderPressure.LOGGER.debug("arm clip: " + s));
//		asm.keySet().forEach(s -> UnderPressure.LOGGER.debug("arm clip: " + s));
//		requestModelDataUpdate();
//		UnderPressure.LOGGER.debug(asm.currentState());
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
	public void tick()
	{
		super.tick();
//		if (this.isRunning() && asm.currentState() == "off")
//		{
//			asm.transition("on");
//			requestModelDataUpdate();
//		}
//		else if(!this.isRunning() && asm.currentState() == "on")
//		{
//			asm.transition("off");
//			requestModelDataUpdate();
//		}
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
	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	@Override
	public int[] getSlotsForFace(@Nonnull Direction side)
	{
		return SLOTS_ALL;
	}

	@Override
	public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nullable Direction direction)
	{
		return index < INPUT_SLOT_COUNT;
	}

	@Override
	public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction)
	{
		return index >= INPUT_SLOT_COUNT;
	}

	@Nonnull
	@Override
	protected ITextComponent getDefaultName()
	{
		return TextUtil.translate("container", "crusher");
	}

	@Nonnull
	@Override
	protected Container createMenu(int id, @Nonnull PlayerInventory playerInventory)
	{
		return new ElectricCrusherContainer(id, playerInventory, this, this.fields);
	}

	@Nonnull
	@Override
	public IModelData getModelData()
	{
		return modelData;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if (!this.removed && cap == CapabilityAnimation.ANIMATION_CAPABILITY)
		{
			return CapabilityAnimation.ANIMATION_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> asm));
		}
		return super.getCapability(cap, side);
	}

	public static class Basic extends ElectricCrusherTile
	{
		public Basic()
		{
			super(MachineTier.BASIC);
		}
	}
}
