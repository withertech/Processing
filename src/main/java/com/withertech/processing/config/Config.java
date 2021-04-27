package com.withertech.processing.config;

import com.withertech.processing.Processing;
import com.withertech.processing.init.ModOres;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Processing.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config
{
	private static final ForgeConfigSpec commonSpec;
	private static final ForgeConfigSpec.BooleanValue oreWorldGenMasterSwitch;
	private static final Map<ModOres, OreConfig> oreConfigs = new EnumMap<>(ModOres.class);

	static
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		// World Gen/Ores
		{
			builder.push("world");
			oreWorldGenMasterSwitch = builder
					.comment("Set to 'false' to completely disable ore generation from this mod, ignoring all other settings.",
							"You can also enable/disable ores individually, but this is useful if you plan to use another mod for ore generation.")
					.define("masterSwitch", true);

			builder.comment("Configs for specific ores. Set veinCount to zero to disable an ore.");
			builder.push("ores");
			Arrays.stream(ModOres.values()).forEach(ore -> oreConfigs.put(ore, new OreConfig(ore, builder, oreWorldGenMasterSwitch)));

			builder.pop(2);
		}

		commonSpec = builder.build();
	}

	private Config()
	{
	}

	public static Optional<OreConfig> getOreConfig(ModOres ore)
	{
		return Optional.ofNullable(oreConfigs.getOrDefault(ore, null));
	}

	public static void init()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
	}

	public static void sync()
	{
	}

	@SubscribeEvent
	public static void sync(ModConfig.Loading event)
	{
		sync();
	}

	@SubscribeEvent
	public static void sync(ModConfig.Reloading event)
	{
		sync();
	}
}
