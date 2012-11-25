
package dsiwars.co.cc.HeldStone.sign.functional;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class BlackHoleSign extends HeldSign {

    private boolean lastState = false;

    @Override
    protected void triggersign(TriggerType type, Object args) {
        InputState is = this.getInput(1, (BlockRedstoneEvent) args);

        if (is != InputState.HIGH) {
            this.lastState = false;
            return;
        }
        else {
            if (this.lastState == true) {
                return;
            }
            this.lastState = true;
        }

        List<Entity> entities = getWorld().getEntities();

        Iterator<Entity> entityIterator = entities.iterator();

        this.blockLocation.getBlock().setTypeIdAndData(0, (byte) 0, false);

        while (entityIterator.hasNext()) {
            Entity currentEntity = entityIterator.next();

            if (currentEntity.getLocation().getBlockX() == this.blockLocation.getBlockX() && currentEntity.getLocation().getBlockY() == this.blockLocation.getBlockY() && currentEntity.getLocation().getBlockZ() == this.blockLocation.getBlockZ()) {
                if (currentEntity instanceof LivingEntity) {
                    LivingEntity currentLivingEntity = (LivingEntity) currentEntity;

                    currentLivingEntity.setHealth(0);
                }
                else {
                    currentEntity.remove();
                }
            }
        }
    }

    @Override
    protected void setNBTData(NBTBase tag) {}

    @Override
    public NBTBase getNBTData() {
        return new NBTTagInt(0);
    }

    Location blockLocation;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {
        int x = 0, y = 0, z = 0;

        if (this.getLines(event)[1].equals("")) {
            if (this.main.players.exists(getOwnerName())) {
                HeldPlayer p = this.main.players.get(getOwnerName());

                if (p.loc3 != null) {
                    x = p.loc3.getBlockX() - getHostLocation().getBlockX();
                    y = p.loc3.getBlockY() - getHostLocation().getBlockY();
                    z = p.loc3.getBlockZ() - getHostLocation().getBlockZ();
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
            String[] split;
            split = this.getLines(event)[1].split(" ");

            try {
                x = Integer.parseInt(split[0]);
                y = Integer.parseInt(split[1]);
                z = Integer.parseInt(split[2]);
            }
            catch (Exception e) {
                this.main.alert(getOwnerName(), "The coordinate you specified is either invalid or not formatted properly.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }

        x += getHostLocation().getBlockX();
        y += getHostLocation().getBlockY();
        z += getHostLocation().getBlockZ();

        this.blockLocation = new Location(getWorld(), x, y, z);

        x -= getHostLocation().getBlockX();
        y -= getHostLocation().getBlockY();
        z -= getHostLocation().getBlockZ();

        if (!reload) {
            this.setLine(1, x + " " + y + " " + z, event);
        }

        this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
        if (!reload) {
            init("BlackHole sign accepted.");
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
