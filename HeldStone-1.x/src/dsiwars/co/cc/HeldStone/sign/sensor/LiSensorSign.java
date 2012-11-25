
package dsiwars.co.cc.HeldStone.sign.sensor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.BlockControl;
import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class LiSensorSign extends HeldSign {

    boolean lastState = false;

    @Override
    protected void triggersign(TriggerType type, Object args) {
        boolean tripped = getWorld().getBlockAt(this.x, this.y, this.z).getLightLevel() <= this.LightLevel;

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

    int x, y, z = 0;
    Byte LightLevel = 15;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {
        if (this.getLines(event)[1].equals("")) {
            if (this.main.players.exists(getOwnerName())) {
                HeldPlayer p = this.main.players.get(getOwnerName());

                if (p.loc3 != null) {
                    this.x = p.loc3.getBlockX() - getHostLocation().getBlockX();
                    this.y = p.loc3.getBlockY() - getHostLocation().getBlockY();
                    this.z = p.loc3.getBlockZ() - getHostLocation().getBlockZ();
                }
                else {
                    this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the block location on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
                    if (!reload) {
                        event.setCancelled(true);
                    }
                    return false;
                }
            }
            else {
                this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the block location on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }
        else {
            String[] split1;
            split1 = this.getLines(event)[1].split(" ");

            try {
                this.x = Integer.parseInt(split1[0]);
                this.y = Integer.parseInt(split1[1]);
                this.z = Integer.parseInt(split1[2]);
            }
            catch (Exception e) {
                this.main.alert(getOwnerName(), "The coordinates you specified are either invalid or not formatted properly.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }

        if (this.getLines()[2].equals("")) {
            this.main.alert(getOwnerName(), "You need to specify a light level between 0 and 15.", ChatColor.RED);
            if (!reload) {
                event.setCancelled(true);
            }
            return false;
        }
        else {
            try {
                this.LightLevel = Byte.parseByte(this.getLines()[2]);

                if (this.LightLevel < 0) {
                    this.LightLevel = 0;
                }
                else if (this.LightLevel > 15) {
                    this.LightLevel = 15;
                }
            }
            catch (Exception ex) {
                this.main.alert(getOwnerName(), "You need to specify a light level between 0 and 15.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }

        if (!reload) {
            this.clearArgLines();
            this.setLine(1, this.x + " " + this.y + " " + this.z, event);
            this.setLine(2, this.LightLevel.toString());
        }

        this.x += getHostLocation().getBlockX();
        this.y += getHostLocation().getBlockY();
        this.z += getHostLocation().getBlockZ();

        this.main.sgc.register(this, TriggerType.TIMER_HALF_SECOND);
        if (!reload) {
            init("LiSensor sign accepted.");
        }

        return true;
    }

    public static boolean isValidID(int id) {

        boolean isValid = false;

        Material[] ms = Material.values();
        for (Material m : ms) {
            if (m.getId() == id && m.isBlock()) {
                isValid = true;
            }
        }

        return isValid;
    }

    private BlockControl Control;

    @Override
    public void invalidate() {
        this.main.bcr.remove(this.Control);
    }

    @Override
    public String getTriggerTypesString() {
        return TriggerType.TIMER_HALF_SECOND.name();
    }
}
