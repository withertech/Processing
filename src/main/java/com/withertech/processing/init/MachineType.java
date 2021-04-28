package com.withertech.processing.init;


import com.withertech.processing.blocks.AbstractMachineBaseTileEntity;
import com.withertech.processing.blocks.crusher.ElectricCrusherContainer;
import com.withertech.processing.blocks.crusher.ElectricCrusherTile;
import com.withertech.processing.blocks.furnace.ElectricFurnaceContainer;
import com.withertech.processing.blocks.furnace.ElectricFurnaceTile;
import com.withertech.processing.util.MachineTier;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

import java.util.Objects;
import java.util.function.Supplier;

public class MachineType<T extends AbstractMachineBaseTileEntity, B extends T, A extends T, U extends T, C extends Container>
{
	//	public static final MachineType<AlloySmelterTileEntity, AlloySmelterTileEntity.Basic, AlloySmelterTileEntity, AlloySmelterContainer> ALLOY_SMELTER = new MachineType<>(
//			() -> TileEntityType.Builder.create(AlloySmelterTileEntity.Basic::new, ModBlocks.BASIC_ALLOY_SMELTER.get()),
//			() -> TileEntityType.Builder.create(AlloySmelterTileEntity::new, ModBlocks.ALLOY_SMELTER.get()),
//			(id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.BASIC),
//			(id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.STANDARD)
//	);
	public static final MachineType<ElectricCrusherTile, ElectricCrusherTile.Basic, ElectricCrusherTile.Advanced, ElectricCrusherTile.Ultimate, ElectricCrusherContainer> CRUSHER = new MachineType<>(
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Basic::new, ModBlocks.BASIC_CRUSHER.get()),
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Advanced::new, ModBlocks.ADVANCED_CRUSHER.get()),
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Ultimate::new, ModBlocks.ULTIMATE_CRUSHER.get()),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.ULTIMATE)
	);

	public static final MachineType<ElectricFurnaceTile, ElectricFurnaceTile.Basic, ElectricFurnaceTile.Advanced, ElectricFurnaceTile.Ultimate, ElectricFurnaceContainer> FURNACE = new MachineType<>(
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Basic::new, ModBlocks.BASIC_FURNACE.get()),
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Advanced::new, ModBlocks.ADVANCED_FURNACE.get()),
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Ultimate::new, ModBlocks.ULTIMATE_FURNACE.get()),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.ADVANCED),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.ULTIMATE)
	);

	private final Lazy<TileEntityType<B>> basicTileEntityType;
	private final Lazy<TileEntityType<A>> advancedTileEntityType;
	private final Lazy<TileEntityType<U>> ultimateTileEntityType;
	private final Lazy<ContainerType<C>> basicContainerType;
	private final Lazy<ContainerType<C>> advancedContainerType;
	private final Lazy<ContainerType<C>> ultimateContainerType;

	public MachineType(
			Supplier<TileEntityType.Builder<B>> basic,
			Supplier<TileEntityType.Builder<A>> advanced,
			Supplier<TileEntityType.Builder<U>> ultimate,
			ContainerType.IFactory<C> basicContainer,
			ContainerType.IFactory<C> advancedContainer,
			ContainerType.IFactory<C> ultimateContainer
	)
	{
		this.basicTileEntityType = Lazy.of(() -> basic.get().build(null));
		this.advancedTileEntityType = Lazy.of(() -> advanced.get().build(null));
		this.ultimateTileEntityType = Lazy.of(() -> ultimate.get().build(null));
		this.basicContainerType = Lazy.of(() -> new ContainerType<>(basicContainer));
		this.advancedContainerType = Lazy.of(() -> new ContainerType<>(advancedContainer));
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
			case ULTIMATE:
				return ultimateContainerType.get();
			default:
				throw new IllegalArgumentException("Unknown MachineTier: " + tier);
		}
	}
}
