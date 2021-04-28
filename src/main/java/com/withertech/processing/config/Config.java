package com.withertech.processing.config;

import com.withertech.processing.Processing;
import com.withertech.processing.init.ModOres;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Processing.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Config
{

	public static ConfigBuilder createConfig()
	{
		ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslationTextComponent("config.processing.title"));
		builder.setDefaultBackgroundTexture(new ResourceLocation("minecraft:textures/block/oak_planks.png"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory world = builder.getOrCreateCategory(new TranslationTextComponent("config.processing.world"));
		SubCategoryBuilder ores = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.world.ores"));
		ores.add(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.processing.world.ores.master"), getOreWorldGenMasterSwitch()).setSaveConsumer(Config::setOreWorldGenMasterSwitch).setDefaultValue(true).build());
		for(Map.Entry<ModOres, OreConfig> entry : oreConfigs.entrySet())
		{
			SubCategoryBuilder ore = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.world.ores." + entry.getKey().getName()));
			ore.add(entryBuilder
					.startBooleanToggle(new TranslationTextComponent("config.processing.world.ores." + entry.getKey().getName() + ".enable"), entry.getValue().getEnabled())
					.setSaveConsumer(newVal -> entry.getValue().setEnabled(newVal))
					.setDefaultValue(entry.getKey().getDefaultOreConfigs().getEnabled())
					.build());
			ore.add(entryBuilder
					.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + entry.getKey().getName() + ".veinCount"), entry.getValue().getVeinCount(), 0, Integer.MAX_VALUE)
					.setSaveConsumer(newVal -> entry.getValue().setVeinCount(newVal))
					.setDefaultValue(entry.getKey().getDefaultOreConfigs().getVeinCount())
					.build());
			ore.add(entryBuilder
					.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + entry.getKey().getName() + ".veinSize"), entry.getValue().getVeinSize(), 0, 100)
					.setSaveConsumer(newVal -> entry.getValue().setVeinSize(newVal))
					.setDefaultValue(entry.getKey().getDefaultOreConfigs().getVeinSize())
					.build());
			ore.add(entryBuilder
					.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + entry.getKey().getName() + ".minHeight"), entry.getValue().getMinHeight(), 0, 255)
					.setSaveConsumer(newVal -> entry.getValue().setMinHeight(newVal))
					.setDefaultValue(entry.getKey().getDefaultOreConfigs().getMinHeight())
					.build());
			ore.add(entryBuilder
					.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + entry.getKey().getName() + ".maxHeight"), entry.getValue().getMaxHeight(), 0, 255)
					.setSaveConsumer(newVal -> entry.getValue().setMaxHeight(newVal))
					.setDefaultValue(entry.getKey().getDefaultOreConfigs().getMaxHeight())
					.build());
			ores.add(ore.build());
		}
		world.addEntry(ores.build());
		builder.transparentBackground();
		builder.setSavingRunnable(commonSpec::save);
		return builder;
	}
	public static void registerModsPage() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> createConfig().setParentScreen(parent).build());
	}
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

	public static boolean getOreWorldGenMasterSwitch()
	{
		return oreWorldGenMasterSwitch.get();
	}

	public static void setOreWorldGenMasterSwitch(boolean oreWorldGenMasterSwitch)
	{
		Config.oreWorldGenMasterSwitch.set(oreWorldGenMasterSwitch);
		Config.oreWorldGenMasterSwitch.save();
	}

	public static OreConfig getConfig(ModOres ore)
	{
		return oreConfigs.get(ore);
	}

	private Config()
	{
	}

	public static void init() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonSpec);
	}

	public static void sync() {
	}

	@SubscribeEvent
	public static void sync(ModConfig.Loading event) {
		sync();
	}

	@SubscribeEvent
	public static void sync(ModConfig.Reloading event) {
		sync();
	}


}
