package leviathan143.morelootstuff.loot.conditions.gamestages;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class HasAnyOfStages implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "has_any_of_stages");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private final List<String> stages;

	public HasAnyOfStages(Iterable<String> stages)
	{
		this.stages = Lists.newArrayList(stages);
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		if (context.getKillerPlayer() == null) 
		{
			LOGGER.debug("No player provided by LootContext. Unable to determine granted stages, returning false.");
			return false;
		}
		return GameStageHelper.hasAnyOf((EntityPlayer) context.getKillerPlayer(), stages);
	}

	public static class Serialiser extends LootCondition.Serializer<HasAnyOfStages>
	{
		public Serialiser()
		{
			super(ID, HasAnyOfStages.class);
		}

		@Override
		public void serialize(JsonObject json, HasAnyOfStages value, JsonSerializationContext context)
		{
			JsonArray stageArray = new JsonArray();
			for (String stage : value.stages)
			{
				stageArray.add(stage);
			}
			json.add("stages", stageArray);
		}

		@Override
		public HasAnyOfStages deserialize(JsonObject json, JsonDeserializationContext context)
		{
			Collection<String> stages = Streams.stream(JsonUtils.getJsonArray(json, "stages"))
				.map(je -> JsonUtils.getString(je, "stage"))
				.collect(Collectors.toList());
			return new HasAnyOfStages(stages);
		}
	}
}
