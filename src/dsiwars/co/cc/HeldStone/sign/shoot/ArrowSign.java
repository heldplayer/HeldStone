
package dsiwars.co.cc.HeldStone.sign.shoot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.Direction;
import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class ArrowSign extends HeldSign {

    @Override
    protected void triggersign(TriggerType type, Object args) {
        if (args == null) {
            return;
        }
        if (getInputId((BlockRedstoneEvent) args) != 1 || this.getInput(1, (BlockRedstoneEvent) args) != InputState.HIGH) {
            return;
        }

        Location spawnLoc = new Location(getWorld(), this.x + 0.5, this.y + 0.5, this.z + 0.5);

        Snowball snowball = getWorld().spawn(spawnLoc, Snowball.class);

        snowball.setVelocity(Direction.getVelocityVector(this.spawnDir, this.main.rand, this.speed));
    }

    @Override
    protected void setNBTData(NBTBase tag) {}

    @Override
    public NBTBase getNBTData() {
        return new NBTTagInt(0);
    }

    private Direction spawnDir;
    int x, y, z = 0;
    Double speed = 1.0;

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

        String dirLine = this.getLines(event)[2];

        if (dirLine.equals("") || (dirLine == null)) {
            this.spawnDir = Direction.UP;
        }
        else {
            this.spawnDir = Direction.fromString(dirLine.toUpperCase());
            if ((this.spawnDir == Direction.ERROR) || (this.spawnDir == null)) {
                this.spawnDir = Direction.UP;
            }
        }

        String speedLine = this.getLines(event)[3];

        if (speedLine.equals("")) {
            this.speed = 1.0;
        }
        else {
            try {
                this.speed = Double.parseDouble(speedLine);
            }
            catch (Exception ex) {
                this.speed = 1.0;
            }
        }

        if (!reload) {
            this.clearArgLines();
            this.setLine(1, this.x + " " + this.y + " " + this.z, event);
            this.setLine(2, this.spawnDir.toString(), event);
            this.setLine(3, this.speed + "", event);
        }

        this.x += getHostLocation().getBlockX();
        this.y += getHostLocation().getBlockY();
        this.z += getHostLocation().getBlockZ();

        this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
        if (!reload) {
            init("Snowball sign accepted.");
        }

        return true;
    }

    @Override
    public void invalidate() {}

    @Override
    public String getTriggerTypesString() {
        return TriggerType.REDSTONE_CHANGE.name();
    }
}
