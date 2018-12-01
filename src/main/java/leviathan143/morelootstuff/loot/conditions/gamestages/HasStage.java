package leviathan143.morelootstuff.loot.conditions.gamestages;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class HasStage implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "has_stage");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private final String stage;

	public HasStage(String stage)
	{
		this.stage = stage;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		if (context.getKillerPlayer() == null) 
		{
			LOGGER.debug("No player provided by LootContext. Unable to determine granted stages, returning false.");
			return false;
		}
		return GameStageHelper.hasStage((EntityPlayer) context.getKillerPlayer(), stage);
	}

	public static class Serialiser extends LootCondition.Serializer<HasStage>
	{
		public Serialiser()
		{
			super(ID, HasStage.class);
		}

		@Override
		public void serialize(JsonObject json, HasStage value, JsonSerializationContext context)
		{
			json.add("stage", new JsonPrimitive(value.stage));
		}

		@Override
		public HasStage deserialize(JsonObject json, JsonDeserializationContext context)
		{
			return new HasStage(JsonUtils.getString(json, "stage"));
		}
	}
}
