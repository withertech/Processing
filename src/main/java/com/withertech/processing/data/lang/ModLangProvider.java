package com.withertech.processing.data.lang;

import com.withertech.processing.init.*;
import com.withertech.processing.items.MachineUpgrades;
import com.withertech.processing.util.MachineTier;
import com.withertech.processing.util.TextUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

import java.awt.*;

@SuppressWarnings("deprecation")
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
				add(metal.getOre().get(), getEntryName(metal, null, "Ore"));
			if (metal.getStorageBlock().isPresent())
				add(metal.getStorageBlock().get(), getEntryName(metal, null, "Block"));
			if (metal.getIngot().isPresent())
				add(metal.getIngot().get(), getEntryName(metal, null, "Ingot"));
			if (metal.getDust().isPresent())
				add(metal.getDust().get(), getEntryName(metal, null, "Dust"));
			if (metal.getNugget().isPresent())
				add(metal.getNugget().get(), getEntryName(metal, null, "Nugget"));
			if (metal.getPlate().isPresent())
				add(metal.getPlate().get(), getEntryName(metal, null, "Plate"));
			if (metal.getGear().isPresent())
				add(metal.getGear().get(), getEntryName(metal, null, "Gear"));
			if (metal.getRod().isPresent())
				add(metal.getRod().get(), getEntryName(metal, null, "Rod"));
		}

		for (ModGems gem : ModGems.values())
		{
			if (gem.getOre().isPresent())
				add(gem.getOre().get(), getEntryName(null, gem, "Ore"));
			if (gem.getStorageBlock().isPresent())
				add(gem.getStorageBlock().get(), getEntryName(null, gem, "Block"));

			if (gem.getDust().isPresent())
				add(gem.getDust().get(), getEntryName(null, gem, "Dust"));

			if (gem.getGem().isPresent())
				if (gem.isVanilla())
					add(gem.getGem().get(), "Crystallized " + getEntryName(null, gem, ""));
				else
					add(gem.getGem().get(), getEntryName(null, gem, ""));
		}
		add("config.processing.title", "Processing");
		add("config.processing.general", "General");
		add("config.processing.general.showBetaWelcomeMessage", "Show Beta Welcome Message");
		add("config.processing.general.showBetaWelcomeMessage.desc", "Toggle beta welcome message on world join");
		add("config.processing.world", "World");
		add("config.processing.world.ores", "Ores");
		add("config.processing.world.ores.master", "Master Switch");
		add("config.processing.world.ores.master.desc", "Set to 'false' to completely disable ore generation from this mod, ignoring all other settings.");
		for (ModOres ore : ModOres.values())
		{
			add("config.processing.world.ores." + ore.getName(), WordUtils.capitalize(ore.getName()));
			add("config.processing.world.ores." + ore.getName() + ".enable", "Enable");
			add("config.processing.world.ores." + ore.getName() + ".enable.desc", "Enable ore generation");
			add("config.processing.world.ores." + ore.getName() + ".veinCount", "Vein Count");
			add("config.processing.world.ores." + ore.getName() + ".veinCount.desc", "Number of veins per chunk");
			add("config.processing.world.ores." + ore.getName() + ".veinSize", "Vein Size");
			add("config.processing.world.ores." + ore.getName() + ".veinSize.desc", "Size of veins");
			add("config.processing.world.ores." + ore.getName() + ".minHeight", "Min Height");
			add("config.processing.world.ores." + ore.getName() + ".minHeight.desc", "Minimum Y-coordinate (base height) of veins");
			add("config.processing.world.ores." + ore.getName() + ".maxHeight", "Max Height");
			add("config.processing.world.ores." + ore.getName() + ".maxHeight.desc", "Maximum Y-coordinate (highest level) of veins");
		}
		add("config.processing.machines", "Machines");
		add("config.processing.machines.tiers", "Tiers");
		for (MachineTier tier : MachineTier.values())
		{
			add("config.processing.machines.tiers." + tier.getName(), WordUtils.capitalize(tier.getName()));
			add("config.processing.machines.tiers." + tier.getName() + ".upgradeSlots", "Upgrade Slots");
			add("config.processing.machines.tiers." + tier.getName() + ".upgradeSlots.desc", "Number of upgrade slots for tier");
			add("config.processing.machines.tiers." + tier.getName() + ".energyCapacity", "Energy Capacity");
			add("config.processing.machines.tiers." + tier.getName() + ".energyCapacity.desc", "Max energy storage for tier");
			add("config.processing.machines.tiers." + tier.getName() + ".storageMultiplier", "Storage Multiplier");
			add("config.processing.machines.tiers." + tier.getName() + ".storageMultiplier.desc", "Input slot max stack size multiplier for tier");
			add("config.processing.machines.tiers." + tier.getName() + ".operationsPerTick", "Operations Per Cycle");
			add("config.processing.machines.tiers." + tier.getName() + ".operationsPerTick.desc", "Operations per cycle for tier");
			add("config.processing.machines.tiers." + tier.getName() + ".processingSpeed", "Processing Speed");
			add("config.processing.machines.tiers." + tier.getName() + ".processingSpeed.desc", "Base processing speed for tier");
		}

		for (MachineUpgrades upgrade : MachineUpgrades.values())
		{
			add(upgrade.asItem(), WordUtils.capitalize(upgrade.getName().replace("_", " ")));
			add(upgrade.asItem().getTranslationKey() + ".desc", upgrade.getDesc());
		}

		add("item.processing.machine_upgrade.energy", "%s%% energy used");

		add(ModBlocks.FLUID_PIPE.get(), "Fluid Pipe");
		add(ModBlocks.FLUID_TANK.get(), "Fluid Tank");

		add(ModBlocks.BASIC_CHASSIS.get(), "Basic Chassis");
		add(ModBlocks.ADVANCED_CHASSIS.get(), "Advanced Chassis");
		add(ModBlocks.ELITE_CHASSIS.get(), "Elite Chassis");
		add(ModBlocks.ULTIMATE_CHASSIS.get(), "Ultimate Chassis");

		add(ModBlocks.BASIC_CRUSHER.get(), "Basic Electric Crusher");
		add(ModBlocks.ADVANCED_CRUSHER.get(), "Advanced Electric Crusher");
		add(ModBlocks.ELITE_CRUSHER.get(), "Elite Electric Crusher");
		add(ModBlocks.ULTIMATE_CRUSHER.get(), "Ultimate Electric Crusher");

		add(ModBlocks.BASIC_PRESS.get(), "Basic Electric Press");
		add(ModBlocks.ADVANCED_PRESS.get(), "Advanced Electric Press");
		add(ModBlocks.ELITE_PRESS.get(), "Elite Electric Press");
		add(ModBlocks.ULTIMATE_PRESS.get(), "Ultimate Electric Press");

		add(ModBlocks.BASIC_FURNACE.get(), "Basic Electric Furnace");
		add(ModBlocks.ADVANCED_FURNACE.get(), "Advanced Electric Furnace");
		add(ModBlocks.ELITE_FURNACE.get(), "Elite Electric Furnace");
		add(ModBlocks.ULTIMATE_FURNACE.get(), "Ultimate Electric Furnace");

		add(ModItems.PLATE_PRESS.get(), "Plate Press");
		add(ModItems.ROD_PRESS.get(), "Rod Press");
		add(ModItems.GEAR_PRESS.get(), "Gear Press");

		add(ModItems.WRENCH.get(), "Wrench");
		add(ModItems.HAMMER.get(), "Hammer");
		add(ModItems.FILE.get(), "File");

		add("itemGroup.processing.machines", "Processing: Machines");
		add("itemGroup.processing.materials", "Processing: Materials");

		add("message.processing.tank", "@aFluid: %s");

		add("container.processing.basic_crusher", "Basic Electric Crusher");
		add("container.processing.advanced_crusher", "Advanced Electric Crusher");
		add("container.processing.elite_crusher", "Elite Electric Crusher");
		add("container.processing.ultimate_crusher", "Ultimate Electric Crusher");

		add("container.processing.basic_press", "Basic Electric Press");
		add("container.processing.advanced_press", "Advanced Electric Press");
		add("container.processing.elite_press", "Elite Electric Press");
		add("container.processing.ultimate_press", "Ultimate Electric Press");

		add("container.processing.basic_furnace", "Basic Electric Furnace");
		add("container.processing.advanced_furnace", "Advanced Electric Furnace");
		add("container.processing.elite_furnace", "Elite Electric Furnace");
		add("container.processing.ultimate_furnace", "Ultimate Electric Furnace");

		add("jei.processing.category.crushing", "Crushing");
		add("jei.processing.category.pressing", "Pressing");

		add("misc.processing.energy", "%s FE");
		add("misc.processing.energyPerTick", "%s FE/t");
		add("misc.processing.energyWithMax", "%s / %s FE");
		add("misc.processing.fluidWithMax", "%s: %s / %s mB");
		add("misc.processing.redstoneMode", "Redstone Mode: %s");
		add("misc.processing.timeInSeconds", "Time: %ss");
		add("misc.processing.upgradeSlot", "Upgrade Slot");
	}

	private String getEntryName(ModMetals metal, ModGems gem, String suffix)
	{
		String ret = "";
		if (metal != null)
			ret = metal.getName().replaceAll("_", " ");
		if (gem != null)
			ret = gem.getName().replaceAll("_", " ");
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
		if (suffix.length() == 0)
			ret = String.valueOf(charArray);
		else
			ret = String.valueOf(charArray) + " " + suffix;
		return ret;
	}
}
