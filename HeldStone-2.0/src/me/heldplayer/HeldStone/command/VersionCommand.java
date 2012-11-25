package me.heldplayer.HeldStone.command;

import me.heldplayer.HeldStone.HeldStone;
import me.heldplayer.util.Constants;
import me.heldplayer.util.Functions;

import org.bukkit.command.CommandSender;

public class VersionCommand extends HeldStoneSubCommand {

	public VersionCommand(String[] labels, String perms, HeldStone plugin) {
		super(labels, perms, plugin);
	}

	public void exectute(HeldStoneCommand parent, CommandSender sender, String label, String[] args) {
		Functions.sendMessage(sender, "========== " + main.getDescription().getFullName() + " ==========", Constants.debug);
		//Functions.sendMessage(sender, "Author: " + Constants.debugVal + main.getDescription().getAuthors().get(0), Constants.debug);
		Functions.sendMessage(sender, "Author: " + Constants.debugVal + "heldplayer", Constants.debug);
	}
}
