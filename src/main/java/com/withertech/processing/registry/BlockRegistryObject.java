package com.withertech.processing.registry;

import com.withertech.processing.blocks.IBlockProvider;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

public class BlockRegistryObject<T extends Block> extends RegistryObjectWrapper<T> implements IBlockProvider
{
	public BlockRegistryObject(RegistryObject<T> block)
	{
		super(block);
	}

	@Override
	public Block asBlock()
	{
		return registryObject.get();
	}
}
