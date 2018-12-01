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

public class InBounds implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "in_bounds");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private static final int WORLD_SIZE = 30000000;
	private final int minX, minY, minZ;
	private final int maxX, maxY, maxZ;

	public InBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		Entity posReference = context.getLootedEntity();
		if (posReference == null)
		{
			LOGGER.debug("No looted entity provided by LootContext, falling back to player.");
			posReference = context.getKillerPlayer();
		}
		if (posReference == null)
		{
			LOGGER.debug("No player provided by LootContext. Unable to determine position, returning false.");
			return false;
		}
		return (posReference.posX >= minX && posReference.posX <= maxX)
				&& (posReference.posY >= minY && posReference.posY <= maxY)
				&& (posReference.posZ >= minZ && posReference.posZ <= maxZ);
	}

	public static class Serialiser extends LootCondition.Serializer<InBounds>
	{
		public Serialiser()
		{
			super(InBounds.ID, InBounds.class);
		}

		@Override
		public void serialize(JsonObject json, InBounds value, JsonSerializationContext context)
		{
			json.addProperty("minX", value.minX);
			json.addProperty("minY", value.minY);
			json.addProperty("minZ", value.minZ);
			json.addProperty("maxX", value.maxX);
			json.addProperty("maxY", value.maxY);
			json.addProperty("maxZ", value.maxZ);
		}

		@Override
		public InBounds deserialize(JsonObject json, JsonDeserializationContext context)
		{
			int minX = JsonUtils.getInt(json, "minX", -WORLD_SIZE), 
				minY = JsonUtils.getInt(json, "minY", 0),
				minZ = JsonUtils.getInt(json, "minZ", -WORLD_SIZE);
			
			int	maxX = JsonUtils.getInt(json, "maxX", WORLD_SIZE),
				maxY = JsonUtils.getInt(json, "maxY", 255), 
				maxZ = JsonUtils.getInt(json, "maxZ", WORLD_SIZE);
			return new InBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}
	}
}
