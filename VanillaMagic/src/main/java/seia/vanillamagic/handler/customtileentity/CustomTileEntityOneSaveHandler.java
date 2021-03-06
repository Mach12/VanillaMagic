package seia.vanillamagic.handler.customtileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.WorldHelper;

public class CustomTileEntityOneSaveHandler
{
	protected final Map<Integer, List<TileEntity>> loadedTileEntities;
	public final Map<Integer, List<TileEntity>> readedTileEntities;
	
	public CustomTileEntityOneSaveHandler()
	{
		loadedTileEntities = new HashMap<Integer, List<TileEntity>>();
		readedTileEntities = new HashMap<Integer, List<TileEntity>>();
	}
	
	public void clearTileEntities()
	{
		loadedTileEntities.clear();
	}
	
	public void clearTileEntitiesForDimension(int dimension)
	{
		loadedTileEntities.get(dimension).clear();
	}
	
	/**
	 * customTileEntity MUST implements {@link ITickable}
	 */
	public boolean addCustomTileEntity(TileEntity customTileEntity, int dimension)
	{
		if(loadedTileEntities.containsKey(dimension))
		{
			List<TileEntity> entitiesInDim = loadedTileEntities.get(dimension);
			for(TileEntity tile : entitiesInDim)
			{
				if(BlockPosHelper.isSameBlockPos(customTileEntity.getPos(), tile.getPos()))
				{
					BlockPosHelper.printCoords(Level.WARN, "There is already CustomTileEntity (" + tile.getClass().getSimpleName() + ") at pos:", tile.getPos());
					return false;
				}
			}
			entitiesInDim.add(customTileEntity);
			add(customTileEntity);
			return true;
		}
		else
		{
			loadedTileEntities.put(new Integer(dimension), new ArrayList<TileEntity>());
			VanillaMagic.logger.log(Level.INFO, "Registered CustomTileEntityHandler for Dimension: " + dimension);
			loadedTileEntities.get(dimension).add(customTileEntity);
			add(customTileEntity);
			return true;
		}
	}
	
	private void add(TileEntity customTileEntity)
	{
		customTileEntity.getWorld().setTileEntity(customTileEntity.getPos(), customTileEntity);
		//customTileEntity.getWorld().tickableTileEntities.add(customTileEntity); // TODO:
		BlockPosHelper.printCoords(Level.INFO, "CustomTileEntity (" + customTileEntity.getClass().getSimpleName() + ") added at pos:", customTileEntity.getPos());
		customTileEntity.getWorld().updateEntities();
	}
	
	/**
	 * TileEntity at position "pos" MUST implements {@link IDimensionKeeper}
	 */
	public boolean removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		int dimID = WorldHelper.getDimensionID(world);
		Set<Entry<Integer, List<TileEntity>>> entrySet = loadedTileEntities.entrySet();
		if(entrySet.size() > 0) // should be 3 (Dims: -1, 0, 1, and more)
		{
			for(Entry<Integer, List<TileEntity>> entry : entrySet)
			{
				if(entry.getKey().intValue() == dimID)
				{
					return removeCustomTileEntityAtPos(world, pos, dimID);
				}
			}
			BlockPosHelper.printCoords(Level.WARN, "Didn't found the TileEntity at pos:", pos);
		}
		return false;
	}
	
	public boolean removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		List<TileEntity> tilesInDimension = loadedTileEntities.get(dimension);
		for(int i = 0; i < tilesInDimension.size(); i++)
		{
			TileEntity tileInDim = tilesInDimension.get(i);
			if(BlockPosHelper.isSameBlockPos(tileInDim.getPos(), pos))
			{
				BlockPosHelper.printCoords(Level.INFO, "Removed CustomTileEntity (" + tileInDim.getClass().getSimpleName() + ") at:", pos);
				tilesInDimension.remove(i);
				for(int j = 0; j < world.tickableTileEntities.size(); j++)
				{
					if(BlockPosHelper.isSameBlockPos(world.tickableTileEntities.get(j).getPos(), tileInDim.getPos()))
					{
						world.tickableTileEntities.remove(j);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Integer[] getIDs()
	{
		return loadedTileEntities.keySet().toArray(new Integer[loadedTileEntities.size()]);
	}
	
	public List<TileEntity> getCustomEntitiesInDimension(int dimension)
	{
		List<TileEntity> tiles = loadedTileEntities.get(dimension);
		if(tiles == null)
		{
			loadedTileEntities.put(new Integer(dimension), new ArrayList<TileEntity>());
		}
		return loadedTileEntities.get(dimension);
	}

	public void moveTilesFromReadded(int dimension) 
	{
		loadedTileEntities.get(dimension).addAll(readedTileEntities.get(dimension));
		readedTileEntities.get(dimension).clear();
	}

	public List<TileEntity> getReadedTileEntitiesForDimension(int dimension) 
	{
		List<TileEntity> tiles = readedTileEntities.get(dimension);
		if(tiles == null)
		{
			readedTileEntities.put(new Integer(dimension), new ArrayList<TileEntity>());
		}
		return readedTileEntities.get(dimension);
	}

	public boolean addReadedTile(TileEntity tileEntity, int dimension) 
	{
		if(readedTileEntities.containsKey(dimension))
		{
			List<TileEntity> entitiesInDim = readedTileEntities.get(dimension);
			for(TileEntity tile : entitiesInDim)
			{
				if(BlockPosHelper.isSameBlockPos(tileEntity.getPos(), tile.getPos()))
				{
					BlockPosHelper.printCoords(Level.WARN, "There is already ReadedTileEntity (" + tile.getClass().getSimpleName() + ") at pos:", tile.getPos());
					return false;
				}
			}
			entitiesInDim.add(tileEntity);
			return true;
		}
		else
		{
			readedTileEntities.put(new Integer(dimension), new ArrayList<TileEntity>());
			VanillaMagic.logger.log(Level.INFO, "Registered ReadedTileEntityHandler for Dimension: " + dimension);
			readedTileEntities.get(dimension).add(tileEntity);
			return true;
		}
	}

	@Nullable
	public TileEntity getCustomTileEntity(BlockPos tilePos, int dimension) 
	{
		List<TileEntity> tiles = loadedTileEntities.get(dimension);
		for(TileEntity tile : tiles)
		{
			if(BlockPosHelper.isSameBlockPos(tilePos, tile.getPos()))
			{
				return tile;
			}
		}
		return null;
	}
}