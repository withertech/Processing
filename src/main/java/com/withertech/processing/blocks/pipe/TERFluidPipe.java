package com.withertech.processing.blocks.pipe;

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

public class TERFluidPipe extends TileEntityRenderer<TileFluidPipe>
{

	public static final float TANK_THICKNESS = 0.0001f;

	public TERFluidPipe(TileEntityRendererDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(@Nonnull TileFluidPipe tileEntityIn, float partialTicks, @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if (tileEntityIn.isRemoved() || tileEntityIn.getPipeNetwork().getFluidInTank(1).isEmpty())
			return;

		matrixStackIn.push();
		renderFluid(tileEntityIn, matrixStackIn, bufferIn);
		matrixStackIn.pop();
	}

	private void renderFluid(@Nonnull TileFluidPipe tileEntity, MatrixStack matrix, IRenderTypeBuffer bufferIn)
	{
		FluidStack fluid = tileEntity.getPipeNetwork().getFluidInTank(1).copy();
		if (fluid.getFluid() != null)
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

			int color = fluid.getFluid().getAttributes().getColor();

			float r = FluidUtils.getRed(color);
			float g = FluidUtils.getGreen(color);
			float b = FluidUtils.getBlue(color);
			float a = FluidUtils.getAlpha(color);
			int light = 15728880;
			switch (tileEntity.getBlockState().get(BlockFluidPipe.UP))
			{
				case NONE:
				{
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					break;
				}
				case BOTH:
				{
					// NORTH
					renderer.pos(matrix4f, 0.625f, 1.0f, 0.375f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.6875f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.6875f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 1.0f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// SOUTH
					renderer.pos(matrix4f, 0.375f, 1.0f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.6875f, 0.625f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.6875f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 1.0f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// EAST
					renderer.pos(matrix4f, 0.625f, 1.0f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.6875f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.6875f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 1.0f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// WEST
					renderer.pos(matrix4f, 0.375f, 1.0f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.6875f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.6875f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 1.0f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
				}
			}
			switch (tileEntity.getBlockState().get(BlockFluidPipe.DOWN))
			{
				case NONE:
				{
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					break;
				}
				case BOTH:
				{
					// NORTH
					renderer.pos(matrix4f, 0.625f, 0.3125f, 0.375f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.0f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.0f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.3125f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// SOUTH
					renderer.pos(matrix4f, 0.375f, 0.3125f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.0f, 0.625f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.0f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.3125f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// EAST
					renderer.pos(matrix4f, 0.625f, 0.3125f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.0f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.0f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.3125f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// WEST
					renderer.pos(matrix4f, 0.375f, 0.3125f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.0f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.0f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.3125f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
				}
			}
			switch (tileEntity.getBlockState().get(BlockFluidPipe.NORTH))
			{
				case NONE:
				{
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.375f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					break;
				}
				case BOTH:
				{
					// UP
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.3125f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.0f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.0f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.3125f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// DOWN
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.3125f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.0f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.0f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.3125f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// WEST
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.0f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.0f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.3125f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.3125f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// EAST
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.3125f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.3125f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.0f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.0f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					break;
				}
			}
			switch (tileEntity.getBlockState().get(BlockFluidPipe.SOUTH))
			{
				case NONE:
				{
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.625f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					break;
				}
				case BOTH:
				{
					// UP
					renderer.pos(matrix4f, 0.625f, 0.625f, 1.0f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.6875f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.6875f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 1.0f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// DOWN
					renderer.pos(matrix4f, 0.375f, 0.375f, 1.0f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.6875f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.6875f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 1.0f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// WEST
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.6875f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.6875f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 1.0f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 1.0f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// EAST
					renderer.pos(matrix4f, 0.625f, 0.625f, 1.0f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 1.0f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.6875f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.6875f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					break;
				}
			}
			switch (tileEntity.getBlockState().get(BlockFluidPipe.EAST))
			{
				case NONE:
				{
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.625f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					break;
				}
				case BOTH:
				{
					// UP
					renderer.pos(matrix4f, 1.0f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 1.0f, 0.625f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.6875f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.6875f, 0.625f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// DOWN
					renderer.pos(matrix4f, 0.6875f, 0.375f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.6875f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 1.0f, 0.375f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 1.0f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// NORTH
					renderer.pos(matrix4f, 1.0f, 0.625f, 0.375f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 1.0f, 0.375f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.6875f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.6875f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// SOUTH
					renderer.pos(matrix4f, 0.6875f, 0.625f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.6875f, 0.375f, 0.625f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 1.0f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 1.0f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					break;
				}
			}
			switch (tileEntity.getBlockState().get(BlockFluidPipe.WEST))
			{
				case NONE:
				{
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.375f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					break;
				}
				case BOTH:
				{
					// UP
					renderer.pos(matrix4f, 0.3125f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.3125f, 0.625f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.0f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.0f, 0.625f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// DOWN
					renderer.pos(matrix4f, 0.0f, 0.375f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.0f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.3125f, 0.375f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.3125f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					// NORTH
					renderer.pos(matrix4f, 0.3125f, 0.625f, 0.375f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.3125f, 0.375f, 0.375f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.0f, 0.375f, 0.375f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.0f, 0.625f, 0.375f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					// SOUTH
					renderer.pos(matrix4f, 0.0f, 0.625f, 0.625f).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.0f, 0.375f, 0.625f).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.3125f, 0.375f, 0.625f).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
					renderer.pos(matrix4f, 0.3125f, 0.625f, 0.625f).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
					break;
				}
			}
//			// Top
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
//
//			// Bottom
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
//
//			// Sides
//			//NORTH
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
//
//			//SOUTH
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
//
//			//WEST
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, margin - TANK_THICKNESS, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
//
//			//EAST
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v1).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, TANK_THICKNESS + offset).color(r, g, b, a).tex(u1, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v2).lightmap(light).endVertex();
//			renderer.pos(matrix4f, TANK_THICKNESS + offset, scale + TANK_THICKNESS, margin - TANK_THICKNESS).color(r, g, b, a).tex(u2, v1).lightmap(light).endVertex();
		}
	}
}
