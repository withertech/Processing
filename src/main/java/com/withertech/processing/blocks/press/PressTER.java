package com.withertech.processing.blocks.press;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.withertech.processing.blocks.press.PressBlock;
import com.withertech.processing.blocks.press.PressTile;
import com.withertech.processing.blocks.press.PressModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class PressTER extends GeoBlockRenderer<PressTile>
{
	public PressTER(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new PressModel());
	}

	@Override
	public void renderEarly(PressTile animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		AnimationProcessor<?> processor = this.getGeoModelProvider().getAnimationProcessor();
		processor.getBone("rightPort").setHidden(!animatable.getBlockState().get(PressBlock.RIGHT));
		processor.getBone("backPort").setHidden(!animatable.getBlockState().get(PressBlock.BACK));
		processor.getBone("leftPort").setHidden(!animatable.getBlockState().get(PressBlock.LEFT));
		processor.getBone("downPort").setHidden(!animatable.getBlockState().get(PressBlock.DOWN));

	}
}
