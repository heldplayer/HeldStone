
package dsiwars.co.cc.HeldStone.sign.functional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.BlockControl;
import dsiwars.co.cc.HeldStone.Functions;
import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagCompound;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class AreaToggleSign extends HeldSign {

    private boolean lastState = false;

    @Override
    protected void triggersign(TriggerType type, Object args) {
        BlockRedstoneEvent event = (BlockRedstoneEvent) args;
        InputState is = this.getInput(1, event);

        if (is == InputState.HIGH && this.lastState) {
            return;
        }
        else if (is == InputState.LOW && !this.lastState) {
            return;
        }
        else {
            this.lastState = (is == InputState.HIGH) ? true : false;
        }

        if (this.lastState) {
            this.Control.restore();
        }
        else {
            Functions.drawSafeCuboid(0, (byte) 0, getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
        }
    }

    @Override
    protected void setNBTData(NBTBase tag) {
        this.data = (NBTTagCompound) tag;
    }

    @Override
    public NBTBase getNBTData() {
        return this.data;
    }

    NBTTagCompound data;
    int x1, y1, z1, x2, y2, z2 = 0;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {
        if (this.getLines(event)[1].equals("") || this.getLines(event)[2].equals("")) {
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
                    this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the cuboid area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
                    if (!reload) {
                        event.setCancelled(true);
                    }
                    return false;
                }
            }
            else {
                this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the cuboid area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }
        else {
            String[] split1, split2;
            split1 = this.getLines(event)[1].split(" ");
            split2 = this.getLines(event)[2].split(" ");

            try {
                this.x1 = Integer.parseInt(split1[0]);
                this.y1 = Integer.parseInt(split1[1]);
                this.z1 = Integer.parseInt(split1[2]);

                this.x2 = Integer.parseInt(split2[0]);
                this.y2 = Integer.parseInt(split2[1]);
                this.z2 = Integer.parseInt(split2[2]);
            }
            catch (Exception e) {
                this.main.alert(getOwnerName(), "The coordinates you specified are either invalid or not formatted properly.", ChatColor.RED);
                if (!reload) {
                    event.setCancelled(true);
                }
                return false;
            }
        }

        if (!reload) {
            this.setLine(1, this.x1 + " " + this.y1 + " " + this.z1, event);
            this.setLine(2, this.x2 + " " + this.y2 + " " + this.z2, event);
        }

        this.x1 += getHostLocation().getBlockX();
        this.y1 += getHostLocation().getBlockY();
        this.z1 += getHostLocation().getBlockZ();

        this.x2 += getHostLocation().getBlockX();
        this.y2 += getHostLocation().getBlockY();
        this.z2 += getHostLocation().getBlockZ();

        int csize = Math.abs((Math.abs(this.x1 - this.x2) + 1) * (Math.abs(this.y1 - this.y2) + 1) * (Math.abs(this.z1 - this.z2) + 1));

        if (csize > this.main.cfgMaxCuboidBlocks) {
            if (!reload) {
                this.main.alert(getOwnerName(), "The cuboid you specified was " + csize + " blocks big. The maximum acceptable area is " + this.main.cfgMaxCuboidBlocks + " blocks.", ChatColor.RED);
                event.setCancelled(true);
            }
            return false;
        }

        if (!reload) {
            int ySize = Math.abs(this.y1 - this.y2) + 1;
            int zSize = Math.abs(this.z1 - this.z2) + 1;

            this.data = new NBTTagCompound();

            int minX = Math.min(this.x1, this.x2);
            int minY = Math.min(this.y1, this.y2);
            int minZ = Math.min(this.z1, this.z2);

            int maxX = Math.max(this.x1, this.x2);
            int maxY = Math.max(this.y1, this.y2);
            int maxZ = Math.max(this.z1, this.z2);

            int tempX = 0;
            for (int x = minX; x <= maxX; x++) {
                int tempY = 0;
                for (int y = minY; y <= maxY; y++) {
                    int tempZ = 0;
                    for (int z = minZ; z <= maxZ; z++) {
                        NBTTagCompound currentBlock = (NBTTagCompound) new NBTTagCompound().setNameAndGet("block" + ((tempX * zSize + tempZ) * ySize + tempY));

                        currentBlock.insert("typeId", getWorld().getBlockAt(x, y, z).getTypeId());
                        currentBlock.insert("data", getWorld().getBlockAt(x, y, z).getData());

                        this.data.insertCompound(currentBlock.getName(), currentBlock);

                        tempZ++;
                    }
                    tempY++;
                }
                tempX++;
            }
        }

        this.Control = this.main.bcr.register(getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2, this.data);

        if (this.Control == null) {
            if (!reload) {
                event.setCancelled(true);
                this.main.alert(getOwnerName(), "Cuboids can't overlap eachother!", ChatColor.RED);
            }
            else {
                this.main.sgc.invalidate(this, "Cuboid is somehow overlapping with another cuboid.");
            }
            return false;
        }

        this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
        if (!reload) {
            init("AreaToggle sign accepted.");
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
        return TriggerType.REDSTONE_CHANGE.name();
    }
}
