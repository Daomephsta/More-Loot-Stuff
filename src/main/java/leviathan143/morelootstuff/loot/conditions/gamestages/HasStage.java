package leviathan143.morelootstuff.loot.conditions.gamestages;

import java.util.Random;

import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class HasStage implements LootCondition
{
	private final String stage;

	public HasStage(String stage)
	{
		this.stage = stage;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		if (context.getKillerPlayer() == null) return false;
		return PlayerDataHandler.getStageData((EntityPlayer) context.getKillerPlayer()).hasUnlockedStage(stage);
	}

	public static class Serialiser extends LootCondition.Serializer<HasStage>
	{
		public Serialiser()
		{
			super(new ResourceLocation(MoreLootStuff.MODID, "has_stage"), HasStage.class);
		}

		@Override
		public void serialize(JsonObject json, HasStage value, JsonSerializationContext context)
		{
			json.add("stage", new JsonPrimitive(value.stage));
		}

		@Override
		public HasStage deserialize(JsonObject json, JsonDeserializationContext context)
		{
			return new HasStage(json.get("stage").getAsString());
		}
	}
}
