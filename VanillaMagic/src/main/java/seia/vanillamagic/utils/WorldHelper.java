package seia.vanillamagic.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldHelper
{
	private WorldHelper()
	{
	}
	
	public static int getDimensionID(World world)
	{
		return world.provider.getDimension();
	}
	
	public static int getDimensionID(EntityPlayer player)
	{
		return player.dimension;
	}
	
	public static int getDimensionID(TileEntity tile)
	{
		return getDimensionID(tile.getWorld());
	}
	
	public static int getDimensionID(WorldEvent event)
	{
		return getDimensionID(event.getWorld());
	}
}