package com.withertech.processing.items;

import com.withertech.processing.blocks.AbstractFactoryMachineBlock;
import com.withertech.processing.util.MachineTier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class FactoryBlockItem extends BlockItem
{
	private final MachineTier tier;
	public FactoryBlockItem(AbstractFactoryMachineBlock blockIn, Properties builder)
	{
		super(blockIn, builder);
		this.tier = blockIn.getTier();
	}

	public MachineTier getTier()
	{
		return tier;
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack)
	{
		IFormattableTextComponent name = super.getDisplayName(stack).copyRaw();
		switch (tier)
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
}
