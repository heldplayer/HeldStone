package me.heldplayer.HeldStone.command;

import me.heldplayer.HeldStone.HeldStone;
import me.heldplayer.HeldStone.Player;
import me.heldplayer.util.Constants;
import me.heldplayer.util.Functions;

import org.bukkit.command.CommandSender;

public class MsgCommand extends HeldStoneSubCommand {

	public MsgCommand(String[] labels, String perms, HeldStone plugin) {
		super(labels, perms, plugin);
	}

	public void exectute(HeldStoneCommand parent, CommandSender sender, String label, String[] args) {
		if (!(sender instanceof org.bukkit.entity.Player)) {
			Functions.sendMessage(sender, "You cannot set messages!", Constants.error);

			return;
		}

		Player player = parent.main.pmng.safelyGet(sender.getName(), parent.main);

		if (args.length == 0) {
			Functions.sendMessage(sender, "Message is set to: " + player.message, Constants.info);
		} else {
			boolean flag = true;
			String result = "";

			for (String part : args) {
				if (flag) {
					flag = false;
				} else {
					result += " ";
				}
				result += part;
			}

			if (result.charAt(0) == '>')
				player.message += result;
			else
				player.message = result;

			Functions.sendMessage(sender, "Message is set to: " + player.message, Constants.success);
		}
	}
}
