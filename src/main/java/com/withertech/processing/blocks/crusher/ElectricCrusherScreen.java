package com.withertech.processing.blocks.crusher;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.AbstractMachineScreen;
import com.withertech.processing.util.MachineTier;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ElectricCrusherScreen extends AbstractMachineScreen<ElectricCrusherContainer>
{
	public static final ResourceLocation JEI_TEXTURE = Processing.getId("textures/gui/crusher.png");;
	public final ResourceLocation TEXTURE;

	public ElectricCrusherScreen(ElectricCrusherContainer containerIn, PlayerInventory playerInventoryIn, ITextComponent titleIn)
	{
		super(containerIn, playerInventoryIn, titleIn);
		MachineTier tier = containerIn.getTileEntity().getMachineTier();
		switch (tier)
		{
			case BASIC:
				TEXTURE = Processing.getId("textures/gui/basic_crusher.png");
				break;
			case ADVANCED:
				TEXTURE = Processing.getId("textures/gui/advanced_crusher.png");
				break;
			case ULTIMATE:
				TEXTURE = Processing.getId("textures/gui/ultimate_crusher.png");
				break;
			default:
				throw new IllegalArgumentException("Unknown MachineTier: " + tier);
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
		return guiPosX + 49;
	}

	@Override
	protected int getProgressArrowPosY(int guiPosY)
	{
		return guiPosY + 34;
	}
}
