package com.withertech.processing.items;

import com.withertech.processing.Processing;
import com.withertech.processing.api.IMachineUpgrade;
import com.withertech.processing.util.MathUtils;
import com.withertech.processing.util.TextUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemMachineUpgrade extends Item
{
	private final IMachineUpgrade upgrade;

	public ItemMachineUpgrade(IMachineUpgrade upgrade)
	{
		super(new Properties().group(Processing.ITEM_GROUP));
		this.upgrade = upgrade;
	}

	public IMachineUpgrade getUpgrade()
	{
		return upgrade;
	}

	@Override
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		// Upgrade description and value
		tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc", upgrade.getDisplayValue()));

		// Energy usage multiplier
		float energyCost = upgrade.getEnergyUsageMultiplier();
		if (!MathUtils.floatsEqual(energyCost, 0f))
		{
			String str = String.format("%d", (int) (100 * energyCost));
			if (energyCost > 0)
				str = "+" + str;
			tooltip.add(TextUtil.translate("item", "machine_upgrade.energy", str));
		}
	}
}
