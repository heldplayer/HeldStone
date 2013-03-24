
package dsiwars.co.cc.HeldStone.sign.spawn;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Colorable;

import dsiwars.co.cc.HeldStone.Direction;
import dsiwars.co.cc.HeldStone.Functions;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class SpawnSign extends HeldSign {

    @Override
    protected void triggersign(TriggerType type, Object args) {
        if (args == null) {
            return;
        }
        if (getInputId((BlockRedstoneEvent) args) != 1 || this.getInput(1, (BlockRedstoneEvent) args) != InputState.HIGH) {
            return;
        }

        Location spawnLoc = null;

        for (int i = 1; i <= 10; i++) {
            spawnLoc = Direction.shift(getHostLocation(), this.spawnDir, i);
            if ((getWorld().getBlockAt(spawnLoc).getType() == Material.AIR) && (getWorld().isChunkLoaded(getWorld().getBlockAt(spawnLoc).getChunk()))) {

                spawnLoc.setX(spawnLoc.getX() + .5);
                spawnLoc.setZ(spawnLoc.getZ() + .5);

                for (int i2 = 0; i2 < this.amount; i2++) {
                    Class<? extends Entity> entityClass = this.entityType.getEntityClass();

                    if (entityClass.equals(EntityType.MINECART.getEntityClass())) {
                        if (this.data.equalsIgnoreCase("chest")) {
                            entityClass = StorageMinecart.class;
                        }
                        if (this.data.equalsIgnoreCase("furnace")) {
                            entityClass = PoweredMinecart.class;
                        }
                    }

                    Entity ent = getWorld().spawn(spawnLoc, entityClass);

                    if (ent instanceof LivingEntity) {
                        LivingEntity c = (LivingEntity) ent;
                        if (c instanceof Colorable) {
                            boolean flag = false;
                            if (c instanceof Sheep) {
                                if (this.data.equalsIgnoreCase("sheared")) {
                                    flag = true;
                                }
                            }
                            if (flag) {
                                Sheep sheep = (Sheep) c;

                                sheep.setSheared(true);

                                continue;
                            }
                            else {
                                byte color = (byte) 0;
                                try {
                                    color = (byte) Math.min(Integer.parseInt(this.data), 16);
                                }
                                catch (Exception e) {
                                    color = (byte) 0;
                                }

                                if (color <= 0) {
                                    continue;
                                }

                                Colorable cle = (Colorable) c;
                                cle.setColor(DyeColor.getByData(color));
                            }
                        }
                        if (c instanceof Slime) {
                            Slime s = (Slime) c;
                            int size = 1;
                            try {
                                size = Math.min(Integer.parseInt(this.data), 16);
                            }
                            catch (Exception e) {
                                size = 1;
                            }

                            if (size <= 1) {
                                continue;
                            }
                            s.setSize(size);
                        }
                        if (c instanceof Creeper) {
                            if (this.data.equalsIgnoreCase("powered")) {
                                Creeper creeper = (Creeper) c;

                                creeper.setPowered(true);

                                continue;
                            }
                        }
                        if (c instanceof PigZombie) {
                            if (this.data.equalsIgnoreCase("angry")) {
                                PigZombie pigzombie = (PigZombie) c;

                                pigzombie.setAngry(true);

                                continue;
                            }
                            else {
                                int level = 0;
                                try {
                                    level = Integer.parseInt(this.data);
                                }
                                catch (Exception e) {
                                    level = 0;
                                }

                                if (level <= 0) {
                                    continue;
                                }
                                PigZombie pigzombie = (PigZombie) c;
                                pigzombie.setAnger(level);
                            }
                        }
                        if (c instanceof Ageable) {
                            if (this.data.equalsIgnoreCase("baby")) {
                                Ageable ageable = (Ageable) c;

                                ageable.setBaby();

                                continue;
                            }
                        }
                        if (c instanceof Pig) {
                            if (this.data.equalsIgnoreCase("saddled")) {
                                Pig pig = (Pig) c;

                                pig.setSaddle(true);

                                continue;
                            }
                        }
                    }
                    if (this.data.equalsIgnoreCase("ignite")) {
                        ent.setFireTicks(100);

                        continue;
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void setNBTData(NBTBase tag) {}

    @Override
    public NBTBase getNBTData() {
        return new NBTTagInt(0);
    }

    private EntityType entityType;
    private Direction spawnDir;
    private Integer amount = 1;
    private String data = "";

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {

        boolean ctypeError = false;

        String cline = this.getLines(event)[1];

        String[] dline = this.getLines(event)[2].split(" ");

        try {
            this.entityType = Functions.getCreature(cline);
        }
        catch (Exception e) {
            ctypeError = true;
            if (!reload) {
                this.main.alert(getOwnerName(), "Unknown creature type. Please try again.", ChatColor.RED);
            }
            return false;
        }

        if (ctypeError || (this.entityType == null)) {
            if (!reload) {
                this.main.alert(getOwnerName(), "Unknown creature type. Please try again.", ChatColor.RED);
            }
            event.setCancelled(true);
            return false;
        }

        String amountLine = dline[0];

        try {
            this.amount = Integer.parseInt(amountLine);
        }
        catch (Exception ex) {
            this.amount = 1;
        }

        if (this.amount <= 0) {
            this.amount = 1;
        }

        if (this.amount >= 50) {
            this.amount = 50;
        }

        if (dline.length < 2) {
            this.data = "";
        }
        else {
            this.data = dline[1];
        }

        if (this.data.equalsIgnoreCase("")) {
            this.data = "0";
        }

        String dirLine = this.getLines(event)[3];

        if (dirLine.equals("") || (dirLine == null)) {
            this.spawnDir = Direction.UP;
        }
        else {
            this.spawnDir = Direction.fromString(dirLine.toUpperCase());
            if ((this.spawnDir == Direction.ERROR) || (this.spawnDir == null)) {
                this.spawnDir = Direction.UP;
            }
        }

        if (!reload) {
            this.clearArgLines();
        }

        String ncline = this.entityType.getTypeId() + "";

        if (!reload) {
            this.setLine(1, ncline, event);
            this.setLine(2, (this.amount + " " + this.data).substring(0, Math.min(16, (this.amount + " " + this.data).length())), event);
            this.setLine(3, this.spawnDir.toString(), event);
        }

        this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
        if (!reload) {
            init("Spawn sign accepted.");
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
