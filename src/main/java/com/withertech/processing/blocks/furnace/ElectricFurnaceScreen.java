package com.withertech.processing.blocks.furnace;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.AbstractMachineScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ElectricFurnaceScreen extends AbstractMachineScreen<ElectricFurnaceContainer>
{
	public final ResourceLocation TEXTURE;

	public ElectricFurnaceScreen(ElectricFurnaceContainer containerIn, PlayerInventory playerInventory, ITextComponent titleIn)
	{
		super(containerIn, playerInventory, titleIn);
		switch (containerIn.getTileEntity().getMachineTier())
		{
			case BASIC:
				TEXTURE = Processing.getId("textures/gui/basic_furnace.png");
				break;
			case STANDARD:
				TEXTURE = Processing.getId("textures/gui/furnace.png");
				break;
			default:
				TEXTURE = Processing.getId("textures/gui/furnace.png");
		}

	}

	@Override
	public ResourceLocation getGuiTexture()
	{
		return this.TEXTURE;
	}

	@Override
	protected int getProgressArrowPosX(int guiPosX)
	{
		return guiPosX + 79;
	}

	@Override
	protected int getProgressArrowPosY(int guiPosY)
	{
		return guiPosY + 35;
	}
}
