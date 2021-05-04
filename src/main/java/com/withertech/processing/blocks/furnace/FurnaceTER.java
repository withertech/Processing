package com.withertech.processing.blocks.furnace;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.withertech.processing.blocks.furnace.FurnaceBlock;
import com.withertech.processing.blocks.furnace.FurnaceTile;
import com.withertech.processing.blocks.furnace.FurnaceModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class FurnaceTER extends GeoBlockRenderer<FurnaceTile>
{
	public FurnaceTER(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new FurnaceModel());
	}

	@Override
	public void renderEarly(FurnaceTile animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		AnimationProcessor<?> processor = this.getGeoModelProvider().getAnimationProcessor();
		processor.getBone("rightPort").setHidden(!animatable.getBlockState().get(FurnaceBlock.RIGHT));
		processor.getBone("backPort").setHidden(!animatable.getBlockState().get(FurnaceBlock.BACK));
		processor.getBone("leftPort").setHidden(!animatable.getBlockState().get(FurnaceBlock.LEFT));
		processor.getBone("downPort").setHidden(!animatable.getBlockState().get(FurnaceBlock.DOWN));

	}
}
