package leviathan143.morelootstuff.commands;

import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.biome.Biome;

public class CommandBiome extends CommandBase
{
	@Override
	public String getName()
	{
		return "biome";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return MoreLootStuff.MODID + ".commands.biome.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		Biome currentBiome = sender.getEntityWorld().getBiome(sender.getPosition());
		sender.sendMessage(new TextComponentString(currentBiome.getRegistryName().toString()));
	}
}
