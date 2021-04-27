package com.withertech.processing.data.recipe;

import com.withertech.processing.Processing;
import com.withertech.processing.init.ModBlocks;
import com.withertech.processing.init.ModItems;
import com.withertech.processing.init.ModMetals;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider
{
	private static final int CRUSHING_INGOT_TIME = 200;
	private static final int CRUSHING_ORE_TIME = 400;
	private static final float CRUSHING_ORE_STONE_CHANCE = 0.1f;
	public ModRecipeProvider(DataGenerator generatorIn)
	{
		super(generatorIn);
	}

	private static void registerCrushingRecipes(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		for (ModMetals metal : ModMetals.values())
		{
			if (metal.getOreItemTag().isPresent() && metal.getDust().isPresent())
			{
				crushingOre(metal.getOreItemTag().get(), metal.getDust().get(), Blocks.COBBLESTONE)
						.build(consumer);
			}
			if (metal.getIngotTag().isPresent() && metal.getDust().isPresent())
			{
				crushingIngot(metal.getIngotTag().get(), metal.getDust().get())
						.build(consumer, Processing.getId("crushing/" + metal.getName() + "_dust_from_ingot"));
			}
		}

		// Vanilla ores
		CrushingRecipeBuilder.builder(Tags.Items.ORES_COAL, CRUSHING_ORE_TIME)
				.result(Items.COAL, 2)
				.result(Items.COBBLESTONE, 1, CRUSHING_ORE_STONE_CHANCE)
				.result(Items.DIAMOND, 1, 0.001f)
				.build(consumer);
		CrushingRecipeBuilder.builder(Tags.Items.ORES_LAPIS, CRUSHING_ORE_TIME)
				.result(Items.LAPIS_LAZULI, 12)
				.build(consumer);
		CrushingRecipeBuilder.builder(Tags.Items.ORES_REDSTONE, CRUSHING_ORE_TIME)
				.result(Items.REDSTONE, 6)
				.build(consumer);
		crushingOreBonus(Tags.Items.ORES_QUARTZ, Items.QUARTZ).build(consumer);
		crushingOreBonus(Tags.Items.ORES_DIAMOND, Items.DIAMOND).build(consumer);
		crushingOreBonus(Tags.Items.ORES_EMERALD, Items.EMERALD).build(consumer);
		crushingOre(Tags.Items.ORES_GOLD, ModMetals.GOLD.getDust().get(), Blocks.COBBLESTONE).build(consumer);
		crushingOre(Blocks.NETHER_GOLD_ORE, ModMetals.GOLD.getDust().get(), Blocks.NETHERRACK)
				.build(consumer, Processing.getId("crushing/gold_chunks_nether"));
		crushingOre(Tags.Items.ORES_IRON, ModMetals.IRON.getDust().get(), Blocks.COBBLESTONE).build(consumer);

		CrushingRecipeBuilder.builder(Blocks.ANCIENT_DEBRIS, 2 * CRUSHING_ORE_TIME)
				.result(Items.NETHERITE_SCRAP, 2)
				.result(Items.NETHERITE_SCRAP, 1, 0.1f)
				.result(Items.NETHERITE_SCRAP, 1, 0.01f)
				.build(consumer);

		// Others
		CrushingRecipeBuilder.builder(Tags.Items.RODS_BLAZE, 200)
				.result(Items.BLAZE_POWDER, 6)
				.build(consumer);
		CrushingRecipeBuilder.builder(Blocks.CLAY, 100)
				.result(Items.CLAY_BALL, 4)
				.build(consumer);
		CrushingRecipeBuilder.builder(Blocks.GLOWSTONE, 100)
				.result(Items.GLOWSTONE_DUST, 4)
				.build(consumer);
		CrushingRecipeBuilder.builder(Tags.Items.COBBLESTONE, 200)
				.result(Blocks.GRAVEL, 1)
				.build(consumer);
		CrushingRecipeBuilder.builder(ItemTags.LOGS, 200)
				.result(Items.PAPER, 1, 0.75f)
				.result(Items.PAPER, 1, 0.25f)
				.result(Items.STICK, 1, 0.25f)
				.result(Items.STICK, 1, 0.25f)
				.build(consumer);
		CrushingRecipeBuilder.builder(
				Ingredient.fromItems(Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_PILLAR, Blocks.CHISELED_QUARTZ_BLOCK, Blocks.SMOOTH_QUARTZ),
				200)
				.result(Items.QUARTZ, 4)
				.build(consumer, Processing.getId("crushing/quartz_from_blocks"));
		CrushingRecipeBuilder.builder(Ingredient.fromItems(Blocks.RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE), 200)
				.result(Blocks.RED_SAND, 4)
				.build(consumer, Processing.getId("crushing/red_sand_from_sandstone"));
		CrushingRecipeBuilder.builder(Ingredient.fromItems(Blocks.SANDSTONE, Blocks.CHISELED_SANDSTONE), 200)
				.result(Blocks.SAND, 4)
				.build(consumer, Processing.getId("crushing/sand_from_sandstone"));
		CrushingRecipeBuilder.builder(Blocks.GRAVEL, 200)
				.result(Blocks.SAND, 1)
				.result(Items.FLINT, 1, 0.1f)
				.build(consumer);
	}

	public static CrushingRecipeBuilder crushingIngot(ITag<Item> ingot, IItemProvider dust)
	{
		return CrushingRecipeBuilder.crushingIngot(ingot, dust, CRUSHING_INGOT_TIME);
	}

	public static CrushingRecipeBuilder crushingOre(ITag<Item> ore, IItemProvider chunks, @Nullable IItemProvider extra)
	{
		return CrushingRecipeBuilder.crushingOre(ore, chunks, CRUSHING_ORE_TIME, extra, CRUSHING_ORE_STONE_CHANCE);
	}

	public static CrushingRecipeBuilder crushingOre(IItemProvider ore, IItemProvider chunks, @Nullable IItemProvider extra)
	{
		return CrushingRecipeBuilder.crushingOre(ore, chunks, CRUSHING_ORE_TIME, extra, CRUSHING_ORE_STONE_CHANCE);
	}

	public static CrushingRecipeBuilder crushingOreBonus(ITag<Item> ore, IItemProvider item)
	{
		return CrushingRecipeBuilder.builder(ore, CRUSHING_ORE_TIME)
				.result(item, 2)
				.result(item, 1, 0.1f)
				.result(Blocks.COBBLESTONE, 1, 0.1f);
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "Processing - Recipes";
	}

	@Override
	protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		registerCrafting(consumer);
		registerSmelting(consumer);
		registerCrushingRecipes(consumer);
	}

	private void registerCrafting(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		registerMetalCrafting(consumer);
		registerBlockCrafting(consumer);
		registerItemCrafting(consumer);
	}

	private void registerMetalCrafting(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		for (ModMetals metal : ModMetals.values())
		{
			if (metal.getIngot().isPresent() && metal.getNuggetTag().isPresent())
			{
				ExtendedShapedRecipeBuilder.vanillaBuilder(metal.getIngot().get())
						.patternLine("###")
						.patternLine("###")
						.patternLine("###")
						.key('#', metal.getNuggetTag().get())
						.build(consumer, Processing.getId("metals/" + metal.getName() + "_ingot_from_nugget"));
			}
			if (metal.getNugget().isPresent() && metal.getIngotTag().isPresent())
			{
				ExtendedShapelessRecipeBuilder.vanillaBuilder(metal.getNugget().get(), 9)
						.addIngredient(metal.getIngotTag().get())
						.build(consumer, Processing.getId("metals/" + metal.getName() + "_nugget"));
			}
			if (metal.getStorageBlock().isPresent() && metal.getIngotTag().isPresent())
			{
				ExtendedShapedRecipeBuilder.vanillaBuilder(metal.getStorageBlock().get())
						.patternLine("###")
						.patternLine("###")
						.patternLine("###")
						.key('#', metal.getIngotTag().get())
						.build(consumer, Processing.getId("metals/" + metal.getName() + "_block"));
			}
			if (metal.getIngot().isPresent() && metal.getStorageBlockItemTag().isPresent())
			{
				ExtendedShapelessRecipeBuilder.vanillaBuilder(metal.getIngot().get(), 9)
						.addIngredient(metal.getStorageBlockItemTag().get())
						.build(consumer, Processing.getId("metals/" + metal.getName() + "_ingot_from_block"));
			}
		}
	}

	private void registerBlockCrafting(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shapedRecipe(ModBlocks.FLUID_PIPE.get(), 12)
				.patternLine("###")
				.patternLine("///")
				.patternLine("###")
				.key('/', Tags.Items.GLASS)
				.key('#', ModMetals.TIN.getIngotTag().get())
				.addCriterion("has_item", hasItem(ModMetals.TIN.getIngotTag().get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.FLUID_TANK.get(), 1)
				.patternLine("#/#")
				.patternLine("///")
				.patternLine("#/#")
				.key('/', Tags.Items.GLASS)
				.key('#', ModMetals.TIN.getIngotTag().get())
				.addCriterion("has_item", hasItem(ModMetals.TIN.getIngotTag().get()))
				.build(consumer);


	}

	private void registerItemCrafting(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shapedRecipe(ModItems.WRENCH.get(), 1)
				.patternLine("# #")
				.patternLine(" / ")
				.patternLine(" # ")
				.key('/', ModMetals.COPPER.getIngotTag().get())
				.key('#', ModMetals.TIN.getIngotTag().get())
				.addCriterion("has_item", hasItem(ModMetals.TIN.getIngotTag().get()))
				.build(consumer);
	}

	private void registerSmelting(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		for (ModMetals metal : ModMetals.values())
		{
			if (metal.getIngot().isPresent() && metal.getOreItemTag().isPresent())
			{
				smeltingAndBlasting(consumer, metal.getName() + "_ingot_from_ore",
						Ingredient.fromTag(metal.getOreItemTag().get()), metal.getIngot().get());
			}
			if (metal.getIngot().isPresent() && metal.getDustTag().isPresent())
			{
				smeltingAndBlasting(consumer, metal.getName() + "_ingot_from_dust",
						Ingredient.fromTag(metal.getDustTag().get()), metal.getIngot().get());
			}
		}

		smeltingAndBlasting(consumer, "iron_ingot_from_dust", ModMetals.IRON.getSmeltables(false), Items.IRON_INGOT);
		smeltingAndBlasting(consumer, "gold_ingot_from_dust", ModMetals.GOLD.getSmeltables(false), Items.GOLD_INGOT);

		assert (ModMetals.REFINED_IRON.getIngot().isPresent());
		smeltingAndBlasting(consumer, "refined_iron_ingot", Ingredient.fromTag(Tags.Items.INGOTS_IRON), ModMetals.REFINED_IRON.getIngot().get());
	}

	private void smeltingAndBlasting(@Nonnull Consumer<IFinishedRecipe> consumer, String name, Ingredient ingredient, IItemProvider result)
	{
		CookingRecipeBuilder.smeltingRecipe(ingredient, result, 1f, 200)
				.addCriterion("has_item", hasItem(Blocks.FURNACE))
				.build(consumer, Processing.getId("smelting/" + name));
		CookingRecipeBuilder.blastingRecipe(ingredient, result, 1f, 100)
				.addCriterion("has_item", hasItem(Blocks.FURNACE))
				.build(consumer, Processing.getId("blasting/" + name));
	}
}
