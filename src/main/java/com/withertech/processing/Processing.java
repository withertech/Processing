package com.withertech.processing;

import com.withertech.processing.config.Config;
import com.withertech.processing.event.Greetings;
import com.withertech.processing.init.*;
import com.withertech.processing.items.ChassisBlockItem;
import com.withertech.processing.items.FactoryBlockItem;
import com.withertech.processing.items.ResourceBlockItem;
import com.withertech.processing.items.ResourceItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Processing.MODID)
public class Processing
{

	public static final String MODID = "processing";
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();

	public Processing()
	{
		Config.init();
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Config::registerModsPage);
		GeckoLib.initialize();
		Registration.register();

		// Register the setup method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		// Register the doClientStuff method for modloading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
		Greetings.addMessage(Processing::getBetaWelcomeMessage);
	}

	@Nullable
	private static ITextComponent getBetaWelcomeMessage(PlayerEntity p)
	{
		return Config.showBetaWelcomeMessage.get()
				? new StringTextComponent("Thanks for trying Processing! This mod is early in development, expect bugs and changes. You can disable this message in the config.")
				: null;
	}

	public static ResourceLocation getId(String path)
	{
		return new ResourceLocation(MODID, path);
	}

	private void setup(final FMLCommonSetupEvent event)
	{
		OreGeneration.registerOres();
	}

	private void doClientStuff(final FMLClientSetupEvent event)
	{
		RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_TANK.get(), (layer) -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_PIPE.get(), (layer) -> layer == RenderType.getSolid() || layer == RenderType.getTranslucent());
		ModTiles.registerRenderers(event);
		ModContainers.registerScreens(event);
	}

}
