package com.withertech.processing.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import com.withertech.processing.blocks.crusher.ElectricCrusherScreen;
import com.withertech.processing.crafting.recipe.RecipeCrushing;
import com.withertech.processing.init.ModBlocks;
import com.withertech.processing.util.Constants;
import com.withertech.processing.util.TextRenderUtils;
import com.withertech.processing.util.TextUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrushingRecipeCategoryJei implements IRecipeCategory<RecipeCrushing>
{
	private static final int GUI_START_X = 25;
	private static final int GUI_START_Y = 34;
	private static final int GUI_WIDTH = 151 - GUI_START_X;
	private static final int GUI_HEIGHT = 60 - GUI_START_Y;

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated arrow;
	private final String localizedName;

	public CrushingRecipeCategoryJei(IGuiHelper guiHelper)
	{
		background = guiHelper.createDrawable(ElectricCrusherScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.CRUSHER.get()));
		arrow = guiHelper.drawableBuilder(ElectricCrusherScreen.TEXTURE, 176, 14, 24, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
		localizedName = TextUtil.translate("jei", "category.crushing").getString();
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return Constants.CRUSHING;
	}

	@Nonnull
	@Override
	public Class<? extends RecipeCrushing> getRecipeClass()
	{
		return RecipeCrushing.class;
	}

	@Nonnull
	@Override
	public String getTitle()
	{
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setIngredients(RecipeCrushing recipe, IIngredients ingredients)
	{
		ingredients.setInputIngredients(Collections.singletonList(recipe.getIngredient()));
		ingredients.setOutputs(VanillaTypes.ITEM, new ArrayList<>(recipe.getPossibleResults(new Inventory(5))));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeCrushing recipe, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(0, true, 0, 0);
		itemStacks.init(1, false, 54, 0);
		itemStacks.init(2, false, 72, 0);
		itemStacks.init(3, false, 90, 0);
		itemStacks.init(4, false, 108, 0);

		// Should only be one ingredient...
//        recipe.getIngredients().forEach(ing -> itemStacks.set(0, Arrays.asList(ing.getMatchingStacks())));
		itemStacks.set(0, Arrays.asList(recipe.getIngredient().getMatchingStacks()));
		// Outputs
		List<Pair<ItemStack, Float>> results = recipe.getPossibleResultsWithChances();
		for (int i = 0; i < results.size(); ++i)
		{
			itemStacks.set(i + 1, results.get(i).getFirst());
		}
	}

	@Override
	public void draw(RecipeCrushing recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY)
	{
		arrow.draw(matrixStack, 49 - GUI_START_X, 35 - GUI_START_Y);

		FontRenderer font = Minecraft.getInstance().fontRenderer;

		List<Pair<ItemStack, Float>> results = recipe.getPossibleResultsWithChances();
		for (int i = 0; i < results.size(); ++i)
		{
			float chance = results.get(i).getSecond();
			if (chance < 1)
			{
				int asPercent = (int) (100 * chance);
				String text = asPercent < 1 ? "<1%" : asPercent + "%";
				TextRenderUtils.renderScaled(matrixStack, font, new StringTextComponent(text).func_241878_f(), 57 + 18 * i, 20, 0.75f, 0xFFFFFF, true);
			}
		}
	}
}
