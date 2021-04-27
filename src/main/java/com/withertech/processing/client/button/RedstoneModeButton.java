package com.withertech.processing.client.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.withertech.processing.api.RedstoneMode;
import com.withertech.processing.blocks.AbstractMachineBaseContainer;
import com.withertech.processing.util.EnumUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class RedstoneModeButton extends Button
{
	private final AbstractMachineBaseContainer<?> container;

	public RedstoneModeButton(AbstractMachineBaseContainer<?> container, int x, int y, int width, int height, IPressable onPress)
	{
		super(x, y, width, height, new StringTextComponent(""), button ->
		{
			((RedstoneModeButton) button).cycleMode();
			onPress.onPress(button);
		});
		this.container = container;
	}

	public RedstoneMode getMode()
	{
		return container.getRedstoneMode();
	}

	private void cycleMode()
	{
		int ordinal = container.getRedstoneMode().ordinal() + 1;
		if (ordinal >= RedstoneMode.values().length)
			ordinal = 0;
		container.setRedstoneMode(EnumUtils.byOrdinal(ordinal, RedstoneMode.IGNORED));
	}

	@Override
	public void renderWidget(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getTextureManager().bindTexture(container.getRedstoneMode().getTexture());
		GlStateManager.disableDepthTest();

		blit(matrixStack, this.x, this.y, 0, 0, this.width, this.height, 16, 16);
		GlStateManager.enableDepthTest();
	}
}
