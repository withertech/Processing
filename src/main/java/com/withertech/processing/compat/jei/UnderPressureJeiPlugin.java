package com.withertech.processing.compat.jei;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.crusher.ElectricCrusherContainer;
import com.withertech.processing.blocks.crusher.ElectricCrusherScreen;
import com.withertech.processing.blocks.furnace.ElectricFurnaceContainer;
import com.withertech.processing.blocks.furnace.ElectricFurnaceScreen;
import com.withertech.processing.blocks.press.ElectricPressContainer;
import com.withertech.processing.blocks.press.ElectricPressScreen;
import com.withertech.processing.init.ModBlocks;
import com.withertech.processing.init.ModRecipes;
import com.withertech.processing.util.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class UnderPressureJeiPlugin implements IModPlugin
{
	private static final ResourceLocation PLUGIN_UID = Processing.getId("plugin/main");

	private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType)
	{
		assert Minecraft.getInstance().world != null;
		return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream()
				.filter(r -> r.getType() == recipeType)
				.collect(Collectors.toList());
	}

	@Nonnull
	@Override
	public ResourceLocation getPluginUid()
	{
		return PLUGIN_UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
//		registration.addRecipeCategories(new AlloySmeltingRecipeCategoryJei(guiHelper));
//		registration.addRecipeCategories(new CompressingRecipeCategoryJei(guiHelper));
		registration.addRecipeCategories(new CrushingRecipeCategoryJei(guiHelper));
		registration.addRecipeCategories(new PressingRecipeCategoryJei(guiHelper));
//		registration.addRecipeCategories(new DryingRecipeCategoryJei(guiHelper));
//		registration.addRecipeCategories(new InfusingRecipeCategory(guiHelper));
//		registration.addRecipeCategories(new MixingRecipeCategory(guiHelper));
//		registration.addRecipeCategories(new RefiningRecipeCategory(guiHelper));
//		registration.addRecipeCategories(new SolidifyingRecipeCategory(guiHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
//		registration.addRecipes(getRecipesOfType(ModRecipes.Types.ALLOY_SMELTING), Constants.ALLOY_SMELTING);
//		registration.addRecipes(getRecipesOfType(ModRecipes.Types.COMPRESSING), Constants.COMPRESSING);
		registration.addRecipes(getRecipesOfType(ModRecipes.Types.CRUSHING), Constants.CRUSHING);
		registration.addRecipes(getRecipesOfType(ModRecipes.Types.PRESSING), Constants.PRESSING);
//		registration.addRecipes(getRecipesOfType(ModRecipes.Types.DRYING), Constants.DRYING);
//		registration.addRecipes(getRecipesOfType(ModRecipes.Types.INFUSING), Constants.INFUSING);
//		registration.addRecipes(getRecipesOfType(ModRecipes.Types.MIXING), Constants.MIXING);
//		registration.addRecipes(getRecipesOfType(ModRecipes.Types.REFINING), Constants.REFINING);
//		registration.addRecipes(getRecipesOfType(ModRecipes.Types.SOLIDIFYING), Constants.SOLIDIFYING);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
//		registration.addRecipeClickArea(AlloySmelterScreen.class, 90, 32, 28, 23, Constants.ALLOY_SMELTING);
//		registration.addRecipeClickArea(CompressorScreen.class, 78, 32, 28, 23, Constants.COMPRESSING);
		registration.addRecipeClickArea(ElectricCrusherScreen.class, 45, 32, 28, 23, Constants.CRUSHING);
		registration.addRecipeClickArea(ElectricFurnaceScreen.class, 78, 32, 28, 23,
				VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeClickArea(ElectricPressScreen.class, 78, 32, 28, 23, Constants.PRESSING);
//		registration.addRecipeClickArea(InfuserScreen.class, 79, 31, 24, 23, Constants.INFUSING);
//		registration.addRecipeClickArea(MixerScreen.class, 92, 31, 24, 23, Constants.MIXING);
//		registration.addRecipeClickArea(RefineryScreen.class, 43, 31, 24, 23, Constants.REFINING);
//		registration.addRecipeClickArea(SolidifierScreen.class, 79, 31, 24, 23, Constants.SOLIDIFYING);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
//		registration.addRecipeTransferHandler(AlloySmelterContainer.class, Constants.ALLOY_SMELTING, 0, 4, 5, 36);
//		registration.addRecipeTransferHandler(CompressorContainer.class, Constants.COMPRESSING, 0, 1, 2, 36);
		registration.addRecipeTransferHandler(ElectricCrusherContainer.class, Constants.CRUSHING, 0, 1, 5, 36);
		registration.addRecipeTransferHandler(ElectricPressContainer.class, Constants.PRESSING, 0, 2, 3, 36);
		registration.addRecipeTransferHandler(ElectricFurnaceContainer.class, VanillaRecipeCategoryUid.FURNACE, 0, 1, 2, 36);
		registration.addRecipeTransferHandler(ElectricFurnaceContainer.class, VanillaRecipeCategoryUid.BLASTING, 0, 1, 2, 36);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
//		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_SMELTER), Constants.ALLOY_SMELTING);
//		registration.addRecipeCatalyst(new ItemStack(ModBlocks.COMPRESSOR), Constants.COMPRESSING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_CRUSHER.get()), Constants.CRUSHING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_CRUSHER.get()), Constants.CRUSHING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_CRUSHER.get()), Constants.CRUSHING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_CRUSHER.get()), Constants.CRUSHING);

		registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_PRESS.get()), Constants.PRESSING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_PRESS.get()), Constants.PRESSING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_PRESS.get()), Constants.PRESSING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_PRESS.get()), Constants.PRESSING);

		registration.addRecipeCatalyst(new ItemStack(ModBlocks.BASIC_FURNACE.get()), VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ADVANCED_FURNACE.get()), VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELITE_FURNACE.get()), VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ULTIMATE_FURNACE.get()), VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
//		registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSER), Constants.INFUSING);
//		registration.addRecipeCatalyst(new ItemStack(ModBlocks.MIXER), Constants.MIXING);
//		registration.addRecipeCatalyst(new ItemStack(ModBlocks.REFINERY), Constants.REFINING);
//		registration.addRecipeCatalyst(new ItemStack(ModBlocks.SOLIDIFIER), Constants.SOLIDIFYING);
	}

//	@Override
//	public void registerItemSubtypes(ISubtypeRegistration registration) {
//		registration.registerSubtypeInterpreter(ModItems.CANISTER.get(), CanisterItem::getFluidKey);
//	}
}
