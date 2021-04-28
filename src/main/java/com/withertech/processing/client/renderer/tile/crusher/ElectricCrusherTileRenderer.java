package com.withertech.processing.client.renderer.tile.crusher;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.withertech.processing.blocks.crusher.ElectricCrusherBlock;
import com.withertech.processing.blocks.crusher.ElectricCrusherTile;
import com.withertech.processing.blocks.furnace.ElectricFurnaceBlock;
import com.withertech.processing.blocks.furnace.ElectricFurnaceTile;
import com.withertech.processing.client.model.block.crusher.ElectricCrusherModel;
import com.withertech.processing.client.model.block.furnace.ElectricFurnaceModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class ElectricCrusherTileRenderer extends GeoBlockRenderer<ElectricCrusherTile>
{
	public ElectricCrusherTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new ElectricCrusherModel());
	}

	@Override
	public void renderEarly(ElectricCrusherTile animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		AnimationProcessor<?> processor = this.getGeoModelProvider().getAnimationProcessor();
		processor.getBone("rightPort").setHidden(!animatable.getBlockState().get(ElectricCrusherBlock.RIGHT));
		processor.getBone("backPort").setHidden(!animatable.getBlockState().get(ElectricCrusherBlock.BACK));
		processor.getBone("leftPort").setHidden(!animatable.getBlockState().get(ElectricCrusherBlock.LEFT));
		processor.getBone("downPort").setHidden(!animatable.getBlockState().get(ElectricCrusherBlock.DOWN));

	}
}
