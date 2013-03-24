
package me.heldplayer.HeldStone.command;

import java.util.HashMap;
import java.util.Map;

import me.heldplayer.HeldStone.HeldStone;
import me.heldplayer.util.Constants;
import me.heldplayer.util.Functions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HeldStoneCommand implements CommandExecutor {
    protected final HeldStone main;
    protected static final Map<String, HeldStoneSubCommand> commands = new HashMap<String, HeldStoneSubCommand>();

    public HeldStoneCommand(HeldStone plugin) {
        this.main = plugin;

        new MsgCommand(new String[] { "message", "msg" }, "heldstone.command.message", plugin);
        new HelpCommand(new String[] { "help", "?" }, "heldstone.command.help", plugin);
        new VersionCommand(new String[] { "version", "v", "about" }, "heldstone.command.about", plugin);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            commands.get("version").exectute(this, sender, "version", null);
            return true;
        }
        else {
            if (commands.containsKey(args[0])) {
                HeldStoneSubCommand subCommand = commands.get(args[0]);

                if (!subCommand.hasPerms(sender)) {
                    Functions.sendMessage(sender, "You lack the required permissions!", Constants.error);
                }

                String[] newArgs = new String[args.length - 1];

                System.arraycopy(args, 1, newArgs, 0, args.length - 1);

                subCommand.exectute(this, sender, args[0], newArgs);
                return true;
            }
            else {
                Functions.sendMessage(sender, "Unknown command, use " + Constants.info + "/" + label + " help " + Constants.error + "for help.", Constants.error);
                return true;
            }
        }
    }
}
