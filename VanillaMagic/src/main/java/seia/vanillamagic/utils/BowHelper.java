package seia.vanillamagic.utils;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class BowHelper 
{
	private BowHelper()
	{
	}
	
	public static boolean isArrow(@Nullable ItemStack stack)
	{
		return stack != null && stack.getItem() instanceof ItemArrow;
	}
	
	public static ItemStack findAmmo(EntityPlayer player)
	{
		if (isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);
				if (isArrow(itemstack))
				{
					return itemstack;
				}
			}
			return null;
		}
	}
}