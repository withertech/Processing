package com.withertech.processing.data.lang;

import com.withertech.processing.init.ModBlocks;
import com.withertech.processing.init.ModItems;
import com.withertech.processing.init.ModMetals;
import com.withertech.processing.items.MachineUpgrades;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

public class ModLangProvider extends LanguageProvider
{
	public ModLangProvider(DataGenerator gen, String modid, String locale)
	{
		super(gen, modid, locale);
	}

	@Override
	public String getName()
	{
		return "Processing - Lang Files";
	}

	@Override
	protected void addTranslations()
	{
		for (ModMetals metal : ModMetals.values())
		{
			if (metal.getOre().isPresent())
				add(metal.getOre().get(), getEntryName(metal, "Ore"));
			if (metal.getStorageBlock().isPresent())
				add(metal.getStorageBlock().get(), getEntryName(metal, "Block"));
			if (metal.getIngot().isPresent())
				add(metal.getIngot().get(), getEntryName(metal, "Ingot"));
			if (metal.getDust().isPresent())
				add(metal.getDust().get(), getEntryName(metal, "Dust"));
			if (metal.getNugget().isPresent())
				add(metal.getNugget().get(), getEntryName(metal, "Nugget"));
		}

		for (MachineUpgrades upgrade : MachineUpgrades.values())
		{
			//noinspection deprecation
			add(upgrade.asItem(), WordUtils.capitalize(upgrade.getName().replace("_", " ")));
			add(upgrade.asItem().getTranslationKey() + ".desc", upgrade.getDesc());

		}

		add("item.processing.machine_upgrade.energy", "%s%% energy used");

		add(ModBlocks.FLUID_PIPE.get(), "Fluid Pipe");
		add(ModBlocks.FLUID_TANK.get(), "Fluid Tank");
		add(ModBlocks.CRUSHER.get(), "Electric Crusher");
		add(ModBlocks.FURNACE.get(), "Electric Furnace");

		add(ModItems.WRENCH.get(), "Wrench");

		add("itemGroup.processing", "Processing");

		add("message.processing.tank", "@aFluid: %s");

		add("container.processing.crusher", "Electric Crusher");
		add("container.processing.furnace", "Electric Furnace");

		add("jei.processing.category.crushing", "Crushing");

		add("misc.processing.energy", "%s FE");
		add("misc.processing.energyPerTick", "%s FE/t");
		add("misc.processing.energyWithMax", "%s / %s FE");
		add("misc.processing.fluidWithMax", "%s: %s / %s mB");
		add("misc.processing.redstoneMode", "Redstone Mode: %s");
		add("misc.processing.timeInSeconds", "Time: %ss");
		add("misc.processing.upgradeSlot", "Upgrade Slot");
	}

	private String getEntryName(ModMetals metal, String suffix)
	{
		String ret = metal.getName().replaceAll("_", " ");
		char[] charArray = ret.toCharArray();
		boolean foundSpace = true;

		for (int i = 0; i < charArray.length; i++)
		{

			// if the array element is a letter
			if (Character.isLetter(charArray[i]))
			{

				// check space is present before the letter
				if (foundSpace)
				{

					// change the letter into uppercase
					charArray[i] = Character.toUpperCase(charArray[i]);
					foundSpace = false;
				}
			} else
			{
				// if the new character is not character
				foundSpace = true;
			}
		}
		ret = String.valueOf(charArray) + " " + suffix;
		return ret;
	}
}
