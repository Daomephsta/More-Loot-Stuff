package leviathan143.morelootstuff.commands;

import leviathan143.morelootstuff.MoreLootStuff.Constants;
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
		return Constants.MODID;
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return Constants.MODID + ".commands.main_command.usage";
	}
}
