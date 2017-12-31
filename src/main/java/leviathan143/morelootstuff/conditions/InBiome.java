package leviathan143.morelootstuff.conditions;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff.Constants;
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

	public InBiome(Biome... targetBiomes)
	{
		this.targetBiomes = Lists.newArrayList(targetBiomes);
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		//If there is no looted entity(e.g chests), use the player instead
		Entity entity = context.getLootedEntity() != null ? context.getLootedEntity() : context.getKillerPlayer();  
		if (entity == null) return false;
		//The biome the entity is in
		Biome biome = context.getWorld().getBiome(entity.getPosition());
		return targetBiomes.contains(biome);
	}

	public static class Serialiser extends LootCondition.Serializer<InBiome>
	{
		public Serialiser()
		{
			super(new ResourceLocation(Constants.MODID, "in_biome"), InBiome.class);
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
			JsonArray biomes = JsonUtils.getJsonArray(json, "biomes");
			Biome[] biomeArray = new Biome[biomes.size()];
			for (int e = 0; e < biomes.size(); e++)
			{
				String biomeRL = biomes.get(e).getAsString();
				Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeRL));
				if (biome == null) throw new JsonSyntaxException("Unknown biome '" + biomeRL + "'");
				else biomeArray[e] = biome;
			}
			return new InBiome(biomeArray);
		}
	}
}
