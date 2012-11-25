
package dsiwars.co.cc.HeldStone.sign.sensor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class PSensorSign extends HeldSign {

    boolean lastState = false;

    @Override
    protected void triggersign(TriggerType type, Object args) {
        boolean tripped = false;

        int xmin, xmax, ymin, ymax, zmin, zmax;
        xmin = Math.min(this.x1, this.x2);
        xmax = Math.max(this.x1, this.x2);

        ymin = Math.min(this.y1, this.y2);
        ymax = Math.max(this.y1, this.y2);

        zmin = Math.min(this.z1, this.z2);
        zmax = Math.max(this.z1, this.z2);

        boolean x, y, z;

        Location l;

        Player p = this.main.getServer().getPlayer(this.PlayerName);

        if (p.isOnline()) {
            l = p.getLocation();

            x = (xmin <= l.getBlockX()) && (l.getBlockX() <= xmax);
            y = (ymin <= l.getBlockY()) && (l.getBlockY() <= ymax);
            z = (zmin <= l.getBlockZ()) && (l.getBlockZ() <= zmax);

            if (x && y && z) {
                tripped = true;
            }
        }

        if (tripped != this.lastState) {
            this.lastState = tripped;
            setOutput(tripped);
        }
    }

    @Override
    protected void setNBTData(NBTBase tag) {}

    @Override
    public NBTBase getNBTData() {
        return new NBTTagInt(0);
    }

    private int x1, y1, z1, x2, y2, z2;
    private String PlayerName;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {
        if (this.getLines()[1].equals("") && this.getLines()[2].equals("")) {
            if (this.main.players.exists(getOwnerName())) {
                HeldPlayer p = this.main.players.get(getOwnerName());

                if (p.l1 && (p.loc2 != null)) {
                    this.x1 = p.loc1.getBlockX() - getHostLocation().getBlockX();
                    this.y1 = p.loc1.getBlockY() - getHostLocation().getBlockY();
                    this.z1 = p.loc1.getBlockZ() - getHostLocation().getBlockZ();

                    this.x2 = p.loc2.getBlockX() - getHostLocation().getBlockX();
                    this.y2 = p.loc2.getBlockY() - getHostLocation().getBlockY();
                    this.z2 = p.loc2.getBlockZ() - getHostLocation().getBlockZ();
                }
                else {
                    this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the sensor area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
                    if (!reload) {
                        event.setCancelled(true);
                    }
                    return false;
                }
            }
            else {
                this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the sensor area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }
        else {
            String[] split1, split2;
            split1 = this.getLines()[1].split(" ");
            split2 = this.getLines()[2].split(" ");

            try {
                this.x1 = Integer.parseInt(split1[0]);
                this.y1 = Integer.parseInt(split1[1]);
                this.z1 = Integer.parseInt(split1[2]);

                this.x2 = Integer.parseInt(split2[0]);
                this.y2 = Integer.parseInt(split2[1]);
                this.z2 = Integer.parseInt(split2[2]);
            }
            catch (Exception e) {
                this.main.alert(getOwnerName(), "The coordinates you specified are either invalid or formatted incorrectly.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }

        if (this.getLines()[3].equalsIgnoreCase("")) {
            this.main.alert(getOwnerName(), "TYou need to specify a player to look for.", ChatColor.RED);
            if (!reload) {
                event.setCancelled(true);
            }
            return false;
        }
        else {
            this.PlayerName = this.getLines()[3];
        }

        if (!reload) {
            this.clearArgLines(event);
            this.setLine(1, this.x1 + " " + this.y1 + " " + this.z1, event);
            this.setLine(2, this.x2 + " " + this.y2 + " " + this.z2, event);
            this.setLine(3, this.PlayerName, event);
        }

        this.x1 += getHostLocation().getBlockX();
        this.y1 += getHostLocation().getBlockY();
        this.z1 += getHostLocation().getBlockZ();

        this.x2 += getHostLocation().getBlockX();
        this.y2 += getHostLocation().getBlockY();
        this.z2 += getHostLocation().getBlockZ();

        this.main.sgc.register(this, TriggerType.TIMER_HALF_SECOND);
        if (!reload) {
            init("PSensor sign accepted.");
        }

        return true;
    }

    @Override
    public void invalidate() {}

    @Override
    public String getTriggerTypesString() {
        return TriggerType.TIMER_HALF_SECOND.name();
    }
}
