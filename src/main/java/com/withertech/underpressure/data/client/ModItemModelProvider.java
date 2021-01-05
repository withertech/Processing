package com.withertech.underpressure.data.client;

import com.withertech.underpressure.UnderPressure;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider
{
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
    {
        super(generator, UnderPressure.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        withExistingParent("orichalcum_ore", modLoc("orichalcum_ore"));
        withExistingParent("orichalcum_block", modLoc("orichalcum_block"));
        withExistingParent("wave_cell", modLoc("wave_cell"));


        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        builder(itemGenerated, "orichalcum_ingot");
    }

    private void builder(ModelFile itemGenerated, String name)
    {
        getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
