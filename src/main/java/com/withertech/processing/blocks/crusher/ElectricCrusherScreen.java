package com.withertech.processing.blocks.crusher;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.AbstractMachineScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ElectricCrusherScreen extends AbstractMachineScreen<ElectricCrusherContainer>
{
	public static final ResourceLocation TEXTURE = Processing.getId("textures/gui/crusher.png");

	public ElectricCrusherScreen(ElectricCrusherContainer containerIn, PlayerInventory playerInventoryIn, ITextComponent titleIn)
	{
		super(containerIn, playerInventoryIn, titleIn);
	}

	@Override
	public ResourceLocation getGuiTexture()
	{
		return TEXTURE;
	}

	@Override
	protected int getProgressArrowPosX(int guiPosX)
	{
		return guiPosX + 49;
	}

	@Override
	protected int getProgressArrowPosY(int guiPosY)
	{
		return guiPosY + 34;
	}
}
