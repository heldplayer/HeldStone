
package dsiwars.co.cc.HeldStone;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class HSCommand implements CommandExecutor {

    private final HeldStone main;

    public HSCommand(HeldStone heldstone) {
        this.main = heldstone;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Unable to do this via console.");
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("msg")) {
                Player p = (Player) sender;

                if (!this.main.hasPermission(p, "heldstone.command.msg")) {
                    this.main.alert(p.getName(), "The player does not have permission for this!", ChatColor.RED);
                    return true;
                }

                if (args.length > 1) {
                    String newMsg = Functions.combine(args, 1);
                    this.main.alert(p.getName(), "Saved: \"" + newMsg + "\"", ChatColor.GREEN);
                    HeldPlayer psp = this.main.players.safelyGet(p.getName(), this.main);
                    psp.setMessage(newMsg);
                    return true;
                }
                else {
                    HeldPlayer psp = this.main.players.safelyGet(p.getName(), this.main);
                    if (psp.message != null) {
                        this.main.alert(p.getName(), "Stored message: \"" + psp.message + "\"", ChatColor.GOLD);
                    }
                    else {
                        this.main.alert(p.getName(), "You have not stored a message. " + org.bukkit.ChatColor.AQUA + "/hs msg <message>", ChatColor.GOLD);
                    }
                    return true;
                }
            }
            else if (args[0].equalsIgnoreCase("help")) {
                Player p = (Player) sender;

                if (!this.main.hasPermission(p, "heldstone.command.help")) {
                    this.main.alert(p.getName(), "The player does not have permission for this!", ChatColor.RED, "");
                    return true;
                }

                if (this.main.hasPermission(p, "heldstone.command.disable")) {
                    this.main.alert(p.getName(), "disable" + ChatColor.GRAY + " - Disables the HeldStone plugin", ChatColor.GOLD, "/hs");
                }
                if (this.main.hasPermission(p, "heldstone.command.help")) {
                    this.main.alert(p.getName(), "help" + ChatColor.GRAY + " - Shows this help message", ChatColor.GOLD, "/hs");
                }
                if (this.main.hasPermission(p, "heldstone.command.msg")) {
                    this.main.alert(p.getName(), "msg <message>" + ChatColor.GRAY + " - Sets the message used for chat signs", ChatColor.GOLD, "/hs");
                }
                if (this.main.hasPermission(p, "heldstone.command.ping")) {
                    this.main.alert(p.getName(), "ping" + ChatColor.GRAY + " - Checks to see if all signs still work", ChatColor.GOLD, "/hs");
                }
                if (this.main.hasPermission(p, "heldstone.command.snow")) {
                    this.main.alert(p.getName(), "snow" + ChatColor.GRAY + " - Transforms snow blocks into snow layers with specified data. 0 = off", ChatColor.GOLD, "/hs");
                }
                if (this.main.hasPermission(p, "heldstone.command.biome")) {
                    this.main.alert(p.getName(), "biome [list|set [biome]]" + ChatColor.GRAY + " - Sets the biome you are in, or lists available biomes", ChatColor.GOLD, "/hs");
                }
                if (p.getName().equals("heldplayer")) {
                    this.main.alert(p.getName(), "updatecheck" + ChatColor.GRAY + " - Checks to see if there are updates available", ChatColor.GOLD, "/hs");
                }
                if (p.getName().equals("heldplayer")) {
                    this.main.alert(p.getName(), "update" + ChatColor.GRAY + " - Updates HeldStone", ChatColor.GOLD, "/hs");
                }
                if (p.getName().equals("heldplayer")) {
                    this.main.alert(p.getName(), "updateip [ip]" + ChatColor.GRAY + " - Updates the update IP", ChatColor.GOLD, "/hs");
                }
                if (p.getName().equals("heldplayer")) {
                    this.main.alert(p.getName(), "addench [enchantment] [level]" + ChatColor.GRAY + " - Adds an enchantment to an item", ChatColor.GOLD, "/hs");
                }

                return true;
            }
            else if (args[0].equalsIgnoreCase("disable")) {
                Player p = (Player) sender;

                if (!this.main.hasPermission(p, "heldstone.command.disable")) {
                    this.main.alert(p.getName(), "The player does not have permission for this!", ChatColor.RED, "");
                    return true;
                }

                this.main.alert(p.getName(), "HeldStone is disabled! Bye Bye!", ChatColor.RED);

                this.main.pm.disablePlugin(this.main);

                return true;
            }
            else if (args[0].equalsIgnoreCase("ping")) {
                Player p = (Player) sender;

                if (!this.main.hasPermission(p, "heldstone.command.ping")) {
                    this.main.alert(p.getName(), "The player does not have permission for this!", ChatColor.RED, "");
                    return true;
                }

                this.main.sgc.trigger(TriggerType.PING, null);

                return true;
            }
            else if (args[0].equalsIgnoreCase("snow")) {
                Player p = (Player) sender;

                if (!this.main.hasPermission(p, "heldstone.command.snow")) {
                    this.main.alert(p.getName(), "The player does not have permission for this!", ChatColor.RED, "");
                    return true;
                }

                if (args.length == 2) {
                    HeldPlayer psp = this.main.players.safelyGet(p.getName(), this.main);
                    try {
                        psp.snow = Math.max(0, Math.min(7, Integer.parseInt(args[1])));
                    }
                    catch (Exception ex) {
                        this.main.alert(p.getName(), "You need to give a number between 0 and 7!", ChatColor.RED, "");
                    }
                    return true;
                }
                else {
                    HeldPlayer psp = this.main.players.safelyGet(p.getName(), this.main);
                    psp.snow = 0;
                }
            }
            else if (args[0].equalsIgnoreCase("ride")) {
                Player p = (Player) sender;

                if (!this.main.hasPermission(p, "heldstone.command.ride")) {
                    this.main.alert(p.getName(), "The player does not have permission for this!", ChatColor.RED, "");
                    return true;
                }

                if (args.length > 1) {
                    Player target = this.main.getServer().getPlayer(args[1]);

                    if (target != null && !target.hasPermission("heldstone.command.ride.exempt")) {
                        target.setPassenger(p);
                    }
                }
                else {
                    if (p.isInsideVehicle()) {
                        p.leaveVehicle();
                    }
                }

                return true;
            }
            else if (args[0].equalsIgnoreCase("biome")) {
                Player p = (Player) sender;

                if (!this.main.hasPermission(p, "heldstone.command.biome")) {
                    this.main.alert(p.getName(), "The player does not have permission for this!", ChatColor.RED, "");
                    return true;
                }

                if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
                    String biomes = "";
                    boolean flag = true;

                    for (Biome biome : Biome.values()) {
                        if (!Functions.validBiome(biome)) {
                            continue;
                        }

                        if (flag) {
                            flag = false;

                            biomes += biome.name().toLowerCase();
                        }
                        else {
                            biomes += ", " + biome.name().toLowerCase();
                        }
                    }

                    this.main.alert(p.getName(), "Available biomes: " + ChatColor.YELLOW + biomes, ChatColor.LIGHT_PURPLE);

                    return true;
                }
                else if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
                    HeldPlayer psp = this.main.players.safelyGet(p.getName(), this.main);

                    if (!psp.l1) {
                        this.main.alert(p.getName(), "Make a valid selection first!", ChatColor.RED);
                        return true;
                    }

                    int x1 = psp.loc1.getBlockX();
                    int z1 = psp.loc1.getBlockZ();
                    int x2 = psp.loc2.getBlockX();
                    int z2 = psp.loc2.getBlockZ();

                    if (x1 > x2) {
                        int x3 = x2;
                        x2 = x1;
                        x1 = x3;
                    }
                    if (z1 > z2) {
                        int z3 = z2;
                        z2 = z1;
                        z1 = z3;
                    }

                    try {
                        Biome biome = Biome.valueOf(args[2].toUpperCase());

                        int counter = 0;
                        for (int x = x1; x <= x2; x++) {
                            for (int z = z1; z <= z2; z++) {
                                psp.loc1.getWorld().setBiome(x, z, biome);

                                counter++;
                            }
                        }

                        this.main.alert(p.getName(), "Biome set! Block update count: " + counter, ChatColor.GREEN);
                        this.main.alert(p.getName(), "To see the changes, relog", ChatColor.GRAY);

                        return true;
                    }
                    catch (Exception ex) {
                        this.main.alert(p.getName(), "Unknown biome! Use \"/" + label + " biome list\" to get a list of available biomes", ChatColor.RED);
                    }
                }
                else if (args.length == 4 && args[1].equalsIgnoreCase("replace")) {
                    HeldPlayer psp = this.main.players.safelyGet(p.getName(), this.main);

                    if (!psp.l1) {
                        this.main.alert(p.getName(), "Make a valid selection first!", ChatColor.RED);
                        return true;
                    }

                    int x1 = psp.loc1.getBlockX();
                    int z1 = psp.loc1.getBlockZ();
                    int x2 = psp.loc2.getBlockX();
                    int z2 = psp.loc2.getBlockZ();

                    if (x1 > x2) {
                        int x3 = x2;
                        x2 = x1;
                        x1 = x3;
                    }
                    if (z1 > z2) {
                        int z3 = z2;
                        z2 = z1;
                        z1 = z3;
                    }

                    try {
                        Biome biome1 = Biome.valueOf(args[2].toUpperCase());
                        Biome biome2 = Biome.valueOf(args[3].toUpperCase());

                        int counter = 0;
                        for (int x = x1; x <= x2; x++) {
                            for (int z = z1; z <= z2; z++) {
                                if (psp.loc1.getWorld().getBiome(x, z) == biome1) {
                                    psp.loc1.getWorld().setBiome(x, z, biome2);

                                    counter++;
                                }
                            }
                        }

                        this.main.alert(p.getName(), "Biome replaced! Block update count: " + counter, ChatColor.GREEN);
                        this.main.alert(p.getName(), "To see the changes, relog", ChatColor.GRAY);

                        return true;
                    }
                    catch (Exception ex) {
                        this.main.alert(p.getName(), "Unknown biome(s)! Use \"/" + label + " biome list\" to get a list of available biomes", ChatColor.RED);
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("updatecheck")) {
                final Player p = (Player) sender;

                if (!p.getName().equals("heldplayer")) {
                    return false;
                }

                if (this.main.address.equalsIgnoreCase("")) {
                    this.main.alert(p.getName(), "The update IP has yet to be specified!", ChatColor.LIGHT_PURPLE);

                    return true;
                }

                this.main.alert(p.getName(), "Checking for updates...", ChatColor.LIGHT_PURPLE);

                this.main.getServer().getScheduler().scheduleAsyncDelayedTask(this.main, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (HSCommand.this.main.upd.updateCheck()) {
                                HSCommand.this.main.alert(p.getName(), "Updates available!", ChatColor.GREEN);
                                String[] reasons = Update.getUpdateReason(HSCommand.this.main.updatereasonaddress);
                                String version = Update.getLatestVersion(HSCommand.this.main.versionaddress);

                                HSCommand.this.main.alert(p.getName(), "Current version: " + HeldStone.version + " New version: " + version, ChatColor.YELLOW);
                                for (String reason : reasons) {
                                    HSCommand.this.main.alert(p.getName(), reason, ChatColor.GOLD);
                                }
                            }
                            else {
                                HSCommand.this.main.alert(p.getName(), "No updates available!", ChatColor.RED);
                            }
                        }
                        catch (Exception ex) {
                            HSCommand.this.main.alert(p.getName(), "Error while checking for updates!", ChatColor.RED);
                        }
                    }
                });

                return true;
            }
            else if (args[0].equalsIgnoreCase("update")) {
                final Player p = (Player) sender;

                if (!p.getName().equals("heldplayer")) {
                    return false;
                }

                if (this.main.address.equalsIgnoreCase("")) {
                    this.main.alert(p.getName(), "The update IP has yet to be specified!", ChatColor.LIGHT_PURPLE);

                    return true;
                }

                this.main.getServer().getScheduler().scheduleAsyncDelayedTask(this.main, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (HSCommand.this.main.upd.updateCheck()) {
                                HSCommand.this.main.alert(p.getName(), "Updates available! Downloading...", ChatColor.GREEN);
                                HSCommand.this.main.upd.download();
                                HSCommand.this.main.alert(p.getName(), "Download complete! Restart the server for the changes to take effect", ChatColor.GREEN);
                            }
                            else {
                                HSCommand.this.main.alert(p.getName(), "No updates available!", ChatColor.RED);
                            }
                        }
                        catch (Exception ex) {
                            HSCommand.this.main.alert(p.getName(), "Error while updating!", ChatColor.RED);
                        }
                    }
                });

                return true;
            }
            else if (args[0].equalsIgnoreCase("updateip")) {
                Player p = (Player) sender;

                if (!p.getName().equals("heldplayer")) {
                    return false;
                }

                this.main.address = "http://" + args[1] + "/jars/HeldStone/HeldStone.jar";
                this.main.versionaddress = "http://" + args[1] + "/jars/HeldStone/version.txt";
                this.main.updatereasonaddress = "http://" + args[1] + "/jars/HeldStone/reason.txt";

                return true;
            }
            if (args[0].equalsIgnoreCase("addench")) {
                Player p = (Player) sender;

                if (!p.getName().equals("heldplayer")) {
                    return false;
                }

                ItemStack stack = null;

                if ((stack = p.getItemInHand()) == null) {
                    this.main.alert(p.getName(), "No item in hand!", ChatColor.RED);
                    return true;
                }

                if ((stack.getAmount() == 0) || (stack.getTypeId() == 0)) {
                    this.main.alert(p.getName(), "No item in hand!", ChatColor.RED);
                    return true;
                }
                try {
                    stack.addUnsafeEnchantment(Enchantment.getByName(args[1]), Integer.parseInt(args[2]));
                }
                catch (Exception ex) {
                    this.main.alert(p.getName(), "Failed to add enchantment", ChatColor.RED);
                }

                return true;
            }
        }
        else {
            Player p = (Player) sender;

            this.main.alert(p.getName(), "HeldStone version " + this.main.pdf.getVersion() + " by heldplayer", ChatColor.GOLD, "");
            if (this.main.hasPermission(p, "heldstone.command.help")) {
                this.main.alert(p.getName(), "/hs help", ChatColor.GOLD, "");
            }

            return true;
        }

        return false;
    }
}
