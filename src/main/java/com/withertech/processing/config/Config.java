package com.withertech.processing.config;

import com.withertech.processing.Processing;
import com.withertech.processing.init.ModOres;
import com.withertech.processing.items.MachineUpgrades;
import com.withertech.processing.util.MachineTier;
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

	public static final ForgeConfigSpec.BooleanValue showBetaWelcomeMessage;
	private static final ForgeConfigSpec commonSpec;
	private static final ForgeConfigSpec.BooleanValue oreWorldGenMasterSwitch;
	private static final Map<ModOres, OreConfig> oreConfigs = new EnumMap<>(ModOres.class);
	private static final Map<MachineTier, TierConfig> tierConfigs = new EnumMap<>(MachineTier.class);
	private static final Map<MachineUpgrades, UpgradeConfig> upgradeConfigs = new EnumMap<>(MachineUpgrades.class);

	static
	{
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		// World Gen/Ores
		{
			builder.push("general");
			showBetaWelcomeMessage = builder
					.comment("Toggle beta welcome message on world join")
					.define("showBetaWelcomeMessage", true);
			builder.pop();

			builder.push("world");

			builder.push("ores");
			oreWorldGenMasterSwitch = builder
					.comment("Set to 'false' to completely disable ore generation from this mod, ignoring all other settings.",
							"You can also enable/disable ores individually, but this is useful if you plan to use another mod for ore generation.")
					.define("masterSwitch", true);
			Arrays.stream(ModOres.values()).forEach(ore -> oreConfigs.put(ore, new OreConfig(ore, builder, oreWorldGenMasterSwitch)));
			builder.pop(2);
			builder.push("machines");
			builder.push("tiers");
			Arrays.stream(MachineTier.values()).forEach(tier -> tierConfigs.put(tier, new TierConfig(tier, builder)));
			builder.pop();
			builder.push("upgrades");
			Arrays.stream(MachineUpgrades.values()).forEach(upgrade -> upgradeConfigs.put(upgrade, new UpgradeConfig(upgrade, builder)));
			builder.pop(2);


		}
		commonSpec = builder.build();
	}

	private Config()
	{
	}

	public static ConfigBuilder createConfig()
	{
		ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslationTextComponent("config.processing.title"));
		builder.setDefaultBackgroundTexture(new ResourceLocation("minecraft:textures/block/soul_soil.png"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		ConfigCategory general = builder.getOrCreateCategory(new TranslationTextComponent("config.processing.general"));
		general.addEntry(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.processing.general.showBetaWelcomeMessage"), getShowBetaWelcomeMessage())
				.setTooltip(new TranslationTextComponent("config.processing.general.showBetaWelcomeMessage.desc"))
				.setSaveConsumer(Config::setShowBetaWelcomeMessage)
				.setDefaultValue(true)
				.build());
		ConfigCategory world = builder.getOrCreateCategory(new TranslationTextComponent("config.processing.world"));
		SubCategoryBuilder ores = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.world.ores"));
		ores.add(entryBuilder.startBooleanToggle(new TranslationTextComponent("config.processing.world.ores.master"), getOreWorldGenMasterSwitch())
				.setTooltip(new TranslationTextComponent("config.processing.world.ores.master.desc"))
				.setSaveConsumer(Config::setOreWorldGenMasterSwitch)
				.setDefaultValue(true)
				.build());
		oreConfigs.forEach((key, value) ->
		{
			SubCategoryBuilder ore = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.world.ores." + key.getName()));
			ore.addAll(Arrays.asList(
					entryBuilder
							.startBooleanToggle(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".enable"), value.getEnabled())
							.setTooltip(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".enable.desc"))
							.setSaveConsumer(value::setEnabled)
							.setDefaultValue(key.getDefaultOreConfigs().getEnabled())
							.build(),
					entryBuilder
							.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".veinCount"), value.getVeinCount(), 0, Integer.MAX_VALUE)
							.setTooltip(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".veinCount.desc"))
							.setSaveConsumer(value::setVeinCount)
							.setDefaultValue(key.getDefaultOreConfigs().getVeinCount())
							.build(),
					entryBuilder
							.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".veinSize"), value.getVeinSize(), 0, 100)
							.setTooltip(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".veinSize.desc"))
							.setSaveConsumer(value::setVeinSize)
							.setDefaultValue(key.getDefaultOreConfigs().getVeinSize())
							.build(),
					entryBuilder
							.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".minHeight"), value.getMinHeight(), 0, 255)
							.setTooltip(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".minHeight.desc"))
							.setSaveConsumer(value::setMinHeight)
							.setDefaultValue(key.getDefaultOreConfigs().getMinHeight())
							.build(),
					entryBuilder
							.startIntSlider(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".maxHeight"), value.getMaxHeight(), 0, 255)
							.setTooltip(new TranslationTextComponent("config.processing.world.ores." + key.getName() + ".maxHeight.desc"))
							.setSaveConsumer(value::setMaxHeight)
							.setDefaultValue(key.getDefaultOreConfigs().getMaxHeight())
							.build()
			));

			ores.add(ore.build());
		});
		world.addEntry(ores.build());
		ConfigCategory machines = builder.getOrCreateCategory(new TranslationTextComponent("config.processing.machines"));
		SubCategoryBuilder tiers = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.machines.tiers"));
		tierConfigs.forEach((key, value) ->
		{
			SubCategoryBuilder tier = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.machines.tiers." + key.getName()));
			tier.addAll(Arrays.asList(
					entryBuilder
							.startIntField(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".upgradeSlots"), value.getUpgradeSlots())
							.setTooltip(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".upgradeSlots.desc"))
							.setMin(0)
							.setMax(9)
							.setSaveConsumer(value::setUpgradeSlots)
							.setDefaultValue(key.getDefaultConfig().getUpgradeSlots())
							.build(),
					entryBuilder
							.startIntField(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".energyCapacity"), value.getEnergyCapacity())
							.setTooltip(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".energyCapacity.desc"))
							.setMin(0)
							.setMax(1_000_000_000)
							.setSaveConsumer(value::setEnergyCapacity)
							.setDefaultValue(key.getDefaultConfig().getEnergyCapacity())
							.build(),
					entryBuilder
							.startIntField(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".storageMultiplier"), value.getStorageMultiplier())
							.setTooltip(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".storageMultiplier.desc"))
							.setMin(0)
							.setMax(64)
							.setSaveConsumer(value::setStorageMultiplier)
							.setDefaultValue(key.getDefaultConfig().getStorageMultiplier())
							.build(),
					entryBuilder
							.startIntField(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".operationsPerTick"), value.getOperationsPerTick())
							.setTooltip(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".operationsPerTick.desc"))
							.setMin(0)
							.setMax(64)
							.setSaveConsumer(value::setOperationsPerTick)
							.setDefaultValue(key.getDefaultConfig().getOperationsPerTick())
							.build(),
					entryBuilder
							.startFloatField(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".processingSpeed"), value.getProcessingSpeed())
							.setTooltip(new TranslationTextComponent("config.processing.machines.tiers." + key.getName() + ".processingSpeed.desc"))
							.setMin(0.0f)
							.setMax(1000.0f)
							.setSaveConsumer(value::setProcessingSpeed)
							.setDefaultValue(key.getDefaultConfig().getProcessingSpeed())
							.build()
			));
			tiers.add(tier.build());
		});
		machines.addEntry(tiers.build());
		SubCategoryBuilder upgrades = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.machines.upgrades"));
		upgradeConfigs.forEach((key, value) ->
		{
			SubCategoryBuilder upgrade = entryBuilder.startSubCategory(new TranslationTextComponent("config.processing.machines.upgrades." + key.getName()));
			upgrade.addAll(Arrays.asList(
					entryBuilder
							.startFloatField(new TranslationTextComponent("config.processing.machines.upgrades." + key.getName() + ".upgradeValue"), value.getUpgradeValue())
							.setTooltip(new TranslationTextComponent("config.processing.machines.upgrades." + key.getName() + ".upgradeValue.desc"))
							.setMin(-1000.0f)
							.setMax(1000.0f)
							.setSaveConsumer(value::setUpgradeValue)
							.setDefaultValue(key.getDefaultConfig().getUpgradeValue())
							.build(),
					entryBuilder
							.startFloatField(new TranslationTextComponent("config.processing.machines.upgrades." + key.getName() + ".energyUsage"), value.getEnergyUsage())
							.setTooltip(new TranslationTextComponent("config.processing.machines.upgrades." + key.getName() + ".energyUsage.desc"))
							.setMin(-1000.0f)
							.setMax(1000.0f)
							.setSaveConsumer(value::setEnergyUsage)
							.setDefaultValue(key.getDefaultConfig().getEnergyUsage())
							.build()
			));
			upgrades.add(upgrade.build());
		});
		machines.addEntry(upgrades.build());
		builder.transparentBackground();
		builder.setSavingRunnable(commonSpec::save);
		return builder;
	}

	public static void registerModsPage()
	{
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> createConfig().setParentScreen(parent).build());
	}

	public static boolean getOreWorldGenMasterSwitch()
	{
		return oreWorldGenMasterSwitch.get();
	}

	public static void setOreWorldGenMasterSwitch(boolean oreWorldGenMasterSwitch)
	{
		Config.oreWorldGenMasterSwitch.set(oreWorldGenMasterSwitch);
	}

	public static boolean getShowBetaWelcomeMessage()
	{
		return showBetaWelcomeMessage.get();
	}

	public static void setShowBetaWelcomeMessage(boolean showBetaWelcomeMessage)
	{
		Config.showBetaWelcomeMessage.set(showBetaWelcomeMessage);
	}

	public static OreConfig getOreConfig(ModOres ore)
	{
		return oreConfigs.get(ore);
	}

	public static TierConfig getTierConfig(MachineTier tier)
	{
		return tierConfigs.get(tier);
	}

	public static UpgradeConfig getUpgradeConfig(MachineUpgrades upgrade)
	{
		return upgradeConfigs.get(upgrade);
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
