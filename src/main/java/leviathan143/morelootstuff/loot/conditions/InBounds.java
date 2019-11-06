package leviathan143.morelootstuff.loot.conditions;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import leviathan143.morelootstuff.MoreLootStuff;
import leviathan143.morelootstuff.loot.TargetSelector;
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
    private final TargetSelector targetSelector;

	public InBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, TargetSelector targetSelector)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
        this.targetSelector = targetSelector;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
	    Entity reference = targetSelector.get(context);
        if (reference == null)
        {
            LOGGER.debug("LootContext has no {}. Unable to determine position, returning false.", targetSelector);
			return false;
		}
		return (reference.posX >= minX && reference.posX <= maxX)
				&& (reference.posY >= minY && reference.posY <= maxY)
				&& (reference.posZ >= minZ && reference.posZ <= maxZ);
	}

	public static class Serialiser extends LootCondition.Serializer<InBounds>
	{
		public Serialiser()
		{
			super(InBounds.ID, InBounds.class);
		}

		@Override
		public void serialize(JsonObject json, InBounds condition, JsonSerializationContext context)
		{
			json.addProperty("minX", condition.minX);
			json.addProperty("minY", condition.minY);
			json.addProperty("minZ", condition.minZ);
			json.addProperty("maxX", condition.maxX);
			json.addProperty("maxY", condition.maxY);
			json.addProperty("maxZ", condition.maxZ);
            json.add("target", condition.targetSelector.toJson());
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

            TargetSelector targetSelector = json.has("target")
                ? TargetSelector.fromJson(json, "target")
                : TargetSelector.OLD_BEHAVIOUR;
			return new InBounds(minX, minY, minZ, maxX, maxY, maxZ, targetSelector);
		}
	}
}
