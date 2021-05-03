package com.withertech.processing.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.withertech.processing.Processing;
import com.withertech.processing.init.ModRecipes;
import com.withertech.processing.util.NameUtils;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public final class PressingRecipeBuilder
{
	private final List<ItemStack> results = new LinkedList<>();
	private final Ingredient ingredient;
	private final Ingredient press;
	private final int processTime;

	private PressingRecipeBuilder(Ingredient ingredient, Ingredient press, int processTime)
	{
		this.ingredient = ingredient;
		this.press = press;
		this.processTime = processTime;
	}

	public static PressingRecipeBuilder builder(IItemProvider ingredient, IItemProvider press, int processTime)
	{
		return builder(Ingredient.fromItems(ingredient), Ingredient.fromItems(press), processTime);
	}

	public static PressingRecipeBuilder builder(ITag<Item> ingredient, ITag<Item> press, int processTime)
	{
		return builder(Ingredient.fromTag(ingredient), Ingredient.fromTag(press), processTime);
	}

	public static PressingRecipeBuilder builder(Ingredient ingredient, Ingredient press, int processTime)
	{
		return new PressingRecipeBuilder(ingredient, press, processTime);
	}

	public static PressingRecipeBuilder pressing(ITag<Item> ingot, IItemProvider plate, ITag<Item> press, int processTime)
	{
		return builder(ingot, press, processTime)
				.result(plate, 1);
	}

	public PressingRecipeBuilder result(IItemProvider item, int count)
	{
		results.add(new ItemStack(item, count));
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumer)
	{
		ResourceLocation resultId = NameUtils.fromItem(results.iterator().next());
		ResourceLocation id = new ResourceLocation(
				"minecraft".equals(resultId.getNamespace()) ? Processing.MODID : resultId.getNamespace(),
				"pressing/" + resultId.getPath());
		build(consumer, id);
	}

	public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id)
	{
		consumer.accept(new Result(id, this));
	}

	public class Result implements IFinishedRecipe
	{
		private final ResourceLocation id;
		private final PressingRecipeBuilder builder;

		public Result(ResourceLocation id, PressingRecipeBuilder builder)
		{
			this.id = id;
			this.builder = builder;
		}

		@Override
		public void serialize(JsonObject json)
		{
			json.addProperty("process_time", builder.processTime);
			json.add("ingredient", builder.ingredient.serialize());
			json.add("press", builder.press.serialize());

			JsonArray results = new JsonArray();
			builder.results.forEach((stack) ->
					results.add(serializeResult(stack)));
			json.add("results", results);
		}

		private JsonObject serializeResult(ItemStack stack)
		{
			JsonObject json = new JsonObject();
			json.addProperty("item", NameUtils.fromItem(stack).toString());
			if (stack.getCount() > 1)
			{
				json.addProperty("count", stack.getCount());
			}
			return json;
		}

		@Nonnull
		@Override
		public ResourceLocation getID()
		{
			return id;
		}

		@Nonnull
		@Override
		public IRecipeSerializer<?> getSerializer()
		{
			return ModRecipes.PRESSING.get();
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson()
		{
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID()
		{
			return null;
		}
	}
}
