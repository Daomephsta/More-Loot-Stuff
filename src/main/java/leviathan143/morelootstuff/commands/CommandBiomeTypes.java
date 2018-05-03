package leviathan143.morelootstuff.commands;

import java.util.stream.Collectors;

import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class CommandBiomeTypes extends CommandBase
{
	@Override
	public String getName()
	{
		return "biomeTypes";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return MoreLootStuff.MODID + ".commands.biome_types.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Biome currentBiome = sender.getEntityWorld().getBiome(sender.getPosition());
		String typeNames = BiomeDictionary.getTypes(currentBiome).stream().map(Object::toString)
				.collect(Collectors.joining(", "));
		sender.sendMessage(new TextComponentTranslation(MoreLootStuff.MODID + ".commands.biomeTypes.output",
				currentBiome.getRegistryName(), typeNames));
	}
}
