
package me.heldplayer.HeldStone.sign.chat;

import java.util.List;

import me.heldplayer.HeldStone.Player;
import me.heldplayer.HeldStone.sign.HeldSign;
import me.heldplayer.HeldStone.sign.TriggerType;
import me.heldplayer.util.Constants;
import me.heldplayer.util.Functions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.mojang.NBT.NBTBase;
import com.mojang.NBT.NBTTagString;

public class AnnounceSign extends HeldSign {
    private String message;
    private String prefix;
    private InputState lastState;
    private int x1, x2, y1, y2, z1, z2;

    public void triggerSign(TriggerType type, Object data) {
        InputState is = this.getInput(1, (BlockRedstoneEvent) data);

        if (lastState != InputState.HIGH && is == InputState.HIGH) {
            List<org.bukkit.entity.Player> players = getLocation().getWorld().getPlayers();

            for (org.bukkit.entity.Player player : players) {
                if (player.getLocation().getBlockX() < x1)
                    continue;
                if (player.getLocation().getBlockX() > x2)
                    continue;
                if (player.getLocation().getBlockY() < y1)
                    continue;
                if (player.getLocation().getBlockY() > y2)
                    continue;
                if (player.getLocation().getBlockZ() < z1)
                    continue;
                if (player.getLocation().getBlockZ() > z2)
                    continue;

                Functions.sendMessage(player, ChatColor.translateAlternateColorCodes('&', message), ChatColor.translateAlternateColorCodes('&', prefix));
            }
        }

        lastState = is;
    }

    public boolean initialize(boolean reload) {
        Player player = getPlugin().pmng.safelyGet(getOwner(), getPlugin());

        if (!reload) {
            if (player.message == null || player.message == "") {
                Functions.sendMessage(player.getPlayer(), "You must specify a message using /heldstone msg first!", Constants.error);

                return false;
            }
            else {
                message = player.message;
            }
        }

        String locLine1 = getLine(1);
        String locLine2 = getLine(2);

        if (locLine1.length() == 0 && locLine2.length() == 0) {
            if (!reload) {
                if (player.locationsSet) {
                    x1 = player.primaryLoc.getBlockX();
                    x2 = player.secondaryLoc.getBlockX();
                    y1 = player.primaryLoc.getBlockY();
                    y2 = player.secondaryLoc.getBlockY();
                    z1 = player.primaryLoc.getBlockZ();
                    z2 = player.secondaryLoc.getBlockZ();

                    if (x1 > x2) {
                        int x3 = x1;
                        x1 = x2;
                        x2 = x3;
                    }
                    if (y1 > y2) {
                        int y3 = y1;
                        y1 = y2;
                        y2 = y3;
                    }
                    if (z1 > z2) {
                        int z3 = z1;
                        z1 = z2;
                        z2 = z3;
                    }
                }
                else {
                    Functions.sendMessage(player.getPlayer(), "Please select a valid cuboid selection with glowstone!", Constants.error);
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else if (locLine1.split(" ").length != 3 && locLine2.split(" ").length != 3) {
            if (!reload) {
                Functions.sendMessage(player.getPlayer(), "Please select a valid cuboid selection with glowstone!", Constants.error);
                return false;
            }
            else {
                return false;
            }
        }
        else if (locLine1.split(" ").length == 3 && locLine2.split(" ").length == 3) {
            try {
                String[] locs1 = locLine1.split(" ");
                String[] locs2 = locLine2.split(" ");

                x1 = Integer.parseInt(locs1[0]);
                x2 = Integer.parseInt(locs2[0]);
                y1 = Integer.parseInt(locs1[1]);
                y2 = Integer.parseInt(locs2[1]);
                z1 = Integer.parseInt(locs1[2]);
                z2 = Integer.parseInt(locs2[2]);
            }
            catch (Exception ex) {
                Functions.sendMessage(player.getPlayer(), "Please select a valid cuboid selection with glowstone!", Constants.error);
                return false;
            }

            if (x1 > x2) {
                int x3 = x1;
                x1 = x2;
                x2 = x3;
            }
            if (y1 > y2) {
                int y3 = y1;
                y1 = y2;
                y2 = y3;
            }
            if (z1 > z2) {
                int z3 = z1;
                z1 = z2;
                z2 = z3;
            }

            Location loc = getLocation();

            x1 += loc.getBlockX();
            x2 += loc.getBlockX();
            y1 += loc.getBlockY();
            y2 += loc.getBlockY();
            z1 += loc.getBlockZ();
            z2 += loc.getBlockZ();
        }

        prefix = getLine(3);

        register(TriggerType.REDSTONE_CHANGE);

        return true;
    }

    public void formatLines() {
        Location loc = getLocation();
        int x1 = this.x1 - loc.getBlockX();
        int x2 = this.x2 - loc.getBlockX();
        int y1 = this.y1 - loc.getBlockY();
        int y2 = this.y2 - loc.getBlockY();
        int z1 = this.z1 - loc.getBlockZ();
        int z2 = this.z2 - loc.getBlockZ();

        setLine(1, x1 + " " + y1 + " " + z1);
        setLine(2, x2 + " " + y2 + " " + z2);
        setLine(3, prefix);
    }

    public void invalidate() {}

    public NBTBase getData() {
        return new NBTTagString(message);
    }

    public void setData(NBTBase value) {
        message = value.toString();
    }
}
