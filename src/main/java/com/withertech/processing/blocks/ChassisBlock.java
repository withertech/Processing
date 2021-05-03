package com.withertech.processing.blocks;

import com.withertech.processing.util.MachineTier;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ChassisBlock extends Block
{
	private final MachineTier tier;

	public ChassisBlock(MachineTier tier)
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(6, 20).sound(SoundType.METAL));
		this.tier = tier;
	}

	public MachineTier getTier()
	{
		return this.tier;
	}

}
