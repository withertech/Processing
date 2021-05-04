package com.withertech.processing.blocks.crusher;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.withertech.processing.blocks.crusher.CrusherBlock;
import com.withertech.processing.blocks.crusher.CrusherTile;
import com.withertech.processing.blocks.crusher.CrusherModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class CrusherTER extends GeoBlockRenderer<CrusherTile>
{
	public CrusherTER(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new CrusherModel());
	}

	@Override
	public void renderEarly(CrusherTile animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		AnimationProcessor<?> processor = this.getGeoModelProvider().getAnimationProcessor();
		processor.getBone("rightPort").setHidden(!animatable.getBlockState().get(CrusherBlock.RIGHT));
		processor.getBone("backPort").setHidden(!animatable.getBlockState().get(CrusherBlock.BACK));
		processor.getBone("leftPort").setHidden(!animatable.getBlockState().get(CrusherBlock.LEFT));
		processor.getBone("downPort").setHidden(!animatable.getBlockState().get(CrusherBlock.DOWN));

	}
}
