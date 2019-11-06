package leviathan143.morelootstuff.loot.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import leviathan143.morelootstuff.CommonReflection;
import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.BiomeDictionary;

public class InBiomeOfType implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "in_biome_of_type");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private final List<BiomeDictionary.Type> targetBiomeTypes;

	public InBiomeOfType(Iterable<BiomeDictionary.Type> targetBiomeTypes)
	{
		this.targetBiomeTypes = Lists.newArrayList(targetBiomeTypes);
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		// If there is no looted entity(e.g chests), use the player instead
		Entity entity = context.getLootedEntity();
		if (entity == null)
		{
			LOGGER.debug("No looted entity provided by LootContext, falling back to player.");
			entity = context.getKillerPlayer();
		}
		if (entity == null)
		{
			LOGGER.debug("No player provided by LootContext. Unable to determine biome type, returning false.");
			return false;
		}
		// The biome the entity is in
		Biome biome = context.getWorld().getBiome(entity.getPosition());
		for (BiomeDictionary.Type type : targetBiomeTypes)
		{
			if (BiomeDictionary.hasType(biome, type)) return true;
		}
		return false;
	}

	public static class Serialiser extends LootCondition.Serializer<InBiomeOfType>
	{
		public Serialiser()
		{
			super(ID, InBiomeOfType.class);
		}

		@Override
		public void serialize(JsonObject json, InBiomeOfType condition, JsonSerializationContext context)
		{
			JsonArray typeNameArray = new JsonArray();
			for (BiomeDictionary.Type biomeType : condition.targetBiomeTypes)
			{
				typeNameArray.add(biomeType.getName());
			}
			json.add("types", typeNameArray);
		}

		@Override
		public InBiomeOfType deserialize(JsonObject json, JsonDeserializationContext context)
		{
			Map<String, BiomeDictionary.Type> typeMap = CommonReflection.getTypeMap();
			JsonArray biomeTypeIDs = JsonUtils.getJsonArray(json, "types");
			List<BiomeDictionary.Type> types = new ArrayList<>();
			for (JsonElement typeID : biomeTypeIDs)
			{
				BiomeDictionary.Type biomeType = typeMap.get(JsonUtils.getString(typeID, "biome type"));
				if (biomeType == null) LOGGER.error("Unknown biome type '{}'", typeID);
				else types.add(biomeType);
			}
			return new InBiomeOfType(types);
		}
	}
}
