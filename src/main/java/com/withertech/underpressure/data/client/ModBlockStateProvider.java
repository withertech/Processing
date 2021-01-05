package com.withertech.underpressure.data.client;

import com.withertech.underpressure.UnderPressure;
import com.withertech.underpressure.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelBuilder.Perspective;

import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider
{
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, UnderPressure.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        simpleBlock(ModBlocks.ORICHALCUM_ORE.get());
        simpleBlock(ModBlocks.ORICHALCUM_BLOCK.get());
        simpleBlock(ModBlocks.WAVE_CELL.get(), models().getExistingFile(modLoc("wave_cell")));
        horizontalBlock(ModBlocks.WAVE_CAP.get(), models().getExistingFile(modLoc("wave_cap")));

    }


}
