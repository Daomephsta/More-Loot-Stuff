package leviathan143.morelootstuff;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Map;

import net.minecraftforge.common.BiomeDictionary;

public class CommonReflection
{
	private static final Field typeMapField;
	static
	{
		try
		{
			typeMapField = BiomeDictionary.Type.class.getDeclaredField("byName");
			Field.setAccessible(new AccessibleObject[] {typeMapField}, true);
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			throw new RuntimeException("Reflection failure", e);
		}
	}

	// No instantiation allowed!
	private CommonReflection()
	{}

	private static <T> T getFieldValue(Field field, Object instance, Class<T> returnType)
	{
		try
		{
			return returnType.cast(field.get(instance));
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new RuntimeException("Could not get value of field " + field.getName(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, BiomeDictionary.Type> getTypeMap()
	{
		return getFieldValue(typeMapField, null, Map.class);
	}
}
