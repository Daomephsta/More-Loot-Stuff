package leviathan143.morelootstuff.loot.conditions;

import java.util.Random;

import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class InDimension implements LootCondition
{
	private final int dimensionId;

	public InDimension(int dimensionId)
	{
		this.dimensionId = dimensionId;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		Entity dimReference = context.getLootedEntity() != null ? context.getLootedEntity() : context.getKillerPlayer();
		if (dimReference == null) return false;
		return dimReference.getEntityWorld().provider.getDimension() == dimensionId;
	}

	public static class Serialiser extends LootCondition.Serializer<InDimension>
	{
		public Serialiser()
		{
			super(new ResourceLocation(MoreLootStuff.MODID, "in_dimension"), InDimension.class);
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
