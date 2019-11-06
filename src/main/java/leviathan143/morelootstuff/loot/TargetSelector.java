package leviathan143.morelootstuff.loot;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.entity.Entity;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootContext.EntityTarget;

public enum TargetSelector
{
    PLAYER("player", EntityTarget.KILLER_PLAYER),
    KILLER("killer", EntityTarget.KILLER),
    LOOTED_ENTITY("looted_entity", EntityTarget.THIS),
    OLD_BEHAVIOUR("old_behaviour", null)
    {
        @Override
        public Entity get(LootContext context)
        {
            //If there is no looted entity(e.g chests), use the player instead
            Entity entity = context.getLootedEntity();
            if (entity == null)
            {
                LOGGER.debug("No looted entity provided by LootContext, falling back to player.");
                entity = context.getKillerPlayer();
            }
            if (entity == null)
                LOGGER.debug("No player provided by LootContext. Unable to determine biome type, returning false.");
            return entity;
        }
    };

    private static final Logger LOGGER = LogManager.getLogger(MoreLootStuff.MODID);
    private static final Map<String, TargetSelector> TARGET_BY_ID;
    static
    {
        TARGET_BY_ID = Arrays.stream(values()).collect(Collectors.toMap(t -> t.id, t -> t));
    }

    private final String id;
    private final EntityTarget delegate;

    private TargetSelector(String id, EntityTarget delegate)
    {
        this.id = id;
        this.delegate = delegate;
    }

    public static TargetSelector fromId(String id)
    {
        TargetSelector target = TARGET_BY_ID.get(id);
        if (target == null)
            throw new IllegalArgumentException("Unknown target " + id);
        return target;
    }

    public static TargetSelector fromJson(JsonObject json, String key)
    {
        try
        {
            return fromId(JsonUtils.getString(json, key));
        }
        catch (IllegalArgumentException iae)
        {
            throw new JsonSyntaxException(iae.getMessage());
        }
    }

    public JsonElement toJson()
    {
        return new JsonPrimitive(id);
    }

    public Entity get(LootContext context)
    {
        return context.getEntity(this.delegate);
    }

    @Override
    public String toString()
    {
        return id;
    }
}
