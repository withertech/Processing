package com.withertech.processing.init;


import com.withertech.processing.blocks.AbstractMachineBaseTileEntity;
import com.withertech.processing.blocks.crusher.CrusherContainer;
import com.withertech.processing.blocks.crusher.CrusherTile;
import com.withertech.processing.blocks.furnace.FurnaceContainer;
import com.withertech.processing.blocks.furnace.FurnaceTile;
import com.withertech.processing.blocks.press.PressContainer;
import com.withertech.processing.blocks.press.PressTile;
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
	public static final MachineType<CrusherTile, CrusherTile.Basic, CrusherTile.Advanced, CrusherTile.Elite, CrusherTile.Ultimate, CrusherContainer> CRUSHER = new MachineType<>(
			() -> TileEntityType.Builder.create(CrusherTile.Basic::new, ModFactoryBlocks.CRUSHER.getBlock(MachineTier.BASIC)),
			() -> TileEntityType.Builder.create(CrusherTile.Advanced::new, ModFactoryBlocks.CRUSHER.getBlock(MachineTier.ADVANCED)),
			() -> TileEntityType.Builder.create(CrusherTile.Elite::new, ModFactoryBlocks.CRUSHER.getBlock(MachineTier.ELITE)),
			() -> TileEntityType.Builder.create(CrusherTile.Ultimate::new, ModFactoryBlocks.CRUSHER.getBlock(MachineTier.ULTIMATE)),
			(id, inv) -> new CrusherContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new CrusherContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new CrusherContainer(id, inv, MachineTier.ELITE),
			(id, inv) -> new CrusherContainer(id, inv, MachineTier.ULTIMATE)
	);

	public static final MachineType<PressTile, PressTile.Basic, PressTile.Advanced, PressTile.Elite, PressTile.Ultimate, PressContainer> PRESS = new MachineType<>(
			() -> TileEntityType.Builder.create(PressTile.Basic::new, ModFactoryBlocks.PRESS.getBlock(MachineTier.BASIC)),
			() -> TileEntityType.Builder.create(PressTile.Advanced::new, ModFactoryBlocks.PRESS.getBlock(MachineTier.ADVANCED)),
			() -> TileEntityType.Builder.create(PressTile.Elite::new, ModFactoryBlocks.PRESS.getBlock(MachineTier.ELITE)),
			() -> TileEntityType.Builder.create(PressTile.Ultimate::new, ModFactoryBlocks.PRESS.getBlock(MachineTier.ULTIMATE)),
			(id, inv) -> new PressContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new PressContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new PressContainer(id, inv, MachineTier.ELITE),
			(id, inv) -> new PressContainer(id, inv, MachineTier.ULTIMATE)
	);

	public static final MachineType<FurnaceTile, FurnaceTile.Basic, FurnaceTile.Advanced, FurnaceTile.Elite, FurnaceTile.Ultimate, FurnaceContainer> FURNACE = new MachineType<>(
			() -> TileEntityType.Builder.create(FurnaceTile.Basic::new, ModFactoryBlocks.FURNACE.getBlock(MachineTier.BASIC)),
			() -> TileEntityType.Builder.create(FurnaceTile.Advanced::new, ModFactoryBlocks.FURNACE.getBlock(MachineTier.ADVANCED)),
			() -> TileEntityType.Builder.create(FurnaceTile.Elite::new, ModFactoryBlocks.FURNACE.getBlock(MachineTier.ELITE)),
			() -> TileEntityType.Builder.create(FurnaceTile.Ultimate::new, ModFactoryBlocks.FURNACE.getBlock(MachineTier.ULTIMATE)),
			(id, inv) -> new FurnaceContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new FurnaceContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new FurnaceContainer(id, inv, MachineTier.ELITE),
			(id, inv) -> new FurnaceContainer(id, inv, MachineTier.ULTIMATE)
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
