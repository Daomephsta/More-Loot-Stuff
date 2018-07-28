package leviathan143.morelootstuff.loot.conditions;

import java.util.*;

import com.google.common.collect.Lists;
import com.google.gson.*;

import crafttweaker.CraftTweakerAPI;
import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class InBiome implements LootCondition
{
	private final List<Biome> targetBiomes;

	public InBiome(Iterable<Biome> targetBiomes)
	{
		this.targetBiomes = Lists.newArrayList(targetBiomes);
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		// If there is no looted entity(e.g chests), use the player instead
		Entity entity = context.getLootedEntity() != null ? context.getLootedEntity() : context.getKillerPlayer();
		if (entity == null) return false;
		// The biome the entity is in
		Biome biome = context.getWorld().getBiome(entity.getPosition());
		return targetBiomes.contains(biome);
	}

	public static class Serialiser extends LootCondition.Serializer<InBiome>
	{
		public Serialiser()
		{
			super(new ResourceLocation(MoreLootStuff.MODID, "in_biome"), InBiome.class);
		}

		@Override
		public void serialize(JsonObject json, InBiome condition, JsonSerializationContext context)
		{
			JsonArray biomeNameArray = new JsonArray();
			for (Biome biome : condition.targetBiomes)
			{
				biomeNameArray.add(biome.getRegistryName().toString());
			}
			json.add("biomes", biomeNameArray);
		}

		@Override
		public InBiome deserialize(JsonObject json, JsonDeserializationContext context)
		{
			JsonArray biomeIDs = JsonUtils.getJsonArray(json, "biomes");
			List<Biome> biomes = new ArrayList<>();
			for (JsonElement biomeID : biomeIDs)
			{
				Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(JsonUtils.getString(biomeID, "biome id")));
				if (biome == null) CraftTweakerAPI.logError("Unknown biome type '" + biomeID + "'");
				else biomes.add(biome);
			}
			return new InBiome(biomes);
		}
	}
}
