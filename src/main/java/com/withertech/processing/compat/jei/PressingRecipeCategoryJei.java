package com.withertech.processing.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.withertech.processing.blocks.press.PressScreen;
import com.withertech.processing.crafting.recipe.RecipePressing;
import com.withertech.processing.init.ModFactoryBlocks;
import com.withertech.processing.util.Constants;
import com.withertech.processing.util.MachineTier;
import com.withertech.processing.util.TextUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

public class PressingRecipeCategoryJei implements IRecipeCategory<RecipePressing>
{
	private static final int GUI_START_X = 37;
	private static final int GUI_START_Y = 30;
	private static final int GUI_WIDTH = 137 - GUI_START_X;
	private static final int GUI_HEIGHT = 56 - GUI_START_Y;

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated arrow;
	private final String localizedName;

	public PressingRecipeCategoryJei(IGuiHelper guiHelper)
	{
		background = guiHelper.createDrawable(PressScreen.JEI_TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
		icon = guiHelper.createDrawableIngredient(new ItemStack(ModFactoryBlocks.PRESS.getItem(MachineTier.BASIC)));
		arrow = guiHelper.drawableBuilder(PressScreen.JEI_TEXTURE, 176, 14, 24, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
		localizedName = TextUtil.translate("jei", "category.pressing").getString();
	}

	@Nonnull
	@Override
	public ResourceLocation getUid()
	{
		return Constants.PRESSING;
	}

	@Nonnull
	@Override
	public Class<? extends RecipePressing> getRecipeClass()
	{
		return RecipePressing.class;
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
	public void setIngredients(RecipePressing recipe, IIngredients ingredients)
	{
		ingredients.setInputIngredients(Arrays.asList(recipe.getIngredient(), recipe.getPress()));
		ingredients.setOutputs(VanillaTypes.ITEM, new ArrayList<>(recipe.getPossibleResults()));
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipePressing recipe, @Nonnull IIngredients ingredients)
	{
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(0, true, 0, 4);
		itemStacks.init(1, true, 18, 4);
		itemStacks.init(2, false, 78, 4);

		// Should only be one ingredient...
//        recipe.getIngredients().forEach(ing -> itemStacks.set(0, Arrays.asList(ing.getMatchingStacks())));
		itemStacks.set(0, Arrays.asList(recipe.getIngredient().getMatchingStacks()));
		itemStacks.set(1, Arrays.asList(recipe.getPress().getMatchingStacks()));
		// Outputs
		itemStacks.set(2, recipe.getPossibleResults());

	}

	@Override
	public void draw(RecipePressing recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY)
	{
		arrow.draw(matrixStack, 79 - GUI_START_X, 35 - GUI_START_Y);
	}
}
