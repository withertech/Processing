package com.withertech.processing.init;

import com.withertech.processing.Processing;
import com.withertech.processing.items.ChassisBlockItem;
import com.withertech.processing.items.FactoryBlockItem;
import com.withertech.processing.items.ResourceBlockItem;
import com.withertech.processing.items.ResourceItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class ModGroups
{
	public static final ItemGroup MISC_ITEM_GROUP = new ItemGroup(Processing.MODID + ".misc")
	{
		@Nonnull
		@Override
		public ResourceLocation getTabsImage()
		{
			return Processing.getId("textures/gui/tabs.png");
		}
		@Override
		public int getLabelColor()
		{
			return TextFormatting.DARK_GREEN.getColor();
		}
		@Nonnull
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModItems.HAMMER.get());
		}
	};
	public static final ItemGroup MACHINES_ITEM_GROUP = new ItemGroup(Processing.MODID + ".machines")
	{
		@Override
		public boolean hasSearchBar()
		{
			return true;
		}

		@Override
		public int getLabelColor()
		{
			return TextFormatting.DARK_GREEN.getColor();
		}

		@Override
		public int getSearchbarWidth()
		{
			return super.getSearchbarWidth() - (2*18);
		}

		@Nonnull
		@Override
		public ResourceLocation getTabsImage()
		{
			return Processing.getId("textures/gui/tabs.png");
		}

		@Override
		public void fill(@Nonnull NonNullList<ItemStack> items)
		{
			super.fill(items);
			items.sort((o1, o2) ->
			{
				String o1n = o1.getDisplayName().getString();
				String o2n = o2.getDisplayName().getString();
				if (o1.getItem() instanceof FactoryBlockItem)
				{
					o1n = ((FactoryBlockItem)o1.getItem()).getTier().name();
				}
				if (o1.getItem() instanceof ChassisBlockItem)
				{
					o1n = ((ChassisBlockItem)o1.getItem()).getTier().name();
				}
				if (o2.getItem() instanceof FactoryBlockItem)
				{
					o2n = ((FactoryBlockItem)o2.getItem()).getTier().name();
				}
				if (o2.getItem() instanceof ChassisBlockItem)
				{
					o2n = ((ChassisBlockItem)o2.getItem()).getTier().name();
				}
				if(o1n.compareToIgnoreCase(o2n) == 0)
				{
					return o1.getDisplayName().getString().compareToIgnoreCase(o2.getDisplayName().getString());
				}
				return o1n.compareToIgnoreCase(o2n);
			});
		}

		@Nonnull
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModItems.WRENCH.get());
		}
	};
	public static final ItemGroup MATERIALS_ITEM_GROUP = new ItemGroup(Processing.MODID + ".materials")
	{
		@Override
		public boolean hasSearchBar()
		{
			return true;
		}

		@Override
		public int getSearchbarWidth()
		{
			return super.getSearchbarWidth() - (2*18);
		}
		@Override
		public int getLabelColor()
		{
			return TextFormatting.DARK_GREEN.getColor();
		}
		@Nonnull
		@Override
		public ResourceLocation getTabsImage()
		{
			return Processing.getId("textures/gui/tabs.png");
		}

		@Override
		public void fill(@Nonnull NonNullList<ItemStack> items)
		{
			super.fill(items);
			items.sort((o1, o2) ->
			{
				String o1n = o1.getDisplayName().getString();
				String o2n = o2.getDisplayName().getString();
				if (o1.getItem() instanceof ResourceItem)
				{
					o1n = ((ResourceItem)o1.getItem()).getType().name();
				}
				if (o1.getItem() instanceof ResourceBlockItem)
				{
					o1n = ((ResourceBlockItem)o1.getItem()).getType().name();
				}
				if (o2.getItem() instanceof ResourceItem)
				{
					o2n = ((ResourceItem)o2.getItem()).getType().name();
				}
				if (o2.getItem() instanceof ResourceBlockItem)
				{
					o2n = ((ResourceBlockItem)o2.getItem()).getType().name();
				}
				if(o1n.compareToIgnoreCase(o2n) == 0)
				{
					return o1.getDisplayName().getString().compareToIgnoreCase(o2.getDisplayName().getString());
				}
				return o1n.compareToIgnoreCase(o2n);
			});


		}

		@Nonnull
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ModMetals.COPPER.getGear().get());
		}
	};
	static
	{
		MACHINES_ITEM_GROUP.setBackgroundImage(Processing.getId( "textures/gui/creative_tab_searchable.png"));
		MATERIALS_ITEM_GROUP.setBackgroundImage(Processing.getId( "textures/gui/creative_tab_searchable.png"));
		MISC_ITEM_GROUP.setBackgroundImage(Processing.getId( "textures/gui/creative_tab.png"));
	}
}
