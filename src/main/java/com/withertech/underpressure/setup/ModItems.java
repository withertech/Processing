package com.withertech.underpressure.setup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

public class ModItems
{
    public static final RegistryObject<Item> ORICHALCUM_INGOT = Registration.ITEMS.register("orichalcum_ingot", () ->
        new Item(new Item.Properties().group(ItemGroup.MATERIALS)));

    public static void register() {}
}
