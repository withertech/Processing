package com.withertech.processing.blocks.cell;

import com.electronwill.nightconfig.core.utils.StringUtils;
import com.withertech.processing.util.IRestorableTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BlockFluidTank extends Block
{
	private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

	public BlockFluidTank()
	{
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(25, 1200)
				.harvestLevel(3)
				.sound(SoundType.METAL)
				.harvestTool(ToolType.PICKAXE)
				.notSolid());
	}


	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn)
	{
		CompoundNBT tagCompound = stack.getTag();
		if (tagCompound != null)
		{
			CompoundNBT nbt = tagCompound.getCompound("tank");
			FluidStack fluidStack = null;
			if (!nbt.contains("empty"))
			{
				fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
			}
			if (fluidStack == null)
			{
				addInformationLocalized(tooltip, "message.underpressure.tank", "empty");
			} else
			{
				String name = fluidStack.getDisplayName().getString();
				addInformationLocalized(tooltip, "message.underpressure.tank", name + " (" + fluidStack.getAmount() + ")");
			}
		}
	}


	protected void addInformationLocalized(List<ITextComponent> tooltip, String key, Object... parameters)
	{
		String translated = I18n.format(key, parameters);
		translated = COMPILE.matcher(translated).replaceAll("\u00a7");
		tooltip.addAll(StringUtils.split(translated, '\n').stream().map(ITextComponent::getTextComponentOrEmpty).collect(Collectors.toList()));
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
	@Nonnull
	@Override
	public ActionResultType onBlockActivated(@Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit)
	{
		if (worldIn.isRemote)
		{
			return ActionResultType.FAIL;
		}
		TileEntity tile = worldIn.getTileEntity(pos);

		if (tile instanceof TileFluidTank)
		{
			return ((TileFluidTank) tile).activate(player, handIn);
		}
		return ActionResultType.FAIL;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileFluidTank();
	}

}
