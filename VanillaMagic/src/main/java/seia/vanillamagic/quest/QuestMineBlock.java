package seia.vanillamagic.quest;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class QuestMineBlock extends Quest
{
	public final Block[] blocksToBeMine;

	public QuestMineBlock(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName, 
			Block[] blocksToBeMine) 
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.blocksToBeMine = blocksToBeMine;
	}
	
	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		try
		{
			EntityPlayer player = event.getPlayer();
			if(!player.hasAchievement(achievement))
			{
				Block block = event.getState().getBlock();
				for(int i = 0; i < blocksToBeMine.length; i++)
				{
					if(Block.isEqualTo(block, blocksToBeMine[i]))
					{
						player.addStat(achievement, 1);
						return;
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
}