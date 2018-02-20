package leviathan143.morelootstuff.loot.conditions.gamestages;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class HasAnyOfStages implements LootCondition
{
	private final List<String> stages;
	
	public HasAnyOfStages(String... stages)
	{
		this.stages = Lists.newArrayList(stages);
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		if(context.getKillerPlayer() == null) return false;
		return PlayerDataHandler.getStageData((EntityPlayer) context.getKillerPlayer()).hasUnlockedAnyOf(stages);
	}
	
	public static class Serialiser extends LootCondition.Serializer<HasAnyOfStages>
	{
		public Serialiser()
		{
			super(new ResourceLocation(MoreLootStuff.MODID, "has_any_of_stages"), HasAnyOfStages.class);
		}

		@Override
		public void serialize(JsonObject json, HasAnyOfStages value, JsonSerializationContext context)
		{
			JsonArray stageArray = new JsonArray();
			for(String stage : value.stages)
			{
				stageArray.add(stage);
			}
			json.add("stages", stageArray);
		}

		@Override
		public HasAnyOfStages deserialize(JsonObject json, JsonDeserializationContext context)
		{
			JsonArray stagesJSON = json.getAsJsonArray("stages");
			String[] stages = new String[stagesJSON.size()];
			for (int e = 0; e < stagesJSON.size(); e++)
			{
				stages[e] = stagesJSON.get(e).getAsString();
			}
			return new HasAnyOfStages(stages);
		}
	}
}
