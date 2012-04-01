package dsiwars.co.cc.HeldStone;

import org.bukkit.Location;

import dsiwars.co.cc.HeldStone.NBT.NBTTagCompound;

public class BlockControl {

	private Location location1;
	private Location location2;
	private HeldStone main;
	private NBTTagCompound blockData;

	public BlockControl(Location loc1, Location loc2, HeldStone plugin, NBTTagCompound blockData) {
		this.location1 = loc1;
		this.location2 = loc2;
		this.main = plugin;
		this.blockData = blockData;
	}

	public boolean contains(Location loc) {
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		int x1 = location1.getBlockX();
		int y1 = location1.getBlockY();
		int z1 = location1.getBlockZ();
		int x2 = location2.getBlockX();
		int y2 = location2.getBlockY();
		int z2 = location2.getBlockZ();

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

		boolean returned = true;

		if (x < x1) {
			returned = false;
		}
		if (x > x2) {
			returned = false;
		}
		if (y < y1) {
			returned = false;
		}
		if (y > y2) {
			returned = false;
		}
		if (z < z1) {
			returned = false;
		}
		if (z > z2) {
			returned = false;
		}

		return returned;
	}

	public void restore() {
		int x1 = location1.getBlockX();
		int y1 = location1.getBlockY();
		int z1 = location1.getBlockZ();
		int x2 = location2.getBlockX();
		int y2 = location2.getBlockY();
		int z2 = location2.getBlockZ();

		int ySize = Math.abs(y1 - y2) + 1;
		int zSize = Math.abs(z1 - z2) + 1;

		int minX = Math.min(x1, x2);
		int minY = Math.min(y1, y2);
		int minZ = Math.min(z1, z2);

		int maxX = Math.max(x1, x2);
		int maxY = Math.max(y1, y2);
		int maxZ = Math.max(z1, z2);

		int tempX = 0;
		for (int x = minX; x <= maxX; x++) {
			int tempY = 0;
			for (int y = minY; y <= maxY; y++) {
				int tempZ = 0;
				for (int z = minZ; z <= maxZ; z++) {
					if (Functions.isSafeToRemove(this.location1.getWorld().getBlockAt(x, y, z).getTypeId())) {
						try {
							this.location1.getWorld().getBlockAt(x, y, z).setTypeIdAndData(this.blockData.getCompound("block" + ((tempX * zSize + tempZ) * ySize + tempY)).getInt("typeId"), this.blockData.getCompound("block" + ((tempX * zSize + tempZ) * ySize + tempY)).getByte("data"), false);
						} catch (Exception ex) {
							main.e("Error while restoring cuboid block (Corrupt data?): " + ex.getMessage());
							main.e("  At (" + x + "; " + y + "; " + z + ") Chunk: (" + this.location1.getWorld().getBlockAt(x, y, z).getChunk().getX() + "; " + this.location1.getWorld().getBlockAt(x, y, z).getChunk().getZ() + ")");
						}
					}
					tempZ++;
				}
				tempY++;
			}
			tempX++;
		}
	}
}