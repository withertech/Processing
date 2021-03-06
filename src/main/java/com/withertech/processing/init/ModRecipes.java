package com.withertech.processing.init;

import com.withertech.processing.crafting.recipe.RecipeCrushing;
import com.withertech.processing.crafting.recipe.RecipePressing;
import com.withertech.processing.util.Constants;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public final class ModRecipes
{
	//	public static final RegistryObject<IRecipeSerializer<?>> ALLOY_SMELTING = registerSerializer(Constants.ALLOY_SMELTING, AlloySmeltingRecipe.Serializer::new);
//	public static final RegistryObject<IRecipeSerializer<?>> COMPRESSING = registerSerializer(Constants.COMPRESSING, CompressingRecipe.Serializer::new);
	public static final RegistryObject<IRecipeSerializer<?>> CRUSHING = registerSerializer(Constants.CRUSHING, RecipeCrushing.Serializer::new);
	public static final RegistryObject<IRecipeSerializer<?>> PRESSING = registerSerializer(Constants.PRESSING, RecipePressing.Serializer::new);

	private ModRecipes()
	{
	}
//	public static final RegistryObject<IRecipeSerializer<?>> DRYING = registerSerializer(Constants.DRYING, DryingRecipe.Serializer::new);
//	public static final RegistryObject<IRecipeSerializer<?>> INFUSING = registerSerializer(Constants.INFUSING, InfusingRecipe.Serializer::new);
//	public static final RegistryObject<IRecipeSerializer<?>> MIXING = registerSerializer(Constants.MIXING, MixingRecipe.Serializer::new);
//	public static final RegistryObject<IRecipeSerializer<?>> REFINING = registerSerializer(Constants.REFINING, RefiningRecipe.Serializer::new);
//	public static final RegistryObject<IRecipeSerializer<?>> SOLIDIFYING = registerSerializer(Constants.SOLIDIFYING, SolidifyingRecipe.Serializer::new);

	static void register()
	{
	}

	private static RegistryObject<IRecipeSerializer<?>> registerSerializer(ResourceLocation name, Supplier<IRecipeSerializer<?>> serializer)
	{
		return Registration.RECIPE_SERIALIZERS.register(name.getPath(), serializer);
	}

	private static <T extends IRecipe<?>> IRecipeType<T> registerType(ResourceLocation name)
	{
		return Registry.register(Registry.RECIPE_TYPE, name, new IRecipeType<T>()
		{
			@Override
			public String toString()
			{
				return name.toString();
			}
		});
	}

	public static final class Types
	{
		//		public static final IRecipeType<AlloySmeltingRecipe> ALLOY_SMELTING = registerType(Constants.ALLOY_SMELTING);
//		public static final IRecipeType<CompressingRecipe> COMPRESSING = registerType(Constants.ALLOY_SMELTING);
		public static final IRecipeType<RecipeCrushing> CRUSHING = registerType(Constants.CRUSHING);
		public static final IRecipeType<RecipePressing> PRESSING = registerType(Constants.PRESSING);
//		public static final IRecipeType<DryingRecipe> DRYING = registerType(Constants.ALLOY_SMELTING);
//		public static final IRecipeType<InfusingRecipe> INFUSING = registerType(Constants.INFUSING);
//		public static final IRecipeType<MixingRecipe> MIXING = registerType(Constants.MIXING);
//		public static final IRecipeType<RefiningRecipe> REFINING = registerType(Constants.REFINING);
//		public static final IRecipeType<SolidifyingRecipe> SOLIDIFYING = registerType(Constants.SOLIDIFYING);
	}
}
