package com.withertech.processing.init;

import com.mojang.datafixers.types.Type;
import com.withertech.processing.blocks.cell.TERFluidTank;
import com.withertech.processing.blocks.cell.TileFluidTank;
import com.withertech.processing.blocks.crusher.ElectricCrusherTile;
import com.withertech.processing.blocks.furnace.ElectricFurnaceTile;
import com.withertech.processing.blocks.pipe.TERFluidPipe;
import com.withertech.processing.blocks.pipe.TileFluidPipe;
import com.withertech.processing.client.renderer.tile.furnace.ElectricFurnaceTileRenderer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.animation.TileEntityRendererAnimation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModTiles
{
	public static final RegistryObject<TileEntityType<TileFluidPipe>> FLUID_PIPE = Registration.TILES.register("fluid_pipe", () -> TileEntityType.Builder.create(TileFluidPipe::new, ModBlocks.FLUID_PIPE.get()).build(null));
	public static final RegistryObject<TileEntityType<TileFluidTank>> FLUID_TANK = Registration.TILES.register("fluid_tank", () -> TileEntityType.Builder.create(TileFluidTank::new, ModBlocks.FLUID_TANK.get()).build(null));
	public static final RegistryObject<TileEntityType<ElectricCrusherTile.Basic>> BASIC_CRUSHER = Registration.TILES.register("basic_crusher", MachineType.CRUSHER::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricCrusherTile>> CRUSHER = Registration.TILES.register("crusher", MachineType.CRUSHER::getStandardTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricFurnaceTile.Basic>> BASIC_FURNACE = Registration.TILES.register("basic_furnace", MachineType.FURNACE::getBasicTileEntityType);
	public static final RegistryObject<TileEntityType<ElectricFurnaceTile>> FURNACE = Registration.TILES.register("furnace", MachineType.FURNACE::getStandardTileEntityType);


	public static void register()
	{
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderers(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(FLUID_TANK.get(), TERFluidTank::new);
		ClientRegistry.bindTileEntityRenderer(FLUID_PIPE.get(), TERFluidPipe::new);
		ClientRegistry.bindTileEntityRenderer(CRUSHER.get(), TileEntityRendererAnimation<ElectricCrusherTile>::new);
		ClientRegistry.bindTileEntityRenderer(BASIC_FURNACE.get(), ElectricFurnaceTileRenderer::new);
		ClientRegistry.bindTileEntityRenderer(FURNACE.get(), ElectricFurnaceTileRenderer::new);
	}
}
