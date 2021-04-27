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

public class MachineType<T extends AbstractMachineBaseTileEntity, B extends T, S extends T, C extends Container>
{
	//	public static final MachineType<AlloySmelterTileEntity, AlloySmelterTileEntity.Basic, AlloySmelterTileEntity, AlloySmelterContainer> ALLOY_SMELTER = new MachineType<>(
//			() -> TileEntityType.Builder.create(AlloySmelterTileEntity.Basic::new, ModBlocks.BASIC_ALLOY_SMELTER.get()),
//			() -> TileEntityType.Builder.create(AlloySmelterTileEntity::new, ModBlocks.ALLOY_SMELTER.get()),
//			(id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.BASIC),
//			(id, inv) -> new AlloySmelterContainer(id, inv, MachineTier.STANDARD)
//	);
	public static final MachineType<ElectricCrusherTile, ElectricCrusherTile.Basic, ElectricCrusherTile, ElectricCrusherContainer> CRUSHER = new MachineType<>(
			() -> TileEntityType.Builder.create(ElectricCrusherTile.Basic::new, ModBlocks.BASIC_CRUSHER.get()),
			() -> TileEntityType.Builder.create(ElectricCrusherTile::new, ModBlocks.CRUSHER.get()),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new ElectricCrusherContainer(id, inv, MachineTier.STANDARD)
	);

	public static final MachineType<ElectricFurnaceTile, ElectricFurnaceTile.Basic, ElectricFurnaceTile, ElectricFurnaceContainer> FURNACE = new MachineType<>(
			() -> TileEntityType.Builder.create(ElectricFurnaceTile.Basic::new, ModBlocks.BASIC_FURNACE.get()),
			() -> TileEntityType.Builder.create(ElectricFurnaceTile::new, ModBlocks.FURNACE.get()),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.BASIC),
			(id, inv) -> new ElectricFurnaceContainer(id, inv, MachineTier.STANDARD)
	);

	private final Lazy<TileEntityType<B>> basicTileEntityType;
	private final Lazy<TileEntityType<S>> standardTileEntityType;
	private final Lazy<ContainerType<C>> basicContainerType;
	private final Lazy<ContainerType<C>> standardContainerType;

	public MachineType(
			Supplier<TileEntityType.Builder<B>> basic,
			Supplier<TileEntityType.Builder<S>> standard,
			ContainerType.IFactory<C> basicContainer,
			ContainerType.IFactory<C> standardContainer
	)
	{
		this.basicTileEntityType = Lazy.of(() -> basic.get().build(null));
		this.standardTileEntityType = Lazy.of(() -> standard.get().build(null));
		this.basicContainerType = Lazy.of(() -> new ContainerType<>(basicContainer));
		this.standardContainerType = Lazy.of(() -> new ContainerType<>(standardContainer));
	}

	public TileEntityType<? extends T> getTileEntityType(MachineTier tier)
	{
		switch (tier)
		{
			case BASIC:
				return basicTileEntityType.get();
			case STANDARD:
				return standardTileEntityType.get();
			default:
				throw new IllegalArgumentException("Unknown MachineTier: " + tier);
		}
	}

	public TileEntityType<B> getBasicTileEntityType()
	{
		return basicTileEntityType.get();
	}

	public TileEntityType<S> getStandardTileEntityType()
	{
		return standardTileEntityType.get();
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
			case STANDARD:
				return standardContainerType.get();
			default:
				throw new IllegalArgumentException("Unknown MachineTier: " + tier);
		}
	}
}
