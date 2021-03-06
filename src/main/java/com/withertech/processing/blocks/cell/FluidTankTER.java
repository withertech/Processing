package com.withertech.processing.blocks.cell;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.withertech.processing.util.FluidUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class FluidTankTER extends TileEntityRenderer<FluidTankTile>
{

	public static final float TANK_THICKNESS = 0.0625f;

	public FluidTankTER(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(@Nonnull FluidTankTile tileEntityIn, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if (tileEntityIn.isRemoved() || tileEntityIn.getTank().getFluid().isEmpty())
			return;

		matrixStackIn.push();
		renderFluid(tileEntityIn, matrixStackIn, bufferIn);
		matrixStackIn.pop();
	}

	private void renderFluid(@Nonnull FluidTankTile tileEntity, MatrixStack matrix, IRenderTypeBuffer bufferIn)
	{
		FluidStack fluid = tileEntity.getTank().getFluid();
		float scale = (1.0f - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluid.getAmount() / (tileEntity.getTank().getCapacity());
		if (scale > 0.0f)
		{
			Matrix4f matrix4f = matrix.getLast().getMatrix();
			TextureAtlasSprite sprite = FluidUtils.getFluidTexture(fluid);
			if (sprite == null)
				return;
			IVertexBuilder renderer = bufferIn.getBuffer(RenderType.getText(sprite.getAtlasTexture().getTextureLocation()));

			float u1 = sprite.getMinU();
			float v1 = sprite.getMinV();
			float u2 = sprite.getMaxU();
			float v2 = sprite.getMaxV();

			float margin = 0.99f;
			float offset = 0.01f;

			int color = fluid.getFluid().getAttributes().getColor();

			float r = FluidUtils.getRed(color);
			float g = FluidUtils.getGreen(color);
			float b = FluidUtils.getBlue(color);
			float a = FluidUtils.getAlpha(color);
			int light = 15728880;

			// Top
			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

			// Bottom
			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();

			// Sides
			//NORTH
			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();

			//SOUTH
			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();

			//WEAST
			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();

			//EAST
			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
		}
	}
}
