package com.withertech.processing.init;

import com.withertech.processing.Processing;
import com.withertech.processing.items.ItemWrench;
import com.withertech.processing.items.MachineUpgrades;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModItems
{
	public static final RegistryObject<ItemWrench> WRENCH = Registration.ITEMS.register("wrench", () ->
			new ItemWrench(new Item.Properties().group(Processing.ITEM_GROUP).maxStackSize(1)));

	static
	{
		ModMetals.registerItems();
		MachineUpgrades.register();
	}

	public static void register()
	{
	}
}
