package com.withertech.processing.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.withertech.processing.api.RedstoneMode;
import com.withertech.processing.client.button.RedstoneModeButton;
import com.withertech.processing.inventory.SlotMachineUpgrade;
import com.withertech.processing.network.Network;
import com.withertech.processing.network.SetRedstoneModePacket;
import com.withertech.processing.util.TextUtil;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public abstract class AbstractMachineBaseScreen<C extends AbstractMachineBaseContainer<?>> extends ContainerScreen<C>
{
	public AbstractMachineBaseScreen(C screenContainer, PlayerInventory inv, ITextComponent titleIn)
	{
		super(screenContainer, inv, titleIn);
	}

	public abstract ResourceLocation getGuiTexture();

	@Override
	protected void init()
	{
		super.init();
		this.addButton(new RedstoneModeButton(container, this.guiLeft - 16, this.guiTop, 16, 16, button ->
		{
			RedstoneMode mode = ((RedstoneModeButton) button).getMode();
			Network.channel.sendToServer(new SetRedstoneModePacket(mode));
		}));
	}

	@Override
	protected void renderHoveredTooltip(@Nonnull MatrixStack matrixStack, int x, int y)
	{
		if (isPointInRegion(153, 17, 13, 51, x, y))
		{
			IFormattableTextComponent text = TextUtil.energyWithMax(container.getEnergyStored(), container.getMaxEnergyStored());
			renderTooltip(matrixStack, text, x, y);
		}
		if (hoveredSlot instanceof SlotMachineUpgrade && !hoveredSlot.getHasStack())
		{
			renderTooltip(matrixStack, TextUtil.translate("misc", "upgradeSlot"), x, y);
		}
		super.renderHoveredTooltip(matrixStack, x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y)
	{
		if (minecraft == null) return;
		//noinspection deprecation
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(getGuiTexture());
		int xPos = (this.width - this.xSize) / 2;
		int yPos = (this.height - this.ySize) / 2;
		blit(matrixStack, xPos, yPos, 0, 0, this.xSize, this.ySize);

		// Upgrade slots
		for (int i = 0; i < this.container.tileEntity.tier.getUpgradeSlots(); ++i)
		{
			blit(matrixStack, xPos + 5 + 18 * i, yPos - 11, 190, 0, 18, 14);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y)
	{
		this.font.drawString(matrixStack, this.title.getString(), 8.0F, 6.0F, 4210752);
//        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);

		for (Widget widget : this.buttons)
		{
			if (widget.isHovered() && widget instanceof RedstoneModeButton)
			{
				RedstoneMode mode = ((RedstoneModeButton) widget).getMode();
				renderTooltip(matrixStack, TextUtil.translate("misc", "redstoneMode", mode.name()), x - guiLeft, y - guiTop);
			}
		}
	}
}
