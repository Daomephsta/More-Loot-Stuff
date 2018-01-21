package leviathan143.morelootstuff.loot.conditions.gamestages;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff.Constants;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class HasAllStages implements LootCondition
{
	private final List<String> stages;
	
	public HasAllStages(String... stages)
	{
		this.stages = Lists.newArrayList(stages);
	}
	
	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		if(context.getKillerPlayer() == null) return false;
		return PlayerDataHandler.getStageData((EntityPlayer) context.getKillerPlayer()).hasUnlockedAll(stages);
	}
	
	public static class Serialiser extends LootCondition.Serializer<HasAllStages>
	{
		public Serialiser()
		{
			super(new ResourceLocation(Constants.MODID, "has_all_stages"), HasAllStages.class);
		}

		@Override
		public void serialize(JsonObject json, HasAllStages value, JsonSerializationContext context)
		{
			JsonArray stageArray = new JsonArray();
			for(String stage : value.stages)
			{
				stageArray.add(stage);
			}
			json.add("stages", stageArray);
		}

		@Override
		public HasAllStages deserialize(JsonObject json, JsonDeserializationContext context)
		{
			JsonArray stagesJSON = json.getAsJsonArray("stages");
			String[] stages = new String[stagesJSON.size()];
			for (int e = 0; e < stagesJSON.size(); e++)
			{
				stages[e] = stagesJSON.get(e).getAsString();
			}
			return new HasAllStages(stages);
		}
	}
}
