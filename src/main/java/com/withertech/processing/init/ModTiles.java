package com.withertech.processing.init;

import com.withertech.processing.blocks.cell.TERFluidTank;
import com.withertech.processing.blocks.cell.TileFluidTank;
import com.withertech.processing.blocks.crusher.ElectricCrusherTile;
import com.withertech.processing.blocks.furnace.ElectricFurnaceTile;
import com.withertech.processing.blocks.pipe.TERFluidPipe;
import com.withertech.processing.blocks.pipe.TileFluidPipe;
import com.withertech.processing.blocks.press.ElectricPressTile;
import com.withertech.processing.client.renderer.tile.crusher.ElectricCrusherTileRenderer;
import com.withertech.processing.client.renderer.tile.furnace.ElectricFurnaceTileRenderer;
import com.withertech.processing.client.renderer.tile.press.ElectricPressTileRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModTiles
{
	public static final RegistryObject<TileEntityType<TileFluidPipe>> FLUID_PIPE = Registration.TILES.register("fluid_pipe", () -> TileEntityType.Builder.create(TileFluidPipe::new, ModBlocks.FLUID_PIPE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileFluidTank>> FLUID_TANK = Registration.TILES.register("fluid_tank", () -> TileEntityType.Builder.create(TileFluidTank::new, ModBlocks.FLUID_TANK.get()).build(null));
	public static final RegistryObject<TileEntityType<ElectricCrusherTile.Basic>> BASIC_CRUSHER = Registration.TILES.register("basic_crusher", MachineType.CRUSHER::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricCrusherTile.Advanced>> ADVANCED_CRUSHER = Registration.TILES.register("advanced_crusher", MachineType.CRUSHER::getAdvancedTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricCrusherTile.Elite>> ELITE_CRUSHER = Registration.TILES.register("elite_crusher", MachineType.CRUSHER::getEliteTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricCrusherTile.Ultimate>> ULTIMATE_CRUSHER = Registration.TILES.register("ultimate_crusher", MachineType.CRUSHER::getUltimateTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricPressTile.Basic>> BASIC_PRESS = Registration.TILES.register("basic_press", MachineType.PRESS::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricPressTile.Advanced>> ADVANCED_PRESS = Registration.TILES.register("advanced_press", MachineType.PRESS::getAdvancedTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricPressTile.Elite>> ELITE_PRESS = Registration.TILES.register("elite_press", MachineType.PRESS::getEliteTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricPressTile.Ultimate>> ULTIMATE_PRESS = Registration.TILES.register("ultimate_press", MachineType.PRESS::getUltimateTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricFurnaceTile.Basic>> BASIC_FURNACE = Registration.TILES.register("basic_furnace", MachineType.FURNACE::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricFurnaceTile.Advanced>> ADVANCED_FURNACE = Registration.TILES.register("advanced_furnace", MachineType.FURNACE::getAdvancedTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricFurnaceTile.Elite>> ELITE_FURNACE = Registration.TILES.register("elite_furnace", MachineType.FURNACE::getEliteTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricFurnaceTile.Ultimate>> ULTIMATE_FURNACE = Registration.TILES.register("ultimate_furnace", MachineType.FURNACE::getUltimateTileEntityType);


	public static void register()
	{
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderers(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(FLUID_TANK.get(), TERFluidTank::new);
		ClientRegistry.bindTileEntityRenderer(FLUID_PIPE.get(), TERFluidPipe::new);
		ClientRegistry.bindTileEntityRenderer(BASIC_CRUSHER.get(), ElectricCrusherTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ADVANCED_CRUSHER.get(), ElectricCrusherTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ELITE_CRUSHER.get(), ElectricCrusherTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ULTIMATE_CRUSHER.get(), ElectricCrusherTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BASIC_PRESS.get(), ElectricPressTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ADVANCED_PRESS.get(), ElectricPressTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ELITE_PRESS.get(), ElectricPressTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ULTIMATE_PRESS.get(), ElectricPressTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(BASIC_FURNACE.get(), ElectricFurnaceTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ADVANCED_FURNACE.get(), ElectricFurnaceTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ELITE_FURNACE.get(), ElectricFurnaceTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ULTIMATE_FURNACE.get(), ElectricFurnaceTileRenderer::new);
	}
}
