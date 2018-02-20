package leviathan143.morelootstuff.commands;

import leviathan143.morelootstuff.MoreLootStuff;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandMoreLootStuff extends CommandTreeBase
{
	public CommandMoreLootStuff()
	{
		addSubcommand(new CommandBiome());
	}
	
	@Override
	public String getName()
	{
		return MoreLootStuff.MODID;
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return MoreLootStuff.MODID + ".commands.main_command.usage";
	}
}
