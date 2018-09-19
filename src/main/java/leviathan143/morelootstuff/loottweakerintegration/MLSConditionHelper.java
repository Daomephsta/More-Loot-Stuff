package leviathan143.morelootstuff.loottweakerintegration;

import java.util.*;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import leviathan143.loottweaker.common.LootTweakerMain;
import leviathan143.loottweaker.common.zenscript.ZenLootConditionWrapper;
import leviathan143.morelootstuff.CommonReflection;
import leviathan143.morelootstuff.loot.conditions.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion(LootTweakerMain.Constants.MODID + ".vanilla.loot.ModConditions")
public class MLSConditionHelper 
{
	@ZenMethod
	public static ZenLootConditionWrapper inBiome(String[] biomeIDs)
	{	
		List<Biome> biomes = new ArrayList<>();
		for (String biomeID : biomeIDs)
		{
			Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeID));
			if (biome == null) CraftTweakerAPI.logError("Unknown biome type '" + biomeID + "'");
			else biomes.add(biome);
		}
		return wrap(new InBiome(biomes));
	}

	@ZenMethod
	public static ZenLootConditionWrapper inBiomeOfType(String[] biomeTypeIDs)
	{	
		Map<String, BiomeDictionary.Type> typeMap = CommonReflection.getTypeMap();
		List<BiomeDictionary.Type> types = new ArrayList<>();
		for (String typeID : biomeTypeIDs)
		{
			BiomeDictionary.Type biomeType = typeMap.get(typeID);
			if (biomeType == null) CraftTweakerAPI.logError("Unknown biome type '" + typeID + "'");
			else types.add(biomeType);
		}
		return wrap(new InBiomeOfType(types));
	}

	@ZenMethod
	public static ZenLootConditionWrapper inDimension(int dimensionID)
	{
		return wrap(new InDimension(dimensionID));
	}

	@ZenMethod
	public static ZenLootConditionWrapper killedByRealPlayer(boolean inverse)
	{
		return wrap(new KilledByRealPlayer(inverse));
	}
	
	private static ZenLootConditionWrapper wrap(LootCondition condition)
	{
		return new ZenLootConditionWrapper(condition);
	}
}
