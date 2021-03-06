package seia.vanillamagic.quest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.AltarChecker;

public class QuestBuildAltar extends Quest
{
	public final int tier;
	
	public QuestBuildAltar(Quest required, int posX, int posY, String questName, String uniqueName, 
			int tier)
	{
		this(required, posX, posY, new ItemStack(Items.CAULDRON), questName, uniqueName,
				tier);
	}
	
	public QuestBuildAltar(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName, 
			int tier)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.tier = tier;
	}
	
	@SubscribeEvent
	public void placeBlock(BlockEvent.PlaceEvent event)
	{
		try
		{
			EntityPlayer player = event.getPlayer();
			BlockPos middlePos = event.getBlockSnapshot().getPos();
			Block middleBlock = event.getPlacedBlock().getBlock();
			if(!player.hasAchievement(achievement))
			{
				if(middleBlock instanceof BlockCauldron)
				{
					BlockSnapshot block = event.getBlockSnapshot();
					if(AltarChecker.checkAltarTier(block.getWorld(), block.getPos(), tier))
					{
						player.addStat(achievement, 1);
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
}