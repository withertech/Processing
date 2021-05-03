package com.withertech.processing.data.recipe;

import com.withertech.processing.Processing;
import com.withertech.processing.init.ModBlocks;
import com.withertech.processing.init.ModItems;
import com.withertech.processing.init.ModMetals;
import com.withertech.processing.init.ModTags;
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
	private static final int PRESSING_PLATE_TIME = 200;

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

	private static void registerPressingRecipes(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		for (ModMetals metal : ModMetals.values())
		{
			if (metal.getIngotTag().isPresent() && metal.getPlate().isPresent())
			{
				pressingPlate(metal.getIngotTag().get(), metal.getPlate().get())
						.build(consumer);
			}
			if (metal.getIngotTag().isPresent() && metal.getRod().isPresent())
			{
				pressingRod(metal.getIngotTag().get(), metal.getRod().get())
						.build(consumer);
			}
			if (metal.getIngotTag().isPresent() && metal.getGear().isPresent())
			{
				pressingGear(metal.getIngotTag().get(), metal.getGear().get())
						.build(consumer);
			}
		}
	}

	public static PressingRecipeBuilder pressingPlate(ITag<Item> ingot, IItemProvider plate)
	{
		return PressingRecipeBuilder.pressing(ingot, plate, ModTags.Items.PLATE_PRESS, PRESSING_PLATE_TIME);
	}

	public static PressingRecipeBuilder pressingRod(ITag<Item> ingot, IItemProvider rod)
	{
		return PressingRecipeBuilder.pressing(ingot, rod, ModTags.Items.ROD_PRESS, PRESSING_PLATE_TIME);
	}

	public static PressingRecipeBuilder pressingGear(ITag<Item> ingot, IItemProvider gear)
	{
		return PressingRecipeBuilder.pressing(ingot, gear, ModTags.Items.GEAR_PRESS, PRESSING_PLATE_TIME);
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
		registerPressingRecipes(consumer);
	}

	private void registerCrafting(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		registerMetalCrafting(consumer);
		registerBlockCrafting(consumer);
		registerItemCrafting(consumer);
		registerAlloyCrafting(consumer);
	}

	private void registerAlloyCrafting(@Nonnull Consumer<IFinishedRecipe> consumer)
	{
		ExtendedShapelessRecipeBuilder.vanillaBuilder(ModMetals.BRONZE.getDust().get(), 4)
				.addIngredient(ModMetals.COPPER.getDustTag().get(), 3)
				.addIngredient(ModMetals.TIN.getDustTag().get())
				.build(consumer, Processing.getId("alloys/bronze_dust"));

		ExtendedShapelessRecipeBuilder.vanillaBuilder(ModMetals.INVAR.getDust().get(), 3)
				.addIngredient(ModMetals.IRON.getDustTag().get(), 2)
				.addIngredient(ModMetals.NICKEL.getDustTag().get())
				.build(consumer, Processing.getId("alloys/invar_dust"));

		ExtendedShapelessRecipeBuilder.vanillaBuilder(ModMetals.ELECTRUM.getDust().get(), 2)
				.addIngredient(ModMetals.GOLD.getDustTag().get())
				.addIngredient(ModMetals.SILVER.getDustTag().get())
				.build(consumer, Processing.getId("alloys/electrum_dust"));
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

			if (metal.getPlate().isPresent() && metal.getIngotTag().isPresent())
			{
				ExtendedShapelessRecipeBuilder.vanillaBuilder(metal.getPlate().get())
						.addIngredient(metal.getIngotTag().get(), 2)
						.addIngredient(ModItems.HAMMER.get())
						.build(consumer, Processing.getId("metals/" + metal.getName() + "_plate"));
			}
			if (metal.getRod().isPresent() && metal.getIngotTag().isPresent())
			{
				ExtendedShapelessRecipeBuilder.vanillaBuilder(metal.getRod().get())
						.addIngredient(metal.getIngotTag().get(), 2)
						.addIngredient(ModItems.FILE.get())
						.build(consumer, Processing.getId("metals/" + metal.getName() + "_rod"));
			}
			if (metal.getGear().isPresent() && metal.getIngotTag().isPresent())
			{
				ExtendedShapedRecipeBuilder.vanillaBuilder(metal.getGear().get(), 2)
						.patternLine("$#%")
						.patternLine("# #")
						.patternLine(" # ")
						.key('#', metal.getIngotTag().get())
						.key('$', ModItems.HAMMER.get())
						.key('%', ModItems.FILE.get())
						.build(consumer, Processing.getId("metals/" + metal.getName() + "_gear"));
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
				.key('#', ModMetals.TIN.getPlateTag().get())
				.addCriterion("has_tin", hasItem(ModMetals.TIN.getPlateTag().get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.FLUID_TANK.get(), 1)
				.patternLine("#/#")
				.patternLine("///")
				.patternLine("#/#")
				.key('/', Tags.Items.GLASS)
				.key('#', ModMetals.TIN.getPlateTag().get())
				.addCriterion("has_tin", hasItem(ModMetals.TIN.getPlateTag().get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BASIC_CHASSIS.get(), 1)
				.patternLine("# #")
				.patternLine("   ")
				.patternLine("# #")
				.key('#', ModMetals.TIN.getPlateTag().get())
				.addCriterion("has_tin", hasItem(ModMetals.TIN.getPlateTag().get()))
				.build(consumer, Processing.getId("chassis/basic_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ADVANCED_CHASSIS.get(), 1)
				.patternLine("# #")
				.patternLine(" @ ")
				.patternLine("# #")
				.key('#', ModMetals.SILVER.getPlateTag().get())
				.key('@', ModBlocks.BASIC_CHASSIS.get())
				.addCriterion("has_silver", hasItem(ModMetals.SILVER.getPlateTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.BASIC_CHASSIS.get()))
				.build(consumer, Processing.getId("chassis/advanced_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ELITE_CHASSIS.get(), 1)
				.patternLine("# #")
				.patternLine(" @ ")
				.patternLine("# #")
				.key('#', ModMetals.INVAR.getPlateTag().get())
				.key('@', ModBlocks.ADVANCED_CHASSIS.get())
				.addCriterion("has_invar", hasItem(ModMetals.INVAR.getPlateTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ADVANCED_CHASSIS.get()))
				.build(consumer, Processing.getId("chassis/elite_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ULTIMATE_CHASSIS.get(), 1)
				.patternLine("# #")
				.patternLine(" @ ")
				.patternLine("# #")
				.key('#', ModMetals.ELECTRUM.getPlateTag().get())
				.key('@', ModBlocks.ELITE_CHASSIS.get())
				.addCriterion("has_invar", hasItem(ModMetals.ELECTRUM.getPlateTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ELITE_CHASSIS.get()))
				.build(consumer, Processing.getId("chassis/ultimate_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BASIC_FURNACE.get(), 1)
				.patternLine("#%#")
				.patternLine("$%$")
				.patternLine("#*#")
				.key('#', ModMetals.TIN.getPlateTag().get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.COAL)
				.key('*', ModMetals.COPPER.getIngotTag().get())
				.addCriterion("has_tin", hasItem(ModMetals.TIN.getPlateTag().get()))
				.addCriterion("has_copper", hasItem(ModMetals.COPPER.getIngotTag().get()))
				.build(consumer, Processing.getId("furnace/basic_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BASIC_FURNACE.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.BASIC_CHASSIS.get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.COAL)
				.key('*', ModMetals.COPPER.getIngotTag().get())
				.addCriterion("has_tin", hasItem(ModMetals.TIN.getPlateTag().get()))
				.addCriterion("has_copper", hasItem(ModMetals.COPPER.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.BASIC_CHASSIS.get()))
				.build(consumer, Processing.getId("furnace/basic_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ADVANCED_FURNACE.get(), 1)
				.patternLine("#%#")
				.patternLine("$@$")
				.patternLine("#*#")
				.key('@', ModBlocks.BASIC_FURNACE.get())
				.key('#', ModMetals.SILVER.getPlateTag().get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.COAL)
				.key('*', ModMetals.GOLD.getIngotTag().get())
				.addCriterion("has_silver", hasItem(ModMetals.SILVER.getPlateTag().get()))
				.addCriterion("has_gold", hasItem(ModMetals.GOLD.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.BASIC_FURNACE.get()))
				.build(consumer, Processing.getId("furnace/advanced_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ADVANCED_FURNACE.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.ADVANCED_CHASSIS.get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.COAL)
				.key('*', ModMetals.GOLD.getIngotTag().get())
				.addCriterion("has_silver", hasItem(ModMetals.SILVER.getPlateTag().get()))
				.addCriterion("has_gold", hasItem(ModMetals.GOLD.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ADVANCED_CHASSIS.get()))
				.build(consumer, Processing.getId("furnace/advanced_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ELITE_FURNACE.get(), 1)
				.patternLine("#%#")
				.patternLine("$@$")
				.patternLine("#*#")
				.key('@', ModBlocks.ADVANCED_FURNACE.get())
				.key('#', ModMetals.INVAR.getPlateTag().get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.STORAGE_BLOCKS_COAL)
				.key('*', ModMetals.ALUMINUM.getIngotTag().get())
				.addCriterion("has_invar", hasItem(ModMetals.INVAR.getPlateTag().get()))
				.addCriterion("has_aluminum", hasItem(ModMetals.ALUMINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ADVANCED_FURNACE.get()))
				.build(consumer, Processing.getId("furnace/elite_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ELITE_FURNACE.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.ELITE_CHASSIS.get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.STORAGE_BLOCKS_COAL)
				.key('*', ModMetals.ALUMINUM.getIngotTag().get())
				.addCriterion("has_invar", hasItem(ModMetals.INVAR.getPlateTag().get()))
				.addCriterion("has_aluminum", hasItem(ModMetals.ALUMINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ELITE_CHASSIS.get()))
				.build(consumer, Processing.getId("furnace/elite_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ULTIMATE_FURNACE.get(), 1)
				.patternLine("#%#")
				.patternLine("$@$")
				.patternLine("#*#")
				.key('@', ModBlocks.ELITE_FURNACE.get())
				.key('#', ModMetals.ELECTRUM.getPlateTag().get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.STORAGE_BLOCKS_COAL)
				.key('*', ModMetals.PLATINUM.getIngotTag().get())
				.addCriterion("has_electrum", hasItem(ModMetals.ELECTRUM.getPlateTag().get()))
				.addCriterion("has_platinum", hasItem(ModMetals.PLATINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ELITE_FURNACE.get()))
				.build(consumer, Processing.getId("furnace/ultimate_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ULTIMATE_FURNACE.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.ULTIMATE_CHASSIS.get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.STORAGE_BLOCKS_COAL)
				.key('*', ModMetals.PLATINUM.getIngotTag().get())
				.addCriterion("has_electrum", hasItem(ModMetals.ELECTRUM.getPlateTag().get()))
				.addCriterion("has_platinum", hasItem(ModMetals.PLATINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ULTIMATE_CHASSIS.get()))
				.build(consumer, Processing.getId("furnace/ultimate_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BASIC_CRUSHER.get(), 1)
				.patternLine("#%#")
				.patternLine("$%$")
				.patternLine("#*#")
				.key('#', ModMetals.TIN.getPlateTag().get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.FLINT)
				.key('*', ModMetals.COPPER.getIngotTag().get())
				.addCriterion("has_tin", hasItem(ModMetals.TIN.getPlateTag().get()))
				.addCriterion("has_copper", hasItem(ModMetals.COPPER.getIngotTag().get()))
				.build(consumer, Processing.getId("crusher/basic_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.BASIC_CRUSHER.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.BASIC_CHASSIS.get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.FLINT)
				.key('*', ModMetals.COPPER.getIngotTag().get())
				.addCriterion("has_tin", hasItem(ModMetals.TIN.getPlateTag().get()))
				.addCriterion("has_copper", hasItem(ModMetals.COPPER.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.BASIC_CHASSIS.get()))
				.build(consumer, Processing.getId("crusher/basic_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ADVANCED_CRUSHER.get(), 1)
				.patternLine("#%#")
				.patternLine("$@$")
				.patternLine("#*#")
				.key('@', ModBlocks.BASIC_CRUSHER.get())
				.key('#', ModMetals.SILVER.getPlateTag().get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.FLINT)
				.key('*', ModMetals.GOLD.getIngotTag().get())
				.addCriterion("has_silver", hasItem(ModMetals.SILVER.getPlateTag().get()))
				.addCriterion("has_gold", hasItem(ModMetals.GOLD.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.BASIC_CRUSHER.get()))
				.build(consumer, Processing.getId("crusher/advanced_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ADVANCED_CRUSHER.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.ADVANCED_CHASSIS.get())
				.key('%', Tags.Items.DUSTS_REDSTONE)
				.key('$', Items.FLINT)
				.key('*', ModMetals.GOLD.getIngotTag().get())
				.addCriterion("has_silver", hasItem(ModMetals.SILVER.getPlateTag().get()))
				.addCriterion("has_gold", hasItem(ModMetals.GOLD.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ADVANCED_CHASSIS.get()))
				.build(consumer, Processing.getId("crusher/advanced_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ELITE_CRUSHER.get(), 1)
				.patternLine("#%#")
				.patternLine("$@$")
				.patternLine("#*#")
				.key('@', ModBlocks.ADVANCED_CRUSHER.get())
				.key('#', ModMetals.INVAR.getPlateTag().get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.OBSIDIAN)
				.key('*', ModMetals.ALUMINUM.getIngotTag().get())
				.addCriterion("has_invar", hasItem(ModMetals.INVAR.getPlateTag().get()))
				.addCriterion("has_aluminum", hasItem(ModMetals.ALUMINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ADVANCED_CRUSHER.get()))
				.build(consumer, Processing.getId("crusher/elite_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ELITE_CRUSHER.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.ELITE_CHASSIS.get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.OBSIDIAN)
				.key('*', ModMetals.ALUMINUM.getIngotTag().get())
				.addCriterion("has_invar", hasItem(ModMetals.INVAR.getPlateTag().get()))
				.addCriterion("has_aluminum", hasItem(ModMetals.ALUMINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ELITE_CHASSIS.get()))
				.build(consumer, Processing.getId("crusher/elite_chassis"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ULTIMATE_CRUSHER.get(), 1)
				.patternLine("#%#")
				.patternLine("$@$")
				.patternLine("#*#")
				.key('@', ModBlocks.ELITE_CRUSHER.get())
				.key('#', ModMetals.ELECTRUM.getPlateTag().get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.OBSIDIAN)
				.key('*', ModMetals.PLATINUM.getIngotTag().get())
				.addCriterion("has_electrum", hasItem(ModMetals.ELECTRUM.getPlateTag().get()))
				.addCriterion("has_platinum", hasItem(ModMetals.PLATINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ELITE_CRUSHER.get()))
				.build(consumer, Processing.getId("crusher/ultimate_upgrade"));

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.ULTIMATE_CRUSHER.get(), 1)
				.patternLine(" % ")
				.patternLine("$@$")
				.patternLine(" * ")
				.key('@', ModBlocks.ULTIMATE_CHASSIS.get())
				.key('%', Tags.Items.STORAGE_BLOCKS_REDSTONE)
				.key('$', Tags.Items.OBSIDIAN)
				.key('*', ModMetals.PLATINUM.getIngotTag().get())
				.addCriterion("has_electrum", hasItem(ModMetals.ELECTRUM.getPlateTag().get()))
				.addCriterion("has_platinum", hasItem(ModMetals.PLATINUM.getIngotTag().get()))
				.addCriterion("has_parent", hasItem(ModBlocks.ULTIMATE_CHASSIS.get()))
				.build(consumer, Processing.getId("crusher/ultimate_chassis"));


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

		ShapedRecipeBuilder.shapedRecipe(ModItems.HAMMER.get(), 1)
				.patternLine("###")
				.patternLine("#/#")
				.patternLine(" / ")
				.key('/', ModMetals.COPPER.getIngotTag().get())
				.key('#', ModMetals.TIN.getIngotTag().get())
				.addCriterion("has_item", hasItem(ModMetals.TIN.getIngotTag().get()))
				.build(consumer);


		ShapedRecipeBuilder.shapedRecipe(ModItems.PLATE_PRESS.get(), 1)
				.patternLine(" # ")
				.patternLine("#$#")
				.patternLine(" # ")
				.key('#', ModMetals.TIN.getIngotTag().get())
				.key('$', ModTags.Items.PLATES)
				.addCriterion("has_item", hasItem(ModTags.Items.PLATES))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.ROD_PRESS.get(), 1)
				.patternLine(" # ")
				.patternLine("#$#")
				.patternLine(" # ")
				.key('#', ModMetals.TIN.getIngotTag().get())
				.key('$', ModTags.Items.RODS)
				.addCriterion("has_item", hasItem(ModTags.Items.RODS))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.GEAR_PRESS.get(), 1)
				.patternLine(" # ")
				.patternLine("#$#")
				.patternLine(" # ")
				.key('#', ModMetals.TIN.getIngotTag().get())
				.key('$', ModTags.Items.GEAR)
				.addCriterion("has_item", hasItem(ModTags.Items.GEAR))
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
