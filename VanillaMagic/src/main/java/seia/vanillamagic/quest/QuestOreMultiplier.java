package seia.vanillamagic.quest;

import java.util.List;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.EntityHelper;
import seia.vanillamagic.utils.OreMultiplierChecker;
import seia.vanillamagic.utils.SmeltingHelper;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestOreMultiplier extends Quest
{
	public final int multiplier;
	public final EnumWand requiredMinimalWand;
	
	public QuestOreMultiplier(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName,
			int multiplier, EnumWand requiredMinimalWand) 
	{
		super(required, posX, posY, icon, questName, uniqueName);
		this.multiplier = multiplier;
		this.requiredMinimalWand = requiredMinimalWand;
	}
	
	@SubscribeEvent
	public void doubleOre(RightClickBlock event)
	{
		try
		{
			EntityPlayer player = event.getEntityPlayer();
			BlockPos cauldronPos = event.getPos();
			
			// player has got required wand in hand
			if(EnumWand.isWandInMainHandRight(player, requiredMinimalWand.wandTier))
			{
				// check if player has the "fuel" in offHand
				ItemStack fuelOffHand = player.getHeldItemOffhand();
				if(SmeltingHelper.isItemFuel(fuelOffHand))
				{
					World world = player.worldObj;
					// is right-clicking on Cauldron
					if(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
					{
						// is altair build correct
						if(OreMultiplierChecker.check(world, cauldronPos))
						{
							List<EntityItem> oresInCauldron = SmeltingHelper.getOresInCauldron(world, cauldronPos);
							if(oresInCauldron.size() > 0)
							{
								multiply(player, oresInCauldron, cauldronPos);
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	public void multiply(EntityPlayer player, List<EntityItem> oresInCauldron, BlockPos cauldronPos)
	{
		List<EntityItem> smeltingResult = SmeltingHelper.countAndSmelt(player, oresInCauldron, cauldronPos.offset(EnumFacing.UP), this, false);
		if(smeltingResult != null)
		{
			World world = player.worldObj;
			for(int i = 0; i < multiplier; i++)
			{
				for(int j = 0; j < smeltingResult.size(); j++)
				{
					world.spawnEntityInWorld(EntityHelper.copyItem(smeltingResult.get(j)));
				}
			}
			world.updateEntities();
		}
	}
}