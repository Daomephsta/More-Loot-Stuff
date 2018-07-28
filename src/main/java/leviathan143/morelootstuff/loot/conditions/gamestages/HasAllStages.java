package leviathan143.morelootstuff.loot.conditions.gamestages;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class HasAllStages implements LootCondition
{
	private final List<String> stages;

	public HasAllStages(Iterable<String> stages)
	{
		this.stages = Lists.newArrayList(stages);
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		if (context.getKillerPlayer() == null) return false;
		return PlayerDataHandler.getStageData((EntityPlayer) context.getKillerPlayer()).hasUnlockedAll(stages);
	}

	public static class Serialiser extends LootCondition.Serializer<HasAllStages>
	{
		public Serialiser()
		{
			super(new ResourceLocation(MoreLootStuff.MODID, "has_all_stages"), HasAllStages.class);
		}

		@Override
		public void serialize(JsonObject json, HasAllStages value, JsonSerializationContext context)
		{
			JsonArray stageArray = new JsonArray();
			for (String stage : value.stages)
			{
				stageArray.add(stage);
			}
			json.add("stages", stageArray);
		}

		@Override
		public HasAllStages deserialize(JsonObject json, JsonDeserializationContext context)
		{
			Collection<String> stages = Streams.stream(JsonUtils.getJsonArray(json, "stages"))
				.map(je -> JsonUtils.getString(je, "stage"))
				.collect(Collectors.toList());
			return new HasAllStages(stages);
		}
	}
}
