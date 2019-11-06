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

public class InDimension implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "in_dimension");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private final int dimensionId;
    public TargetSelector targetSelector;

	public InDimension(int dimensionId, TargetSelector targetSelector)
	{
		this.dimensionId = dimensionId;
        this.targetSelector = targetSelector;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
	    Entity reference = targetSelector.get(context);
        if (reference == null)
        {
            LOGGER.debug("LootContext has no {}. Unable to determine dimension, returning false.", targetSelector);
			return false;
		}
		return reference.getEntityWorld().provider.getDimension() == dimensionId;
	}

	public static class Serialiser extends LootCondition.Serializer<InDimension>
	{
		public Serialiser()
		{
			super(ID, InDimension.class);
		}

		@Override
		public void serialize(JsonObject json, InDimension condition, JsonSerializationContext context)
		{
			json.addProperty("dimID", condition.dimensionId);
            json.add("target", condition.targetSelector.toJson());
		}

		@Override
		public InDimension deserialize(JsonObject json, JsonDeserializationContext context)
		{
            TargetSelector targetSelector = json.has("target")
                ? TargetSelector.fromJson(json, "target")
                : TargetSelector.OLD_BEHAVIOUR;
			return new InDimension(JsonUtils.getInt(json, "dimID"), targetSelector);
		}
	}
}
