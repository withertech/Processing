package com.withertech.processing.client.renderer.tile.press;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.withertech.processing.blocks.press.ElectricPressBlock;
import com.withertech.processing.blocks.press.ElectricPressTile;
import com.withertech.processing.client.model.block.press.ElectricPressModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class ElectricPressTileRenderer extends GeoBlockRenderer<ElectricPressTile>
{
	public ElectricPressTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new ElectricPressModel());
	}

	@Override
	public void renderEarly(ElectricPressTile animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		AnimationProcessor<?> processor = this.getGeoModelProvider().getAnimationProcessor();
		processor.getBone("rightPort").setHidden(!animatable.getBlockState().get(ElectricPressBlock.RIGHT));
		processor.getBone("backPort").setHidden(!animatable.getBlockState().get(ElectricPressBlock.BACK));
		processor.getBone("leftPort").setHidden(!animatable.getBlockState().get(ElectricPressBlock.LEFT));
		processor.getBone("downPort").setHidden(!animatable.getBlockState().get(ElectricPressBlock.DOWN));

	}
}
