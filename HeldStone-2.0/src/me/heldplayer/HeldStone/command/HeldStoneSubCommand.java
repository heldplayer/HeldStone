package me.heldplayer.HeldStone.command;

import me.heldplayer.HeldStone.HeldStone;

import org.bukkit.command.CommandSender;

public abstract class HeldStoneSubCommand {
	private final String perm;
	protected final HeldStone main;

	public HeldStoneSubCommand(String[] names, String perms, HeldStone plugin) {
		for (String name : names) {
			HeldStoneCommand.commands.put(name, this);
		}
		perm = perms;
		main = plugin;
	}
	
	public boolean hasPerms(CommandSender sender){
		if(sender.hasPermission("heldstone.command.*")){
			return true;
		}
		
		return sender.hasPermission(perm);
	}

	public abstract void exectute(HeldStoneCommand parent, CommandSender sender, String label, String[] args);
}
