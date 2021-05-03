package com.withertech.processing.init;

import com.withertech.processing.Processing;
import com.withertech.processing.items.CraftingToolItem;
import com.withertech.processing.items.ItemWrench;
import com.withertech.processing.items.MachineUpgrades;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class ModItems
{
	public static final RegistryObject<ItemWrench> WRENCH = Registration.ITEMS.register("wrench", () ->
			new ItemWrench(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP).maxStackSize(1)));
	public static final RegistryObject<Item> HAMMER = Registration.ITEMS.register("hammer", () -> new CraftingToolItem(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP).maxStackSize(1).defaultMaxDamage(500)));
	public static final RegistryObject<Item> FILE = Registration.ITEMS.register("file", () -> new CraftingToolItem(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP).maxStackSize(1).defaultMaxDamage(500)));

	public static final RegistryObject<Item> PLATE_PRESS = Registration.ITEMS.register("plate_press", () -> new Item(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP).maxStackSize(1)));
	public static final RegistryObject<Item> ROD_PRESS = Registration.ITEMS.register("rod_press", () -> new Item(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP).maxStackSize(1)));
	public static final RegistryObject<Item> GEAR_PRESS = Registration.ITEMS.register("gear_press", () -> new Item(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP).maxStackSize(1)));

	static
	{
		ModMetals.registerItems();
		ModGems.registerItems();
		MachineUpgrades.register();
	}

	public static void register()
	{
	}
}
