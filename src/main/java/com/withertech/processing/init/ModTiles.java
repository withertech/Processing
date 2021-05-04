package com.withertech.processing.init;

import com.withertech.processing.blocks.cell.FluidTankTER;
import com.withertech.processing.blocks.cell.FluidTankTile;
import com.withertech.processing.blocks.crusher.CrusherTile;
import com.withertech.processing.blocks.furnace.FurnaceTile;
import com.withertech.processing.blocks.pipe.FluidPipeTER;
import com.withertech.processing.blocks.pipe.FluidPipeTile;
import com.withertech.processing.blocks.press.PressTile;
import com.withertech.processing.blocks.crusher.CrusherTER;
import com.withertech.processing.blocks.furnace.FurnaceTER;
import com.withertech.processing.blocks.press.PressTER;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModTiles
{
	public static final RegistryObject<TileEntityType<FluidPipeTile>> FLUID_PIPE = Registration.TILES.register("fluid_pipe", () -> TileEntityType.Builder.create(FluidPipeTile::new, ModBlocks.FLUID_PIPE.get()).build(null));
	public static final RegistryObject<TileEntityType<FluidTankTile>> FLUID_TANK = Registration.TILES.register("fluid_tank", () -> TileEntityType.Builder.create(FluidTankTile::new, ModBlocks.FLUID_TANK.get()).build(null));
	public static final RegistryObject<TileEntityType<CrusherTile.Basic>> BASIC_CRUSHER = Registration.TILES.register("basic_crusher", MachineType.CRUSHER::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<CrusherTile.Advanced>> ADVANCED_CRUSHER = Registration.TILES.register("advanced_crusher", MachineType.CRUSHER::getAdvancedTileEntityType);
	public static final RegistryObject<TileEntityType<CrusherTile.Elite>> ELITE_CRUSHER = Registration.TILES.register("elite_crusher", MachineType.CRUSHER::getEliteTileEntityType);
	public static final RegistryObject<TileEntityType<CrusherTile.Ultimate>> ULTIMATE_CRUSHER = Registration.TILES.register("ultimate_crusher", MachineType.CRUSHER::getUltimateTileEntityType);
	public static final RegistryObject<TileEntityType<PressTile.Basic>> BASIC_PRESS = Registration.TILES.register("basic_press", MachineType.PRESS::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<PressTile.Advanced>> ADVANCED_PRESS = Registration.TILES.register("advanced_press", MachineType.PRESS::getAdvancedTileEntityType);
	public static final RegistryObject<TileEntityType<PressTile.Elite>> ELITE_PRESS = Registration.TILES.register("elite_press", MachineType.PRESS::getEliteTileEntityType);
	public static final RegistryObject<TileEntityType<PressTile.Ultimate>> ULTIMATE_PRESS = Registration.TILES.register("ultimate_press", MachineType.PRESS::getUltimateTileEntityType);
	public static final RegistryObject<TileEntityType<FurnaceTile.Basic>> BASIC_FURNACE = Registration.TILES.register("basic_furnace", MachineType.FURNACE::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<FurnaceTile.Advanced>> ADVANCED_FURNACE = Registration.TILES.register("advanced_furnace", MachineType.FURNACE::getAdvancedTileEntityType);
	public static final RegistryObject<TileEntityType<FurnaceTile.Elite>> ELITE_FURNACE = Registration.TILES.register("elite_furnace", MachineType.FURNACE::getEliteTileEntityType);
	public static final RegistryObject<TileEntityType<FurnaceTile.Ultimate>> ULTIMATE_FURNACE = Registration.TILES.register("ultimate_furnace", MachineType.FURNACE::getUltimateTileEntityType);


	public static void register()
	{
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderers(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(FLUID_TANK.get(), FluidTankTER::new);
		ClientRegistry.bindTileEntityRenderer(FLUID_PIPE.get(), FluidPipeTER::new);
		ClientRegistry.bindTileEntityRenderer(BASIC_CRUSHER.get(), CrusherTER::new);
		ClientRegistry.bindTileEntityRenderer(ADVANCED_CRUSHER.get(), CrusherTER::new);
		ClientRegistry.bindTileEntityRenderer(ELITE_CRUSHER.get(), CrusherTER::new);
		ClientRegistry.bindTileEntityRenderer(ULTIMATE_CRUSHER.get(), CrusherTER::new);
		ClientRegistry.bindTileEntityRenderer(BASIC_PRESS.get(), PressTER::new);
		ClientRegistry.bindTileEntityRenderer(ADVANCED_PRESS.get(), PressTER::new);
		ClientRegistry.bindTileEntityRenderer(ELITE_PRESS.get(), PressTER::new);
		ClientRegistry.bindTileEntityRenderer(ULTIMATE_PRESS.get(), PressTER::new);
		ClientRegistry.bindTileEntityRenderer(BASIC_FURNACE.get(), FurnaceTER::new);
		ClientRegistry.bindTileEntityRenderer(ADVANCED_FURNACE.get(), FurnaceTER::new);
		ClientRegistry.bindTileEntityRenderer(ELITE_FURNACE.get(), FurnaceTER::new);
		ClientRegistry.bindTileEntityRenderer(ULTIMATE_FURNACE.get(), FurnaceTER::new);
	}
}
