package com.withertech.processing.blocks.furnace;

import com.withertech.processing.Processing;
import com.withertech.processing.blocks.AbstractMachineScreen;
import com.withertech.processing.util.MachineTier;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FurnaceScreen extends AbstractMachineScreen<FurnaceContainer>
{
	public final ResourceLocation TEXTURE;

	public FurnaceScreen(FurnaceContainer containerIn, PlayerInventory playerInventory, ITextComponent titleIn)
	{
		super(containerIn, playerInventory, titleIn);
		MachineTier tier = containerIn.getTileEntity().getMachineTier();
		switch (tier)
		{
			case BASIC:
				TEXTURE = Processing.getId("textures/gui/basic_furnace.png");
				break;
			case ADVANCED:
				TEXTURE = Processing.getId("textures/gui/advanced_furnace.png");
				break;
			case ELITE:
				TEXTURE = Processing.getId("textures/gui/elite_furnace.png");
				break;
			case ULTIMATE:
				TEXTURE = Processing.getId("textures/gui/ultimate_furnace.png");
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
		return guiPosX + 79;
	}

	@Override
	protected int getProgressArrowPosY(int guiPosY)
	{
		return guiPosY + 35;
	}
}
