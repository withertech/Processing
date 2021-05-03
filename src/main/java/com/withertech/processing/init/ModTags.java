package com.withertech.processing.init;

import com.withertech.processing.Processing;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags
{
	private static ResourceLocation forgeId(String path)
	{
		return new ResourceLocation("forge", path);
	}

	private static ResourceLocation modId(String path)
	{
		return new ResourceLocation(Processing.MODID, path);
	}

	public static final class Blocks
	{

		private Blocks()
		{
		}

		private static ITag.INamedTag<Block> forge(String path)
		{
			return BlockTags.makeWrapperTag(forgeId(path).toString());
		}

		private static ITag.INamedTag<Block> mod(String path)
		{
			return BlockTags.makeWrapperTag(modId(path).toString());
		}
	}

	public static final class Items
	{
		public static final ITag.INamedTag<Item> PRESSES = mod("presses");
		public static final ITag.INamedTag<Item> PLATE_PRESS = mod("presses/plate");
		public static final ITag.INamedTag<Item> ROD_PRESS = mod("presses/rod");
		public static final ITag.INamedTag<Item> GEAR_PRESS = mod("presses/gear");


		public static final ITag.INamedTag<Item> PLATES = forge("plates");
		public static final ITag.INamedTag<Item> RODS = forge("rods");
		public static final ITag.INamedTag<Item> GEAR = forge("gear");

		private Items()
		{
		}

		private static ITag.INamedTag<Item> forge(String path)
		{
			return ItemTags.makeWrapperTag(forgeId(path).toString());
		}

		private static ITag.INamedTag<Item> mod(String path)
		{
			return ItemTags.makeWrapperTag(modId(path).toString());
		}
	}
}
