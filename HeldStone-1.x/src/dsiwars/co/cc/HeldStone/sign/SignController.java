
package dsiwars.co.cc.HeldStone.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.Location;

import dsiwars.co.cc.HeldStone.Direction;
import dsiwars.co.cc.HeldStone.HeldStone;
import dsiwars.co.cc.HeldStone.NBT.CompressedStreamTools;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagCompound;

public class SignController {

    private final HeldStone main;
    private final SignGroup[] groups = new SignGroup[TriggerType.values().length];
    private NBTTagCompound SignCompound;

    public SignController(HeldStone main) {
        this.main = main;
        for (int i = 0; i < TriggerType.values().length; i++) {
            this.groups[i] = new SignGroup(TriggerType.values()[i], this.main);
        }
    }

    public void register(HeldSign sign, TriggerType type) {
        for (SignGroup group : this.groups) {
            if (group.isType(type)) {
                group.add(sign);
            }
        }
    }

    public void invalidate(HeldSign sign, String reason) {
        this.main.i("Invalidating sign at (" + sign.getLocation().getBlockX() + ", " + sign.getLocation().getBlockY() + ", " + sign.getLocation().getBlockZ() + ")");
        Chunk signChunk = sign.getWorld().getChunkAt(sign.getLocation());
        this.main.i("\tIn Chunk: (" + signChunk.getX() + ", " + signChunk.getZ() + ")");
        this.main.i("\t" + reason);
        try {
            sign.invalidate();
        }
        catch (Exception ex) {
            this.main.e("Error invalidating sign: " + ex.getMessage());
        }
        for (SignGroup group : this.groups) {
            try {
                group.invalidate(sign);
            }
            catch (Exception ex) {
                this.main.e("Error invalidating sign: " + ex.getMessage());
            }
        }
        try {
            this.main.tickctrl.purge(sign);
        }
        catch (Exception ex) {
            this.main.e("Error invalidating sign: " + ex.getMessage());
        }
    }

    public void trigger(TriggerType type, Object args) {
        for (SignGroup s : this.groups) {
            if (s.getType() == type) {
                s.trigger(args);
            }
        }
    }

    public final static String signFile = "/signs.dat";

    public void load() {
        FileInputStream FIS = null;

        try {
            FIS = new FileInputStream(new File(this.main.dataPath, signFile));
        }
        catch (Exception e) {
            this.main.i("[LOADING] No sign data file found.");
            return;
        }

        this.SignCompound = CompressedStreamTools.load(FIS);

        if (this.SignCompound == null) {
            this.SignCompound = (NBTTagCompound) new NBTTagCompound().setNameAndGet("signs");
        }

        Iterator<NBTBase> i = this.SignCompound.getCollection().iterator();

        while (i.hasNext()) {
            NBTBase base = i.next();

            if (base instanceof NBTTagCompound) {
                NBTTagCompound signCompound = (NBTTagCompound) base;

                String world = signCompound.getString("world");

                int x = signCompound.getInt("posx");
                int y = signCompound.getInt("posy");
                int z = signCompound.getInt("posz");

                Location l = new Location(this.main.getServer().getWorld(world), x, y, z);

                Direction facing = Direction.fromString(signCompound.getString("direction"));

                String owner = signCompound.getString("owner");

                String[] lines = new String[4];
                lines[0] = signCompound.getString("line1");
                lines[1] = signCompound.getString("line2");
                lines[2] = signCompound.getString("line3");
                lines[3] = signCompound.getString("line4");

                NBTBase data = signCompound.getTag("data");

                HeldSign.signFactory(lines, owner, data, world, l, facing, true, null, this.main);
            }
        }
        this.main.i("[LOADING] Signs loaded");
        try {
            FIS.close();
        }
        catch (IOException ex) {
            Logger.getLogger(SignController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveNBT() {
        File dir = new File(this.main.dataPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File sdat = new File(this.main.dataPath, signFile);

        if (!sdat.exists()) {
            try {
                sdat.createNewFile();
            }
            catch (IOException e) {
                this.main.e("[SAVING] Could not save signs:");
                this.main.e(e.getMessage());
            }
        }

        FileOutputStream FOS = null;

        try {
            FOS = new FileOutputStream(sdat);
        }
        catch (Exception e) {
            this.main.e("[SAVING] Could not save signs:");
            this.main.e(e.getMessage());
            return;
        }

        this.SignCompound = (NBTTagCompound) new NBTTagCompound().setNameAndGet("signs");

        Iterator<HeldSign> signs = getAllSigns().iterator();

        while (signs.hasNext()) {
            HeldSign currentSign = signs.next();

            NBTTagCompound CurrentSingCompound = new NBTTagCompound();

            CurrentSingCompound.insert("world", currentSign.getWorld().getName());
            CurrentSingCompound.insert("posx", currentSign.getLocation().getBlockX());
            CurrentSingCompound.insert("posy", currentSign.getLocation().getBlockY());
            CurrentSingCompound.insert("posz", currentSign.getLocation().getBlockZ());
            CurrentSingCompound.insert("direction", currentSign.getFacing().name());
            CurrentSingCompound.insert("owner", currentSign.getOwnerName());
            CurrentSingCompound.insert("line1", currentSign.getLines()[0]);
            CurrentSingCompound.insert("line2", currentSign.getLines()[1]);
            CurrentSingCompound.insert("line3", currentSign.getLines()[2]);
            CurrentSingCompound.insert("line4", currentSign.getLines()[3]);
            CurrentSingCompound.insert("data", currentSign.getNBTData());

            this.SignCompound.insertCompound(currentSign.getWorld().getName() + ":" + currentSign.getLocation().getBlockX() + "," + currentSign.getLocation().getBlockY() + "," + currentSign.getLocation().getBlockZ(), CurrentSingCompound);
        }

        CompressedStreamTools.save(this.SignCompound, FOS);

        this.main.i("[SAVING] Signs saved");
        try {
            FOS.close();
        }
        catch (IOException ex) {
            Logger.getLogger(SignController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<HeldSign> getAllSigns() {
        ArrayList<HeldSign> all = new ArrayList<HeldSign>();

        ArrayList<HeldSign> gsigns;

        for (SignGroup g : this.groups) {
            gsigns = g.getSigns();

            for (int i = 0; i < gsigns.size(); i++) {
                if (!all.contains(gsigns.get(i))) {
                    all.add(gsigns.get(i));
                }
            }
        }
        return all;
    }
}
