package leviathan143.morelootstuff;

import leviathan143.morelootstuff.MoreLootStuff.Constants;
import leviathan143.morelootstuff.commands.CommandMoreLootStuff;
import leviathan143.morelootstuff.loot.conditions.InBiome;
import leviathan143.morelootstuff.loot.conditions.InBounds;
import leviathan143.morelootstuff.loot.conditions.gamestages.*;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class MoreLootStuff
{
	public class Constants
	{
		public static final String MODNAME = "More Loot Stuff";
		public static final String MODID = "morelootstuff";
		public static final String VERSION = "0.0.2";
		public static final String DEPENDENCIES = "required-after:forge@[14.23.1.2577,)";
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		//Register conditions
		LootConditionManager.registerCondition(new InBiome.Serialiser());
		LootConditionManager.registerCondition(new InBounds.Serialiser());
		if(Loader.isModLoaded("gamestages"))
		{
			LootConditionManager.registerCondition(new HasAllStages.Serialiser());
			LootConditionManager.registerCondition(new HasAnyOfStages.Serialiser());
			LootConditionManager.registerCondition(new HasStage.Serialiser());
		}
		
		//Register functions
	}
	
	@Mod.EventHandler
	public void serverInit(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandMoreLootStuff());
	}
}
