package seia.vanillamagic.utils;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import seia.vanillamagic.quest.portablecraftingtable.ICraftingTable;

public class EntityHelper 
{
	private EntityHelper()
	{
	}
	
	public static boolean hasPlayerCraftingTableInMainHand(EntityPlayer player)
	{
		try
		{
			if(player.getHeldItemMainhand().getItem() != null)
			{
				if(Block.isEqualTo(Block.getBlockFromItem(player.getHeldItemMainhand().getItem()), Blocks.CRAFTING_TABLE))
				{
					return true;
				}
				else if(player.getHeldItemMainhand().getItem() instanceof ICraftingTable)
				{
					return ((ICraftingTable) player.getHeldItemMainhand().getItem()).canOpenGui(player);
				}
				return false;
			}
		}
		catch(Exception e)
		{
		}
		return false;
	}
	
	public static EntityItem copyItem(EntityItem original)
	{
		return new EntityItem(original.worldObj, original.posX, original.posY, original.posZ, original.getEntityItem().copy());
	}
	
	public static Vec3d getEyePosition(EntityPlayer player)
	{
		double posX = player.posX;
		double posY = player.posY;
		double posZ = player.posZ;
		if (player.worldObj.isRemote) 
		{
			posY += player.getEyeHeight() - player.getDefaultEyeHeight();
		} 
		else 
		{
			posY += player.getEyeHeight();
			if(player instanceof EntityPlayerMP && player.isSneaking()) 
			{
				posY -= 0.08;
			}
		}
		return new Vec3d(posX, posY, posZ);
	}
	
	public static Vec3d getLookVec(EntityPlayer player) 
	{
		Vec3d lv = player.getLookVec();
		return new Vec3d(lv.xCoord, lv.yCoord, lv.zCoord);
	}
	
	/**
	 * knocks back "toKnockBack" entity
	 */
	public static void knockBack(Entity user, Entity toKnockBack, float strenght, double xRatio, double zRatio)
	{
		toKnockBack.isAirBorne = true;
		float f = MathHelper.sqrt_double(xRatio * xRatio + zRatio * zRatio);
		toKnockBack.motionX /= 2.0D;
		toKnockBack.motionZ /= 2.0D;
		toKnockBack.motionX -= xRatio / (double)f * (double)strenght;
		toKnockBack.motionZ -= zRatio / (double)f * (double)strenght;
		if(toKnockBack.onGround)
		{
			toKnockBack.motionY /= 2.0D;
			toKnockBack.motionY += (double)strenght;
			if(toKnockBack.motionY > 0.4000000059604645D)
			{
				toKnockBack.motionY = 0.4000000059604645D;
			}
		}
	}
	
	/**
	 * knocks back "toKnockBack" entity
	 */
	public static void knockBack(Entity user, Entity toKnockBack, float strenght)
	{
		double xRatio = user.posX - toKnockBack.posX;
		double zRatio = user.posZ - toKnockBack.posZ;
		knockBack(user, toKnockBack, strenght, xRatio, zRatio);
	}
	
	public static void addChatComponentMessage(EntityPlayer player, String msg)
	{
		player.addChatComponentMessage(new TextComponentString(msg));
	}

	public static void removeEntities(World world, List<EntityItem> itemsInCauldron)
	{
		for(EntityItem ei : itemsInCauldron)
		{
			world.removeEntity(ei);
		}
	}
}