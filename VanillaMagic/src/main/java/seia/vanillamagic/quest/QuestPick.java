package seia.vanillamagic.quest;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

public class QuestPick extends Quest
{
	public final ItemStack whatToPick;
	
	public QuestPick(Quest required, int posX, int posY, String questName, String uniqueName,
			ItemStack whatToPick)
	{
		this(required, posX, posY, whatToPick, questName, uniqueName,
				whatToPick);
	}
	
	public QuestPick(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName,
			ItemStack whatToPick)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.whatToPick = whatToPick;
	}
	
	@SubscribeEvent
	public void pickItem(ItemPickupEvent event)
	{
		try
		{
			EntityPlayer player = event.player;
			if(!player.hasAchievement(achievement))
			{
				EntityItem onGround = event.pickedUp;
				if(whatToPick.getItem().equals(onGround.getEntityItem().getItem()))
				{
					player.addStat(achievement, 1);
				}
			}
		}
		catch(Exception e)
		{
		}
	}
}