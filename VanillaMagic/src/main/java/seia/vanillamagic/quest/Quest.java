package seia.vanillamagic.quest;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

/** 
 * Base class for all the quests.
 */
public abstract class Quest
{
	public final Achievement achievement;
	public final Achievement required;
	public final Quest requiredQuest;
	public final int posX, posY;
	public final ItemStack icon;
	public final String questName, uniqueName;
	
	@Nullable
	public Quest[] additionalRequiredQuests;
	
	private Quest()
	{
		achievement = null;
		required = null;
		requiredQuest = null;
		posX = 0;
		posY = 0;
		icon = null;
		questName = "";
		uniqueName = "";
		additionalRequiredQuests = null;
	}
	
	public Quest(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName)
	{
		this(required, posX, posY, icon, questName, uniqueName, null);
	}
	
	public Quest(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName, Quest[] additionalRequiredQuests)
	{
		this.requiredQuest = required;
		this.required = required.achievement;
		this.posX = required.posX + posX;
		this.posY = required.posY + posY;
		this.icon = icon;
		this.questName = questName;
		this.uniqueName = uniqueName;
		this.additionalRequiredQuests = additionalRequiredQuests;
		
		this.achievement = new Achievement(this.questName, 
				this.uniqueName, 
				this.posX, 
				this.posY, 
				this.icon, 
				this.required)
				.registerStat();
		
		QuestList.QUESTS.add(this);
	}
	
	/**
	 * This is only for the first Achievement.
	 * Try to use the other Constructor.
	 */
	public Quest(Achievement required, int posX, int posY, ItemStack icon, String questName, String uniqueName)
	{
		this.requiredQuest = null;
		this.required = required;
		this.posX = 0 + posX;
		this.posY = 0 + posY;
		this.icon = icon;
		this.questName = questName;
		this.uniqueName = uniqueName;
		this.additionalRequiredQuests = null;
		
		this.achievement = new Achievement(this.questName, 
				this.uniqueName, 
				this.posX, 
				this.posY, 
				this.icon, 
				this.required)
				.registerStat();
		
		QuestList.QUESTS.add(this);
	}
	
	public boolean canPlayerGetAchievement(EntityPlayer player)
	{
		if(player.hasAchievement(required))
		{
			// Player need additional quests to be completed.
			if(additionalRequiredQuests != null)
			{
				for(Quest quest : additionalRequiredQuests)
				{
					if(!player.hasAchievement(quest.achievement))
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean hasAdditionalQuests()
	{
		return this.additionalRequiredQuests != null;
	}
	
	public boolean finishedAdditionalQuests(EntityPlayer player)
	{
		if(hasAdditionalQuests())
		{
			for(Quest quest : additionalRequiredQuests)
			{
				if(!player.hasAchievement(quest.achievement))
				{
					return false;
				}
			}
		}
		return true;
	}
}