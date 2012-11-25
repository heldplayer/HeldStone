package me.heldplayer.HeldStone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;

import me.heldplayer.HeldStone.sign.HeldSign;
import me.heldplayer.HeldStone.sign.SignGroup;
import me.heldplayer.HeldStone.sign.TriggerType;
import me.heldplayer.util.Direction;

import com.mojang.NBT.CompressedStreamTools;
import com.mojang.NBT.NBTBase;
import com.mojang.NBT.NBTTagCompound;

public class SignManager {
	private HeldStone main;
	private final SignGroup[] groups = new SignGroup[TriggerType.values().length];
	private NBTTagCompound SignCompound;
	private List<NBTTagCompound> missingSigns;
	private int loaded = 0, unloaded = 0;

	public SignManager(HeldStone main) {
		this.main = main;
		missingSigns = new ArrayList<NBTTagCompound>();
		for (int i = 0; i < TriggerType.values().length; i++) {
			groups[i] = new SignGroup(TriggerType.values()[i], this.main);
		}
	}

	public void announce() {
		HeldStone.info("Loaded signs: " + loaded);
		HeldStone.info("Unloaded signs: " + unloaded);
		HeldStone.info("Total: " + (loaded + unloaded));
	}

	public void register(HeldSign sign, TriggerType type) {
		for (SignGroup group : groups) {
			if (group.isType(type)) {
				group.add(sign);
			}
		}
	}

	public HeldSign signAtLocation(Location loc) {
		List<HeldSign> signs = getAllSigns();

		for (HeldSign sign : signs) {
			if (sign.getLocation().equals(loc)) {
				return sign;
			}
		}
		return null;
	}

	public boolean isSignAtLocation(Location loc) {
		List<HeldSign> signs = getAllSigns();

		for (HeldSign sign : signs) {
			if (sign.getLocation().equals(loc)) {
				return true;
			}
		}
		return false;
	}

	public void invalidate(HeldSign sign, String reason) {
		HeldStone.info("Invalidating sign at (" + sign.getLocation().getBlockX() + ", " + sign.getLocation().getBlockY() + ", " + sign.getLocation().getBlockZ() + ")");
		Chunk signChunk = sign.getLocation().getChunk();
		HeldStone.info("\tIn Chunk: (" + signChunk.getX() + ", " + signChunk.getZ() + ") and World " + sign.getLocation().getWorld().getName());
		HeldStone.info("\t" + reason);
		try {
			sign.invalidate();
		} catch (Exception ex) {
			HeldStone.warning("Error invalidating sign: " + ex.getMessage());
		}
		for (SignGroup group : groups) {
			try {
				group.invalidate(sign);
			} catch (Exception ex) {
				HeldStone.warning("Error invalidating sign: " + ex.getMessage());
			}
		}
	}

	public void trigger(TriggerType type, Object args) {
		for (SignGroup s : groups) {
			if (s.getType() == type) {
				s.trigger(args);
			}
		}
	}

	public final static String signFile = "/signs.dat";

	public void load() {
		loaded = 0;
		unloaded = 0;
		FileInputStream FIS = null;

		try {
			FIS = new FileInputStream(new File(this.main.getDataFolder(), signFile));
		} catch (Exception e) {
			HeldStone.info("No sign data file found or error while loading data file.");
			return;
		}

		SignCompound = CompressedStreamTools.load(FIS);

		if (SignCompound == null) {
			SignCompound = (NBTTagCompound) new NBTTagCompound().setNameAndGet("signs");
		}

		Iterator<NBTBase> i = SignCompound.getCollection().iterator();

		while (i.hasNext()) {
			NBTBase base = (NBTBase) i.next();

			if (base instanceof NBTTagCompound) {
				NBTTagCompound signCompound = (NBTTagCompound) base;

				String world = signCompound.getString("world");

				if (this.main.getServer().getWorld(world) == null) {
					missingSigns.add(signCompound);
					unloaded++;
					continue;
				}

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

				if (HeldSign.create(lines, owner, null, l, facing, data, main, true, null)) {
					loaded++;
				} else {
					unloaded++;
				}
			}
		}
		HeldStone.info("Signs loaded");
		try {
			FIS.close();
		} catch (IOException ex) {
			main.getLogger().log(Level.WARNING, "Failed closing FileInputStream");
			ex.printStackTrace();
		}
	}

	public void save() {
		File dir = main.getDataFolder();

		if (!dir.exists()) {
			dir.mkdirs();
		}

		File sdat = new File(main.getDataFolder(), signFile);

		if (!sdat.exists()) {
			try {
				sdat.createNewFile();
			} catch (IOException e) {
				HeldStone.warning("Could not save signs: " + e.getMessage());
			}
		}

		FileOutputStream FOS = null;

		try {
			FOS = new FileOutputStream(sdat);
		} catch (Exception e) {
			HeldStone.warning("Could not save signs: " + e.getMessage());
			return;
		}

		SignCompound = (NBTTagCompound) new NBTTagCompound().setNameAndGet("signs");

		Iterator<HeldSign> signs = this.getAllSigns().iterator();

		while (signs.hasNext()) {
			HeldSign currentSign = signs.next();

			NBTTagCompound CurrentSingCompound = new NBTTagCompound();

			CurrentSingCompound.insert("world", currentSign.getLocation().getWorld().getName());
			CurrentSingCompound.insert("posx", currentSign.getLocation().getBlockX());
			CurrentSingCompound.insert("posy", currentSign.getLocation().getBlockY());
			CurrentSingCompound.insert("posz", currentSign.getLocation().getBlockZ());
			CurrentSingCompound.insert("direction", currentSign.getDirection().name());
			CurrentSingCompound.insert("owner", currentSign.getOwner());
			CurrentSingCompound.insert("line1", currentSign.getLine(0));
			CurrentSingCompound.insert("line2", currentSign.getLine(1));
			CurrentSingCompound.insert("line3", currentSign.getLine(2));
			CurrentSingCompound.insert("line4", currentSign.getLine(3));
			CurrentSingCompound.insert("data", currentSign.getData());

			SignCompound.insertCompound(currentSign.getLocation().getWorld().getName() + ":" + currentSign.getLocation().getBlockX() + "," + currentSign.getLocation().getBlockY() + "," + currentSign.getLocation().getBlockZ(), CurrentSingCompound);
		}

		for (NBTTagCompound compound : missingSigns) {
			SignCompound.insertCompound(compound.getName(), compound);
		}

		CompressedStreamTools.save(SignCompound, FOS);

		HeldStone.info("[SAVING] Signs saved");
		try {
			FOS.close();
		} catch (IOException ex) {
			main.getLogger().log(Level.WARNING, "Failed closing FileInputStream");
			ex.printStackTrace();
		}
	}

	public List<HeldSign> getAllSigns() {
		List<HeldSign> signs = new ArrayList<HeldSign>();

		for (SignGroup group : groups) {
			for (HeldSign sign : group.getSigns()) {
				if (!signs.contains(sign)) {
					signs.add(sign);
				}
			}
		}

		return signs;
	}
}
