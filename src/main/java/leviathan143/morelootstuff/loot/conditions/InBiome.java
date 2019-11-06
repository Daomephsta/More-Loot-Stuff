package leviathan143.morelootstuff.loot.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import leviathan143.morelootstuff.MoreLootStuff;
import leviathan143.morelootstuff.loot.TargetSelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class InBiome implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "in_biome");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private final List<Biome> targetBiomes;
    private final TargetSelector targetSelector;

	public InBiome(Iterable<Biome> targetBiomes, TargetSelector targetSelector)
	{
		this.targetBiomes = Lists.newArrayList(targetBiomes);
        this.targetSelector = targetSelector;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
	    Entity reference = targetSelector.get(context);
        if (reference == null)
        {
			LOGGER.debug("LootContext has no {}. Unable to determine biome, returning false.", targetSelector);
			return false;
		}
		// The biome the entity is in
		Biome biome = context.getWorld().getBiome(reference.getPosition());
		return targetBiomes.contains(biome);
	}

	public static class Serialiser extends LootCondition.Serializer<InBiome>
	{
		public Serialiser()
		{
			super(ID, InBiome.class);
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
            json.add("target", condition.targetSelector.toJson());
		}

		@Override
		public InBiome deserialize(JsonObject json, JsonDeserializationContext context)
		{
			JsonArray biomeIDs = JsonUtils.getJsonArray(json, "biomes");
			List<Biome> biomes = new ArrayList<>();
			for (JsonElement biomeID : biomeIDs)
			{
				Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(JsonUtils.getString(biomeID, "biome id")));
				if (biome == null) LOGGER.error("Unknown biome type '{}'", biomeID);
				else biomes.add(biome);
			}
            TargetSelector targetSelector = json.has("target")
                ? TargetSelector.fromJson(json, "target")
                : TargetSelector.OLD_BEHAVIOUR;
			return new InBiome(biomes, targetSelector);
		}
	}
}
