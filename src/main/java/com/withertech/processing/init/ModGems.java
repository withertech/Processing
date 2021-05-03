package com.withertech.processing.init;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.BlockMetal;
import com.withertech.processing.registry.BlockRegistryObject;
import com.withertech.processing.registry.ItemRegistryObject;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

public enum ModGems
{
	COAL(builder("coal").dust().vanilla()),
	REDSTONE(builder("redstone").gem().vanilla()),
	DIAMOND(builder("diamond").dust().vanilla()),
	LAPIS(builder("lapis").dust().vanilla()),
	EMERALD(builder("emerald").dust().vanilla()),
	GLOWSTONE(builder("glowstone").gem().vanilla()),

	RUBY(builderBaseWithOre("ruby", ModOres.RUBY));

	private final String oreName;
	private final boolean vanilla;
	private final Supplier<Block> oreSupplier;
	private final Supplier<Block> storageBlockSupplier;
	private final Supplier<Item> dustSupplier;
	private final Supplier<Item> gemSupplier;
	private final ITag.INamedTag<Block> storageBlockTag;
	private final ITag.INamedTag<Block> oreTag;
	private final ITag.INamedTag<Item> storageBlockItemTag;
	private final ITag.INamedTag<Item> oreItemTag;
	private final ITag.INamedTag<Item> dustTag;
	private final ITag.INamedTag<Item> gemTag;
	@SuppressWarnings("NonFinalFieldInEnum")
	private BlockRegistryObject<Block> ore;
	@SuppressWarnings("NonFinalFieldInEnum")
	private BlockRegistryObject<Block> storageBlock;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> dust;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> gem;

	ModGems(Builder builder)
	{
		this(builder, builder.name);
	}

	ModGems(Builder builder, String oreName)
	{
		if (!builder.name.equals(this.getName()))
		{
			throw new IllegalArgumentException("Builder name is incorrect, should be " + this.getName());
		}
		this.oreName = oreName;
		this.vanilla = builder.vanilla;
		this.storageBlockSupplier = builder.storageBlock;
		this.oreSupplier = builder.ore;
		this.dustSupplier = builder.dust;
		this.gemSupplier = builder.gem;
		this.oreTag = builder.oreTag;
		this.storageBlockTag = builder.storageBlockTag;
		this.oreItemTag = this.oreTag != null ? Builder.itemTag(this.oreTag.getName()) : null;
		this.storageBlockItemTag = this.storageBlockTag != null ? Builder.itemTag(this.storageBlockTag.getName()) : null;
		this.dustTag = builder.dustTag;
		this.gemTag = builder.gemTag;
	}

	public static void registerBlocks()
	{
		for (ModGems metal : values())
		{
			if (metal.oreSupplier != null)
			{
				String name = metal.oreName + "_ore";
				metal.ore = new BlockRegistryObject<>(Registration.BLOCKS.register(name, metal.oreSupplier));
				Registration.ITEMS.register(name, () ->
						new BlockItem(metal.ore.get(), new Item.Properties().group(Processing.MACHINES_ITEM_GROUP)));
			}
		}
		for (ModGems metal : values())
		{
			if (metal.storageBlockSupplier != null)
			{
				String name = metal.getName() + "_block";
				metal.storageBlock = new BlockRegistryObject<>(Registration.BLOCKS.register(name, metal.storageBlockSupplier));
				Registration.ITEMS.register(name, () ->
						new BlockItem(metal.storageBlock.get(), new Item.Properties().group(Processing.MACHINES_ITEM_GROUP)));
			}
		}
	}

	public static void registerItems()
	{
		for (ModGems gem : values())
		{
			if (gem.dustSupplier != null)
			{
				gem.dust = new ItemRegistryObject<>(Registration.ITEMS.register(
						gem.getName() + "_dust", gem.dustSupplier));
			}
			if (gem.gemSupplier != null)
			{
				gem.gem = new ItemRegistryObject<>(Registration.ITEMS.register(
						gem.getName() + "_gem", gem.gemSupplier));
			}
		}
	}

	private static Builder builder(String name)
	{
		return new Builder(name);
	}

