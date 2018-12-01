package leviathan143.morelootstuff.loot.conditions;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class InDimension implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "in_dimension");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private final int dimensionId;

	public InDimension(int dimensionId)
	{
		this.dimensionId = dimensionId;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		Entity dimReference = context.getLootedEntity();
		if (dimReference == null)
		{
			LOGGER.debug("No looted entity provided by LootContext, falling back to player.");
			dimReference = context.getKillerPlayer();
		}
		if (dimReference == null)
		{
			LOGGER.debug("No player provided by LootContext. Unable to determine dimension, returning false.");
			return false;
		}
		return dimReference.getEntityWorld().provider.getDimension() == dimensionId;
	}

	public static class Serialiser extends LootCondition.Serializer<InDimension>
	{
		public Serialiser()
		{
			super(ID, InDimension.class);
		}

		@Override
		public void serialize(JsonObject json, InDimension value, JsonSerializationContext context)
		{
			json.addProperty("dimID", value.dimensionId);
		}

		@Override
		public InDimension deserialize(JsonObject json, JsonDeserializationContext context)
		{
			return new InDimension(JsonUtils.getInt(json, "dimID"));
		}
	}
}
