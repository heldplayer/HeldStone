package dsiwars.co.cc.HeldStone;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;

import dsiwars.co.cc.HeldStone.NBT.NBTTagCompound;

public class BlockController {

	private final HeldStone main;
	private final HashSet<BlockControl> Controllers = new HashSet<BlockControl>();

	public BlockController(HeldStone main) {
		this.main = main;
	}

	public void insert(BlockControl Control) {
		if (!Controllers.contains(Control)) {
			Controllers.add(Control);
		}
	}

	public BlockControl register(World world, int x1, int y1, int z1, int x2, int y2, int z2, NBTTagCompound blockData) {
		Location loc1 = new Location(world, x1, y1, z1);
		Location loc2 = new Location(world, x2, y2, z2);

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

		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					Location blockLoc = new Location(world, x, y, z);

					if (this.contains(blockLoc)) {
						return null;
					}
				}
			}
		}

		BlockControl Control = new BlockControl(loc1, loc2, main, blockData);

		insert(Control);

		return Control;
	}

	public void remove(BlockControl Control) {
		if (Controllers.contains(Control)) {
			Control.restore();
			Controllers.remove(Control);
		}
	}

	public boolean contains(Location loc) {
		boolean containsit = false;

		Iterator<BlockControl> it = Controllers.iterator();

		while (it.hasNext()) {
			BlockControl control = it.next();

			if (control.contains(loc)) {
				containsit = true;
				break;
			}
		}

		return containsit;
	}
}