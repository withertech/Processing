package com.withertech.processing.init;

import com.withertech.processing.blocks.MetalBlock;
import com.withertech.processing.items.ResourceBlockItem;
import com.withertech.processing.items.ResourceItem;
import com.withertech.processing.items.ResourceItemType;
import com.withertech.processing.registry.BlockRegistryObject;
import com.withertech.processing.registry.ItemRegistryObject;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum ModMetals implements IModResource
{
	REDSTONE_ALLOY(builderAlloy("redstone_alloy")),
	REFINED_IRON(builder("refined_iron").ingot()),
	IRON(builder("iron").dust().plate().gear().rod().ingotTagOnly().nuggetTagOnly()),
	GOLD(builder("gold").dust().plate().gear().rod().ingotTagOnly().nuggetTagOnly()),
	COPPER(builderBaseWithOre("copper", ModOres.COPPER)),
	TIN(builderBaseWithOre("tin", ModOres.TIN)),
	SILVER(builderBaseWithOre("silver", ModOres.SILVER)),
	LEAD(builderBaseWithOre("lead", ModOres.LEAD)),
	NICKEL(builderBaseWithOre("nickel", ModOres.NICKEL)),
	PLATINUM(builderBaseWithOre("platinum", ModOres.PLATINUM)),
	ZINC(builderBaseWithOre("zinc", ModOres.ZINC)),
	BISMUTH(builderBaseWithOre("bismuth", ModOres.BISMUTH)),
	ALUMINUM(builderBaseWithOre("aluminum", ModOres.BAUXITE), "bauxite"),
	URANIUM(builderBaseWithOre("uranium", ModOres.URANIUM)),
	BRONZE(builderAlloy("bronze")),
	BRASS(builderAlloy("brass")),
	INVAR(builderAlloy("invar")),
	ELECTRUM(builderAlloy("electrum")),
	STEEL(builderAlloy("steel")),
	BISMUTH_BRASS(builderAlloy("bismuth_brass")),
	ALUMINUM_STEEL(builderAlloy("aluminum_steel")),
	BISMUTH_STEEL(builderAlloy("bismuth_steel")),
	SIGNALUM(builderAlloy("signalum")),
	LUMIUM(builderAlloy("lumium")),
	ENDERIUM(builderAlloy("enderium")),
	;

	private final String oreName;
	private final Supplier<Block> oreSupplier;
	private final Supplier<Block> storageBlockSupplier;
	private final Supplier<Item> dustSupplier;
	private final Supplier<Item> ingotSupplier;
	private final Supplier<Item> nuggetSupplier;
	private final Supplier<Item> plateSupplier;
	private final Supplier<Item> gearSupplier;
	private final Supplier<Item> rodSupplier;
	private final ITag.INamedTag<Block> storageBlockTag;
	private final ITag.INamedTag<Block> oreTag;
	private final ITag.INamedTag<Item> storageBlockItemTag;
	private final ITag.INamedTag<Item> oreItemTag;
	private final ITag.INamedTag<Item> dustTag;
	private final ITag.INamedTag<Item> ingotTag;
	private final ITag.INamedTag<Item> nuggetTag;
	private final ITag.INamedTag<Item> plateTag;
	private final ITag.INamedTag<Item> gearTag;
	private final ITag.INamedTag<Item> rodTag;
	@SuppressWarnings("NonFinalFieldInEnum")
	private BlockRegistryObject<Block> ore;
	@SuppressWarnings("NonFinalFieldInEnum")
	private BlockRegistryObject<Block> storageBlock;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> dust;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> ingot;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> nugget;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> plate;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> gear;
	@SuppressWarnings("NonFinalFieldInEnum")
	private ItemRegistryObject<Item> rod;

	ModMetals(Builder builder)
	{
		this(builder, builder.name);
	}

	ModMetals(Builder builder, String oreName)
	{
		if (!builder.name.equals(this.getName()))
		{
			throw new IllegalArgumentException("Builder name is incorrect, should be " + this.getName());
		}
		this.oreName = oreName;
		this.storageBlockSupplier = builder.storageBlock;
		this.oreSupplier = builder.ore;
		this.dustSupplier = builder.dust;
		this.ingotSupplier = builder.ingot;
		this.nuggetSupplier = builder.nugget;
		this.plateSupplier = builder.plate;
		this.gearSupplier = builder.gear;
		this.rodSupplier = builder.rod;
		this.oreTag = builder.oreTag;
		this.storageBlockTag = builder.storageBlockTag;
		this.oreItemTag = this.oreTag != null ? Builder.itemTag(this.oreTag.getName()) : null;
		this.storageBlockItemTag = this.storageBlockTag != null ? Builder.itemTag(this.storageBlockTag.getName()) : null;
		this.dustTag = builder.dustTag;
		this.ingotTag = builder.ingotTag;
		this.nuggetTag = builder.nuggetTag;
		this.plateTag = builder.plateTag;
		this.gearTag = builder.gearTag;
		this.rodTag = builder.rodTag;
	}

	public static void registerBlocks()
	{
		for (ModMetals metal : values())
		{
			if (metal.oreSupplier != null)
			{
				String name = metal.oreName + "_ore";
				metal.ore = new BlockRegistryObject<>(Registration.BLOCKS.register(name, metal.oreSupplier));
				Registration.ITEMS.register(name, () ->
						new ResourceBlockItem(metal.ore.get(), new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.ORE));
			}
		}
		for (ModMetals metal : values())
		{
			if (metal.storageBlockSupplier != null)
			{
				String name = metal.getName() + "_block";
				metal.storageBlock = new BlockRegistryObject<>(Registration.BLOCKS.register(name, metal.storageBlockSupplier));
				Registration.ITEMS.register(name, () ->
						new ResourceBlockItem(metal.storageBlock.get(), new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.STORAGE));
			}
		}
	}

	public static void registerItems()
	{
		for (ModMetals metal : values())
		{
			if (metal.dustSupplier != null)
			{
				metal.dust = new ItemRegistryObject<>(Registration.ITEMS.register(
						metal.getName() + "_dust", metal.dustSupplier));
			}
			if (metal.ingotSupplier != null)
			{
				metal.ingot = new ItemRegistryObject<>(Registration.ITEMS.register(
						metal.getName() + "_ingot", metal.ingotSupplier));
			}
			if (metal.nuggetSupplier != null)
			{
				metal.nugget = new ItemRegistryObject<>(Registration.ITEMS.register(
						metal.getName() + "_nugget", metal.nuggetSupplier));
			}
			if (metal.plateSupplier != null)
			{
				metal.plate = new ItemRegistryObject<>(Registration.ITEMS.register(
						metal.getName() + "_plate", metal.plateSupplier));
			}
			if (metal.gearSupplier != null)
			{
				metal.gear = new ItemRegistryObject<>(Registration.ITEMS.register(
						metal.getName() + "_gear", metal.gearSupplier));
			}
			if (metal.rodSupplier != null)
			{
				metal.rod = new ItemRegistryObject<>(Registration.ITEMS.register(
						metal.getName() + "_rod", metal.rodSupplier));
			}
		}
	}

	private static Builder builder(String name)
	{
		return new Builder(name);
	}

	private static Builder builderBaseWithOre(String name, ModOres ore)
	{
		return builder(name).storageBlock().ore(ore).dust().ingot().nugget().plate().gear().rod();
	}

	private static Builder builderAlloy(String name)
	{
		return builder(name).storageBlock().dust().ingot().nugget().plate().gear().rod();
	}

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

	public Optional<Item> getDust()
	{
		return dust != null ? Optional.of(dust.get()) : Optional.empty();
	}

	public Optional<Item> getIngot()
	{
		return ingot != null ? Optional.of(ingot.get()) : Optional.empty();
	}

	public Optional<Item> getNugget()
	{
		return nugget != null ? Optional.of(nugget.get()) : Optional.empty();
	}

	public Optional<Item> getPlate()
	{
		return plate != null ? Optional.of(plate.get()) : Optional.empty();
	}

	public Optional<Item> getGear()
	{
		return gear != null ? Optional.of(gear.get()) : Optional.empty();
	}

	public Optional<Item> getRod()
	{
		return rod != null ? Optional.of(rod.get()) : Optional.empty();
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

	public Optional<ITag.INamedTag<Item>> getIngotTag()
	{
		return ingotTag != null ? Optional.of(ingotTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getNuggetTag()
	{
		return nuggetTag != null ? Optional.of(nuggetTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getPlateTag()
	{
		return plateTag != null ? Optional.of(plateTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getGearTag()
	{
		return gearTag != null ? Optional.of(gearTag) : Optional.empty();
	}

	public Optional<ITag.INamedTag<Item>> getRodTag()
	{
		return rodTag != null ? Optional.of(rodTag) : Optional.empty();
	}

	public Ingredient getSmeltables()
	{
		return getSmeltables(true);
	}

	public Ingredient getSmeltables(boolean includeIngot)
	{
		Stream.Builder<ITag.INamedTag<Item>> builder = Stream.builder();
		if (includeIngot)
		{
			getIngotTag().ifPresent(builder::add);
		}
		getDustTag().ifPresent(builder::add);
		return Ingredient.fromItemListStream(builder.build().map(Ingredient.TagList::new));
	}

	private static class Builder
	{
		final String name;
		Supplier<Block> ore;
		Supplier<Block> storageBlock;
		Supplier<Item> dust;
		Supplier<Item> ingot;
		Supplier<Item> nugget;
		Supplier<Item> plate;
		Supplier<Item> gear;
		Supplier<Item> rod;
		ITag.INamedTag<Block> oreTag;
		ITag.INamedTag<Block> storageBlockTag;
		ITag.INamedTag<Item> dustTag;
		ITag.INamedTag<Item> ingotTag;
		ITag.INamedTag<Item> nuggetTag;
		ITag.INamedTag<Item> plateTag;
		ITag.INamedTag<Item> gearTag;
		ITag.INamedTag<Item> rodTag;

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
			this.storageBlock = MetalBlock::new;
			this.storageBlockTag = blockTag("storage_blocks/" + name);
			return this;
		}


		Builder dust()
		{
			this.dust = () -> new ResourceItem(new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.DUST);
			this.dustTag = itemTag("dusts/" + name);
			return this;
		}

		Builder ingot()
		{
			this.ingot = () -> new ResourceItem(new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.INGOT);
			this.ingotTag = itemTag("ingots/" + name);
			return this;
		}

		Builder ingotTagOnly()
		{
			this.ingotTag = itemTag("ingots/" + name);
			return this;
		}

		Builder nugget()
		{
			this.nugget = () -> new ResourceItem(new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.NUGGET);
			this.nuggetTag = itemTag("nuggets/" + name);
			return this;
		}

		Builder nuggetTagOnly()
		{
			this.nuggetTag = itemTag("nuggets/" + name);
			return this;
		}

		Builder plate()
		{
			this.plate = () -> new ResourceItem(new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.PLATE);
			this.plateTag = itemTag("plates/" + name);
			return this;
		}

		Builder gear()
		{
			this.gear = () -> new ResourceItem(new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.GEAR);
			this.gearTag = itemTag("gear/" + name);
			return this;
		}

		Builder rod()
		{
			this.rod = () -> new ResourceItem(new Item.Properties().group(ModGroups.MATERIALS_ITEM_GROUP), ResourceItemType.ROD);
			this.rodTag = itemTag("rods/" + name);
			return this;
		}
	}
}