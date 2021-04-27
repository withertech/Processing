package com.withertech.processing.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMetal extends Block
{
	public BlockMetal()
	{
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(4, 20)
				.sound(SoundType.METAL)
		);
	}
}
