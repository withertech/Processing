package com.withertech.processing.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.withertech.processing.init.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecipePressing implements IRecipe<IInventory>
{
	private final ResourceLocation recipeId;
	private final List<ItemStack> results = new LinkedList<>();
	private int processTime;
	private Ingredient ingredient;
	private Ingredient press;

	public RecipePressing(ResourceLocation recipeId)
	{
		this.recipeId = recipeId;
	}

	/**
	 * Get the time (in ticks) required to crush one ingredient
	 *
	 * @return The process time in ticks
	 */
	public int getProcessTime()
	{
		return processTime;
	}

	/**
	 * Get the input ingredient for the recipe
	 *
	 * @return The input ingredient
	 */
	public Ingredient getIngredient()
	{
		return ingredient;
	}

	public Ingredient getPress()
	{
		return press;
	}

	/**
	 * Get results of crushing. Some results may have a limited chance of being produced, and this
	 * method takes that into account.
	 *
	 * @param inv The crusher
	 * @return Results of crushing
	 */
	public List<ItemStack> getResults(IInventory inv)
	{
		return results.stream()
				.map(ItemStack::copy)
				.collect(Collectors.toList());
	}

	public List<ItemStack> getPossibleResults()
	{
		return results.stream()
				.map(ItemStack::copy)
				.collect(Collectors.toList());
	}


	@Override
	public boolean matches(IInventory inv, @Nonnull World worldIn)
	{
		return this.ingredient.test(inv.getStackInSlot(0));
	}

	@Nonnull
	@Deprecated
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv)
	{
		// DO NOT USE
		return getRecipeOutput();
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return true;
	}

	@Nonnull
	@Deprecated
	@Override
	public ItemStack getRecipeOutput()
	{
		// DO NOT USE
		return !results.isEmpty() ? results.iterator().next() : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ResourceLocation getId()
	{
		return recipeId;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return ModRecipes.PRESSING.get();
	}

	@Nonnull
	@Override
	public IRecipeType<?> getType()
	{
		return ModRecipes.Types.PRESSING;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipePressing>
	{
		@Nonnull
		@Override
		public RecipePressing read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
		{
			RecipePressing recipe = new RecipePressing(recipeId);
			recipe.processTime = JSONUtils.getInt(json, "process_time", 400);
			recipe.ingredient = Ingredient.deserialize(json.get("ingredient"));
			recipe.press = Ingredient.deserialize(json.get("press"));
			JsonArray resultsArray = json.getAsJsonArray("results");
			for (JsonElement element : resultsArray)
			{
				JsonObject obj = element.getAsJsonObject();
				String itemId = JSONUtils.getString(obj, "item");
				Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryCreate(itemId));
				int count = JSONUtils.getInt(obj, "count", 1);
				ItemStack stack = new ItemStack(item, count);
				recipe.results.add(stack);
			}
			return recipe;
		}

		@Override
		public RecipePressing read(@Nonnull ResourceLocation recipeId, PacketBuffer buffer)
		{
			RecipePressing recipe = new RecipePressing(recipeId);
			recipe.processTime = buffer.readVarInt();
			recipe.ingredient = Ingredient.read(buffer);
			recipe.press = Ingredient.read(buffer);
			int resultCount = buffer.readByte();
			for (int i = 0; i < resultCount; ++i)
			{
				ResourceLocation itemId = buffer.readResourceLocation();
				int count = buffer.readVarInt();
				Item item = ForgeRegistries.ITEMS.getValue(itemId);
				recipe.results.add(new ItemStack(item, count));
			}
			return recipe;
		}

		@Override
		public void write(PacketBuffer buffer, RecipePressing recipe)
		{
			buffer.writeVarInt(recipe.processTime);
			recipe.ingredient.write(buffer);
			recipe.press.write(buffer);
			buffer.writeByte(recipe.results.size());
			recipe.results.forEach((stack) ->
			{
				buffer.writeResourceLocation(Objects.requireNonNull(stack.getItem().getRegistryName()));
				buffer.writeVarInt(stack.getCount());
			});
		}
	}
}