	private static Builder builderBaseWithOre(String name, ModOres ore)
	{
		return builder(name).storageBlock().ore(ore).dust().gem();
	}

//	private static Builder builderAlloy(String name)
//	{
//		return builder(name).storageBlock().dust().gem();
//	}

	public String getName()
	{
		return name().toLowerCase(Locale.ROOT);
	}

	public Optional<Block> getOre()
	{
		return ore != null ? Optional.of(ore.get()) : Optional.empty();
	}

	public Optional<Block> getStorageBlock()
	{
		return storageBlock != null ? Optional.of(storageBlock.get()) : Optional.empty();
	}

	public boolean isVanilla()
	{
		return this.vanilla;
	}

	public Optional<Item> getDust()
	{
		return dust != null ? Optional.of(dust.get()) : Optional.empty();
	}

	public Optional<Item> getGem()
	{
		return gem != null ? Optional.of(gem.get()) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Block>> getOreTag()
	{
		return oreTag != null ? Optional.of(oreTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Block>> getStorageBlockTag()
	{
		return storageBlockTag != null ? Optional.of(storageBlockTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getOreItemTag()
	{
		return oreItemTag != null ? Optional.of(oreItemTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getStorageBlockItemTag()
	{
		return storageBlockItemTag != null ? Optional.of(storageBlockItemTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getDustTag()
	{
		return dustTag != null ? Optional.of(dustTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getGemTag()
	{
		return gemTag != null ? Optional.of(gemTag) : Optional.empty();
	}

//	public Ingredient getSmeltables()
//	{
//		return getSmeltables(true);
//	}
//
//	public Ingredient getSmeltables(boolean includeIngot)
//	{
//		Stream.Builder<ITag.INamedTag<Item>> builder = Stream.builder();
//		if (includeIngot)
//		{
//			getIngotTag().ifPresent(builder::add);
//		}
//		getDustTag().ifPresent(builder::add);
//		return Ingredient.fromItemListStream(builder.build().map(Ingredient.TagList::new));
//	}

	private static class Builder
	{
		final String name;
		boolean vanilla = false;
		Supplier<Block> ore;
		Supplier<Block> storageBlock;
		Supplier<Item> dust;
		Supplier<Item> gem;
		ITag.INamedTag<Block> oreTag;
		ITag.INamedTag<Block> storageBlockTag;
		ITag.INamedTag<Item> dustTag;
		ITag.INamedTag<Item> gemTag;

		Builder(String name)
		{
			this.name = name;
		}

		private static ITag.INamedTag<Block> blockTag(String path)
		{
			return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
		}

		private static ITag.INamedTag<Item> itemTag(String path)
		{
			return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
		}

		private static ITag.INamedTag<Item> itemTag(ResourceLocation tag)
		{
			return ItemTags.makeWrapperTag(tag.toString());
		}

		Builder ore(ModOres ore)
		{
			this.ore = () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK)
					.setRequiresTool()
					.harvestTool(ToolType.PICKAXE)
					.harvestLevel(ore.getHarvestLevel())
					.hardnessAndResistance(ore.getHardness(), 3)
					.sound(SoundType.STONE));
			this.oreTag = blockTag("ores/" + name);
			return this;
		}

		Builder storageBlock()
		{
			this.storageBlock = BlockMetal::new;
			this.storageBlockTag = blockTag("storage_blocks/" + name);
			return this;
		}

		Builder vanilla()
		{
			this.vanilla = true;
			return this;
		}

		Builder dust()
		{
			this.dust = () -> new Item(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP));
			this.dustTag = itemTag("dusts/" + name);
			return this;
		}

		Builder dustTagOnly()
		{
			this.dustTag = itemTag("dusts/" + name);
			return this;
		}

		Builder gem()
		{
			this.gem = () -> new Item(new Item.Properties().group(Processing.MACHINES_ITEM_GROUP));
			this.gemTag = itemTag("gems/" + name);
			return this;
		}

		Builder gemTagOnly()
		{
			this.gemTag = itemTag("gems/" + name);
			return this;
		}
	}
}