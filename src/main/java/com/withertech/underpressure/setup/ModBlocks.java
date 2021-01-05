package com.withertech.underpressure.setup;

import com.withertech.underpressure.blocks.BlockWaveCap;
import com.withertech.underpressure.blocks.BlockWaveCell;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks
{
    public static final RegistryObject<Block> ORICHALCUM_ORE = register("orichalcum_ore", () ->
        new Block(AbstractBlock.Properties.create(Material.ROCK)
            .hardnessAndResistance(50, 1200)
            .harvestLevel(3)
            .sound(SoundType.STONE)
            .harvestTool(ToolType.PICKAXE)
        )
    );

    public static final RegistryObject<Block> ORICHALCUM_BLOCK = register("orichalcum_block", () ->
        new Block(AbstractBlock.Properties.create(Material.IRON)
            .hardnessAndResistance(50, 1200)
            .harvestLevel(3)
            .sound(SoundType.METAL)
            .harvestTool(ToolType.PICKAXE)
        )
    );

    public static final RegistryObject<BlockWaveCell> WAVE_CELL = register("wave_cell", () ->
        new BlockWaveCell(AbstractBlock.Properties.create(Material.IRON)
            .hardnessAndResistance(25, 1200)
            .harvestLevel(3)
            .sound(SoundType.METAL)
            .harvestTool(ToolType.PICKAXE)
        )
    );

    public static final RegistryObject<BlockWaveCap> WAVE_CAP = register("wave_cap", () ->
        new BlockWaveCap(Block.Properties.create(Material.IRON)
            .hardnessAndResistance(25, 1200)
            .harvestLevel(3)
            .sound(SoundType.METAL)
            .harvestTool(ToolType.PICKAXE)
        )
    );

    public static void register() {}

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block)
    {
        return Registration.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block)
    {
        RegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
        return ret;
    }
}
