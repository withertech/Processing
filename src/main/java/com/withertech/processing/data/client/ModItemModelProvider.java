package com.withertech.processing.data.client;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.IBlockProvider;
import com.withertech.processing.init.ModGems;
import com.withertech.processing.init.ModItems;
import com.withertech.processing.init.ModMetals;
import com.withertech.processing.init.Registration;
import com.withertech.processing.items.MachineUpgrades;
import com.withertech.processing.util.NameUtils;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class ModItemModelProvider extends ItemModelProvider
{
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		super(generator, Processing.MODID, existingFileHelper);
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "Processing - Item Models";
	}

	@Override
	protected void registerModels()
	{
		ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
		ModelFile itemHandheld = getExistingFile(mcLoc("item/handheld"));
		//noinspection OverlyLongLambda
		Arrays.stream(ModMetals.values()).forEach(metal ->
		{
			metal.getOre().ifPresent(this::blockBuilder);
			metal.getStorageBlock().ifPresent(this::blockBuilder);
			metal.getDust().ifPresent(item -> builder(item, itemGenerated));
			metal.getIngot().ifPresent(item -> builder(item, itemGenerated));
			metal.getNugget().ifPresent(item -> builder(item, itemGenerated));
			metal.getPlate().ifPresent(item -> builder(item, itemGenerated));
			metal.getRod().ifPresent(item -> builder(item, itemGenerated));
			metal.getGear().ifPresent(item -> builder(item, itemGenerated));
		});

		Arrays.stream(ModGems.values()).forEach(gem ->
		{
			gem.getOre().ifPresent(this::blockBuilder);
			gem.getStorageBlock().ifPresent(this::blockBuilder);
			gem.getDust().ifPresent(item -> builder(item, itemGenerated));
			gem.getGem().ifPresent(item -> builder(item, itemGenerated));
		});

		Arrays.stream(MachineUpgrades.values()).forEach(upgrade ->
				builder(upgrade.asItem(), itemGenerated));


		Registration.BLOCKS.getEntries().stream()
				.map(RegistryObject::get)
				.filter(block -> block.asItem() != Items.AIR)
				.forEach(this::blockBuilder);


		builder(ModItems.PLATE_PRESS.get(), itemGenerated);
		builder(ModItems.ROD_PRESS.get(), itemGenerated);
		builder(ModItems.GEAR_PRESS.get(), itemGenerated);

		builder(ModItems.WRENCH.get(), itemHandheld);
		builder(ModItems.HAMMER.get(), itemHandheld);
	}

	private void blockBuilder(IBlockProvider block)
	{
		blockBuilder(block.asBlock());
	}

	private void blockBuilder(Block block)
	{
		String name = NameUtils.from(block).getPath();
		withExistingParent(name, modLoc("block/" + name));
	}

	private void builder(IItemProvider item, ModelFile parent)
	{
		String name = NameUtils.fromItem(item).getPath();
		builder(item, parent, "item/" + name);
	}

	private void builder(IItemProvider item, ModelFile parent, String texture)
	{
		getBuilder(NameUtils.fromItem(item).getPath())
				.parent(parent)
				.texture("layer0", modLoc(texture));
	}
}
