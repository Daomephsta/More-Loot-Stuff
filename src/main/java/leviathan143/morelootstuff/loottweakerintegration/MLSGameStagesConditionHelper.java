package leviathan143.morelootstuff.loottweakerintegration;

import java.util.Arrays;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.zenscript.ZenLootConditionWrapper;
import leviathan143.morelootstuff.loot.conditions.gamestages.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ModOnly("gamestages")
@ZenExpansion(LootTweakerMain.Constants.MODID + ".vanilla.loot.ModConditions")
public class MLSGameStagesConditionHelper 
{
	@ZenMethod
	public static ZenLootConditionWrapper hasAllStages(String[] stages)
	{
		return wrap(new HasAllStages(Arrays.asList(stages)));
	}
	
	@ZenMethod
	public static ZenLootConditionWrapper hasAnyOfStages(String[] stages)
	{
		return wrap(new HasAnyOfStages(Arrays.asList(stages)));
	}
	
	@ZenMethod
	public static ZenLootConditionWrapper hasStage(String stage)
	{
		return wrap(new HasStage(stage));
	}
	
	@ZenMethod
	private static ZenLootConditionWrapper wrap(LootCondition condition)
	{
		return new ZenLootConditionWrapper(condition);
	}
}
