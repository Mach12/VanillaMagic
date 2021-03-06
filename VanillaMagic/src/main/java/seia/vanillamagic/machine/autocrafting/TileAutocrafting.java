package seia.vanillamagic.machine.autocrafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.machine.TileMachine;

public class TileAutocrafting extends TileMachine
{
	public static final String REGISTRY_NAME = TileAutocrafting.class.getSimpleName();
	
	public ContainerAutocrafting container;
	
	private int currentlyCraftingSlot = 0;
	
	private final int defaultCraftingSlot = 0;
	
	public void init(World world, BlockPos machinePos)
	{
		super.init(world, machinePos);
		this.oneOperationCost = 100; // 1 Coal = 16 crafting operations ?
	}
	
	public void initContainer()
	{
		BlockPos[][] inventoryPosMatrix = QuestAutocrafting.buildInventoryMatrix(getPos());
		IInventory[][] inventoryMatrix = QuestAutocrafting.buildIInventoryMatrix(worldObj, inventoryPosMatrix);
		ItemStack[][] stackMatrix = QuestAutocrafting.buildStackMatrix(inventoryMatrix, currentlyCraftingSlot);
		container = new ContainerAutocrafting(worldObj, stackMatrix);
	}
	
	public IInventory getInputInventory() 
	{
		return ((IInventory) worldObj.getTileEntity(getMachinePos().up()));
	}
	
	public IInventory getOutputInventory() 
	{
		return ((IInventory) worldObj.getTileEntity(getMachinePos().down(2)));
	}
	
	public boolean checkSurroundings() 
	{
		return QuestAutocrafting.isConstructionComplete(this.worldObj, getMachinePos());
	}
	
	public void doWork() 
	{
		if(inventoryOutputHasSpace())
		{
			initContainer();
			for(int i = 0; i < 4; i++)
			{
				boolean crafted = container.craft();
				if(crafted)
				{
					container.outputResult(getHopperForStructure());
					container.removeStacks(
							QuestAutocrafting.buildIInventoryMatrix(worldObj, 
									QuestAutocrafting.buildInventoryMatrix(getMachinePos())));
					return;
				}
				container.rotateMatrix();
			}
			decreaseTicks();
		}
	}
	
	public EnumFacing getOutputFacing() 
	{
		return EnumFacing.DOWN;
	}
	
	public IHopper getHopperForStructure()
	{
		BlockPos hopperPos = getMachinePos().down(2);
		TileEntity hopperTile = worldObj.getTileEntity(hopperPos);
		if(hopperTile instanceof IHopper)
		{
			return (IHopper) hopperTile;
		}
		return null;
	}
}