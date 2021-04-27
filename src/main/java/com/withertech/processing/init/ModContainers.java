package com.withertech.processing.init;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.crusher.ElectricCrusherContainer;
import com.withertech.processing.blocks.crusher.ElectricCrusherScreen;
import com.withertech.processing.blocks.furnace.ElectricFurnaceContainer;
import com.withertech.processing.blocks.furnace.ElectricFurnaceScreen;
import com.withertech.processing.util.MachineTier;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModContainers
{
//	public static ContainerType<BatteryBoxContainer> batteryBox;
//	public static ContainerType<CoalGeneratorContainer> coalGenerator;
//	public static ContainerType<CompressorContainer> compressor;
//	public static ContainerType<DieselGeneratorContainer> dieselGenerator;
//	public static ContainerType<ElectricFurnaceContainer> electricFurnace;
//	public static ContainerType<InfuserContainer> infuser;
//	public static ContainerType<LavaGeneratorContainer> lavaGenerator;
//	public static ContainerType<MixerContainer> mixer;
//	public static ContainerType<PumpContainer> pump;
//	public static ContainerType<RefineryContainer> refinery;
//	public static ContainerType<SolidifierContainer> solidifier;

	public static final RegistryObject<ContainerType<ElectricCrusherContainer>> BASIC_CRUSHER = Registration.CONTAINERS.register("basic_crusher", () -> MachineType.CRUSHER.getContainerType(MachineTier.BASIC));
	public static final RegistryObject<ContainerType<ElectricCrusherContainer>> CRUSHER = Registration.CONTAINERS.register("crusher", () -> MachineType.CRUSHER.getContainerType(MachineTier.STANDARD));
	public static final RegistryObject<ContainerType<ElectricFurnaceContainer>> BASIC_FURNACE = Registration.CONTAINERS.register("basic_furnace", () -> MachineType.FURNACE.getContainerType(MachineTier.BASIC));
	public static final RegistryObject<ContainerType<ElectricFurnaceContainer>> FURNACE = Registration.CONTAINERS.register("furnace", () -> MachineType.FURNACE.getContainerType(MachineTier.STANDARD));
	private ModContainers()
	{
	}

	public static void register()
	{
	}

	public static void registerAll(RegistryEvent.Register<ContainerType<?>> event)
	{
//		register("alloy_smelter", MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD));
//		batteryBox = register("battery_box", BatteryBoxContainer::new);
//		coalGenerator = register("coal_generator", CoalGeneratorContainer::new);
//		compressor = register("compressor", CompressorContainer::new);
//		dieselGenerator = register("diesel_generator", DieselGeneratorContainer::new);
//		infuser = register("infuser", InfuserContainer::new);
//		lavaGenerator = register("lava_generator", LavaGeneratorContainer::new);
//		mixer = register("mixer", MixerContainer::new);
//		pump = register("pump", PumpContainer::new);
//		refinery = register("refinery", RefineryContainer::new);
//		solidifier = register("solidifier", SolidifierContainer::new);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerScreens(FMLClientSetupEvent event)
	{
//		ScreenManager.registerFactory(MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD), AlloySmelterScreen::new);
//		ScreenManager.registerFactory(batteryBox, BatteryBoxScreen::new);
//		ScreenManager.registerFactory(coalGenerator, CoalGeneratorScreen::new);
//		ScreenManager.registerFactory(compressor, CompressorScreen::new);
		ScreenManager.registerFactory(MachineType.CRUSHER.getContainerType(MachineTier.BASIC), ElectricCrusherScreen::new);
		ScreenManager.registerFactory(MachineType.CRUSHER.getContainerType(MachineTier.STANDARD), ElectricCrusherScreen::new);
//		ScreenManager.registerFactory(dieselGenerator, DieselGeneratorScreen::new);
		ScreenManager.registerFactory(MachineType.FURNACE.getContainerType(MachineTier.BASIC), ElectricFurnaceScreen::new);
		ScreenManager.registerFactory(MachineType.FURNACE.getContainerType(MachineTier.STANDARD), ElectricFurnaceScreen::new);
//		ScreenManager.registerFactory(infuser, InfuserScreen::new);
//		ScreenManager.registerFactory(lavaGenerator, LavaGeneratorScreen::new);
//		ScreenManager.registerFactory(mixer, MixerScreen::new);
//		ScreenManager.registerFactory(pump, PumpScreen::new);
//		ScreenManager.registerFactory(refinery, RefineryScreen::new);
//		ScreenManager.registerFactory(solidifier, SolidifierScreen::new);
	}

	private static <C extends Container> ContainerType<C> register(String name, ContainerType.IFactory<C> containerFactory)
	{
		ContainerType<C> type = new ContainerType<>(containerFactory);
		return register(name, type);
	}

	private static <C extends Container> ContainerType<C> register(String name, ContainerType<C> containerType)
	{
		containerType.setRegistryName(Processing.getId(name));
		ForgeRegistries.CONTAINERS.register(containerType);
		return containerType;
	}
}