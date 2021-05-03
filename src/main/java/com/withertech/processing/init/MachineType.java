package com.withertech.processing.init;


import com.withertech.processing.blocks.AbstractMachineBaseTileEntity;
import com.withertech.processing.blocks.crusher.ElectricCrusherContainer;
import com.withertech.processing.blocks.crusher.ElectricCrusherTile;
import com.withertech.processing.blocks.furnace.ElectricFurnaceContainer;
import com.withertech.processing.blocks.furnace.ElectricFurnaceTile;
import com.withertech.processing.blocks.press.ElectricPressContainer;
import com.withertech.processing.blocks.press.ElectricPressTile;
import com.withertech.processing.util.MachineTier;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

import java.util.Objects;
import java.util.function.Supplier;

public class MachineType<T extends AbstractMachineBaseTileEntity, B extends T, A extends T, E extends T, U extends T, C extends Container>
{
	//	public static final MachineType<AlloySmelterTileEntity, AlloySmelterTileEntity.Basic, AlloySmelterTileEntity, AlloySmelterContainer> ALLOY_SMELTER = new MachineType<>(
//			() -> TileEntityType.Builder.create(AlloySmelterTileEntity.Basic::new, ModBlocks.BASIC_ALLOY_SMELTER.get()),
//			() -> TileEntityType.Builder.create(AlloySmelterTileEntity::new, ModBlocks.ALLOY_SMELTER.get()),
//			(id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.BASIC),
//			(id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.STANDARD)
//	);
	public static final MachineType<ElectricCrusherTile, ElectricCrusherTile.Basic, ElectricCrusherTile.Advanced, ElectricCrusherTile.Elite, ElectricCrusherTile.Ultimate, ElectricCrusherContainer> CRUSHER = new MachineType<>(
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Basic::new, ModBlocks.BASIC_CRUSHER.get()),
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Advanced::new, ModBlocks.ADVANCED_CRUSHER.get()),
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Elite::new, ModBlocks.ELITE_CRUSHER.get()),
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Ultimate::new, ModBlocks.ULTIMATE_CRUSHER.get()),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.ELITE),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.ULTIMATE)
	);

	public static final MachineType<ElectricPressTile, ElectricPressTile.Basic, ElectricPressTile.Advanced, ElectricPressTile.Elite, ElectricPressTile.Ultimate, ElectricPressContainer> PRESS = new MachineType<>(
			() -> TileEntityType.Builder.create(ElectricPressTile.Basic::new, ModBlocks.BASIC_PRESS.get()),
			() -> TileEntityType.Builder.create(ElectricPressTile.Advanced::new, ModBlocks.ADVANCED_PRESS.get()),
			() -> TileEntityType.Builder.create(ElectricPressTile.Elite::new, ModBlocks.ELITE_PRESS.get()),
			() -> TileEntityType.Builder.create(ElectricPressTile.Ultimate::new, ModBlocks.ULTIMATE_PRESS.get()),
			(id, inv) -> new ElectricPressContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new ElectricPressContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new ElectricPressContainer(id, inv, MachineTier.ELITE),
			(id, inv) -> new ElectricPressContainer(id, inv, MachineTier.ULTIMATE)
	);

	public static final MachineType<ElectricFurnaceTile, ElectricFurnaceTile.Basic, ElectricFurnaceTile.Advanced, ElectricFurnaceTile.Elite, ElectricFurnaceTile.Ultimate, ElectricFurnaceContainer> FURNACE = new MachineType<>(
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Basic::new, ModBlocks.BASIC_FURNACE.get()),
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Advanced::new, ModBlocks.ADVANCED_FURNACE.get()),
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Elite::new, ModBlocks.ELITE_FURNACE.get()),
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Ultimate::new, ModBlocks.ULTIMATE_FURNACE.get()),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.ELITE),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.ULTIMATE)
	);

	private final Lazy<TileEntityType<B>> basicTileEntityType;
	private final Lazy<TileEntityType<A>> advancedTileEntityType;
	private final Lazy<TileEntityType<E>> eliteTileEntityType;
	private final Lazy<TileEntityType<U>> ultimateTileEntityType;
	private final Lazy<ContainerType<C>> basicContainerType;
	private final Lazy<ContainerType<C>> advancedContainerType;
	private final Lazy<ContainerType<C>> eliteContainerType;
	private final Lazy<ContainerType<C>> ultimateContainerType;

	public MachineType(
			Supplier<TileEntityType.Builder<B>> basic,
			Supplier<TileEntityType.Builder<A>> advanced,
			Supplier<TileEntityType.Builder<E>> elite,
			Supplier<TileEntityType.Builder<U>> ultimate,
			ContainerType.IFactory<C> basicContainer,
			ContainerType.IFactory<C> advancedContainer,
			ContainerType.IFactory<C> eliteContainer,
			ContainerType.IFactory<C> ultimateContainer
	)
	{
		this.basicTileEntityType = Lazy.of(() -> basic.get().build(null));
		this.advancedTileEntityType = Lazy.of(() -> advanced.get().build(null));
		this.eliteTileEntityType = Lazy.of(() -> elite.get().build(null));
		this.ultimateTileEntityType = Lazy.of(() -> ultimate.get().build(null));
		this.basicContainerType = Lazy.of(() -> new ContainerType<>(basicContainer));
		this.advancedContainerType = Lazy.of(() -> new ContainerType<>(advancedContainer));
		this.eliteContainerType = Lazy.of(() -> new ContainerType<>(eliteContainer));
		this.ultimateContainerType = Lazy.of(() -> new ContainerType<>(ultimateContainer));
	}


	public TileEntityType<? extends T> getTileEntityType(MachineTier tier)
	{
		switch (tier)
		{
			case BASIC:
				return basicTileEntityType.get();
			case ADVANCED:
				return advancedTileEntityType.get();
			case ELITE:
				return eliteTileEntityType.get();
			case ULTIMATE:
				return ultimateTileEntityType.get();
			default:
				throw new IllegalArgumentException("Unknown MachineTier: " + tier);
		}
	}

	public TileEntityType<B> getBasicTileEntityType()
	{
		return basicTileEntityType.get();
	}

	public TileEntityType<A> getAdvancedTileEntityType()
	{
		return advancedTileEntityType.get();
	}

	public TileEntityType<E> getEliteTileEntityType()
	{
		return eliteTileEntityType.get();
	}

	public TileEntityType<U> getUltimateTileEntityType()
	{
		return ultimateTileEntityType.get();
	}

	public T create(MachineTier tier)
	{
		return Objects.requireNonNull(getTileEntityType(tier).create());
	}

	public ContainerType<C> getContainerType(MachineTier tier)
	{
		switch (tier)
		{
			case BASIC:
				return basicContainerType.get();
			case ADVANCED:
				return advancedContainerType.get();
			case ELITE:
				return eliteContainerType.get();
			case ULTIMATE:
				return ultimateContainerType.get();
			default:
				throw new IllegalArgumentException("Unknown MachineTier: " + tier);
		}
	}
}
