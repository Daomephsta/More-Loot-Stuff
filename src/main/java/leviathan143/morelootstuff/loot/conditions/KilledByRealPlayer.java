package leviathan143.morelootstuff.loot.conditions;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.*;

import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.common.util.FakePlayer;

public class KilledByRealPlayer implements LootCondition
{
	private static final ResourceLocation ID = new ResourceLocation(MoreLootStuff.MODID, "killed_by_real_player");
	private static final Logger LOGGER = LogManager.getLogger(ID.toString());
	private final boolean inverse;

	public KilledByRealPlayer(boolean inverse)
	{
		this.inverse = inverse;
	}

	@Override
	public boolean testCondition(Random rand, LootContext context)
	{
		if (context.getKillerPlayer() == null)
		{
			LOGGER.debug("No player provided by LootContext. Unable to determine killer type, returning false.");
		}
		boolean killerIsPlayer = context.getKillerPlayer() instanceof EntityPlayer;
		boolean isFake = context.getKillerPlayer() instanceof FakePlayer;
		return killerIsPlayer && (inverse ? isFake : !isFake);
	}

	public static class Serialiser extends LootCondition.Serializer<KilledByRealPlayer>
	{
		public Serialiser()
		{
			super(ID, KilledByRealPlayer.class);
		}

		@Override
		public void serialize(JsonObject json, KilledByRealPlayer value, JsonSerializationContext context)
		{
			json.addProperty("inverse", value.inverse);
		}

		@Override
		public KilledByRealPlayer deserialize(JsonObject json, JsonDeserializationContext context)
		{
			return new KilledByRealPlayer(JsonUtils.getBoolean(json, "inverse", false));
		}
	}
}
