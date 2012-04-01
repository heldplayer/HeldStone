package dsiwars.co.cc.HeldStone.sign.spawn;

/*
 * This code is Copyright (C) 2011 Chris Bode, Some Rights Reserved.
 * Copyright (C) 1999-2002 Technical Pursuit Inc., All Rights Reserved. Patent
 * Pending, Technical Pursuit Inc.
 * Unless explicitly acquired and licensed from Licensor under the Technical
 * Pursuit License ("TPL") Version 1.0 or greater, the contents of this file are
 * subject to the Reciprocal Public License ("RPL") Version 1.1, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in
 * either source code or executable form, except in compliance with the terms
 * and conditions of the RPL.
 * You may obtain a copy of both the TPL and the RPL (the "Licenses") from
 * Technical Pursuit Inc. at http://www.technicalpursuit.com.
 * All software distributed under the Licenses is provided strictly on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND
 * TECHNICAL PURSUIT INC. HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING
 * WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the Licenses
 * for specific language governing rights and limitations under the Licenses.
 */
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

public class CuboidSign extends HeldSign {

	protected void triggersign(TriggerType type, Object args) {
		int mat = lmat;
		byte dat = ldat;

		BlockRedstoneEvent event = (BlockRedstoneEvent) args;
		InputState is = this.getInput(1, event);

		if (is == InputState.HIGH) {
			mat = hmat;
			dat = hdat;
		}

		Functions.drawSafeCuboid(mat, dat, this.getWorld(), x1, y1, z1, x2, y2, z2);
	}

	@Override
	protected void setNBTData(NBTBase tag) {
		data = (NBTTagCompound) tag;
	}

	@Override
	public NBTBase getNBTData() {
		return data;
	}

	NBTTagCompound data;
	int x1, y1, z1, x2, y2, z2, lmat, hmat = 0;
	byte ldat, hdat = (byte) 0;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		if (this.getLines(event)[1].equals("") || this.getLines(event)[2].equals("")) {
			if (this.main.players.exists(this.getOwnerName())) {
				HeldPlayer p = this.main.players.get(this.getOwnerName());

				if (p.l1 && (p.loc2 != null)) {
					x1 = p.loc1.getBlockX() - this.getHostLocation().getBlockX();
					y1 = p.loc1.getBlockY() - this.getHostLocation().getBlockY();
					z1 = p.loc1.getBlockZ() - this.getHostLocation().getBlockZ();

					x2 = p.loc2.getBlockX() - this.getHostLocation().getBlockX();
					y2 = p.loc2.getBlockY() - this.getHostLocation().getBlockY();
					z2 = p.loc2.getBlockZ() - this.getHostLocation().getBlockZ();
				} else {
					this.main.alert(this.getOwnerName(), "You did not supply any arguments. You must either designate the cuboid area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
					if (!reload) {
						event.setCancelled(true);
					}
					return false;
				}
			} else {
				this.main.alert(this.getOwnerName(), "You did not supply any arguments. You must either designate the cuboid area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		} else {
			String[] split1, split2;
			split1 = this.getLines(event)[1].split(" ");
			split2 = this.getLines(event)[2].split(" ");

			try {
				x1 = Integer.parseInt(split1[0]);
				y1 = Integer.parseInt(split1[1]);
				z1 = Integer.parseInt(split1[2]);

				x2 = Integer.parseInt(split2[0]);
				y2 = Integer.parseInt(split2[1]);
				z2 = Integer.parseInt(split2[2]);
			} catch (Exception e) {
				this.main.alert(this.getOwnerName(), "The coordinates you specified are either invalid or not formatted properly.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		}

		if (!reload) {
			this.setLine(1, x1 + " " + y1 + " " + z1, event);
			this.setLine(2, x2 + " " + y2 + " " + z2, event);
		}

		x1 += this.getHostLocation().getBlockX();
		y1 += this.getHostLocation().getBlockY();
		z1 += this.getHostLocation().getBlockZ();

		x2 += this.getHostLocation().getBlockX();
		y2 += this.getHostLocation().getBlockY();
		z2 += this.getHostLocation().getBlockZ();

		int csize = Math.abs((Math.abs(x1 - x2) + 1) * (Math.abs(y1 - y2) + 1) * (Math.abs(z1 - z2) + 1));

		if (csize > this.main.cfgMaxCuboidBlocks) {
			if (!reload) {
				this.main.alert(this.getOwnerName(), "The cuboid you specified was " + csize + " blocks big. The maximum acceptable area is " + this.main.cfgMaxCuboidBlocks + " blocks.", ChatColor.RED);
				event.setCancelled(true);
			}
			return false;
		}

		int matHigh, matLow;
		matHigh = -1;
		matLow = -1;

		String matLine = this.getLines(event)[3];
		String matArgs[] = matLine.split(" ");

		try {
			matLow = Integer.parseInt(matArgs[1].split(":")[0]);
			matLow = validate(matLow);
		} catch (Exception e) {
			matLow = -1;
		}

		try {
			matHigh = Integer.parseInt(matArgs[0].split(":")[0]);
			matHigh = validate(matHigh);
		} catch (Exception e) {
			matHigh = -1;
		}

		try {
			ldat = Byte.parseByte(matArgs[1].split(":")[1]);
		} catch (Exception e) {
			ldat = -1;
		}

		try {
			hdat = Byte.parseByte(matArgs[0].split(":")[1]);
		} catch (Exception e) {
			hdat = -1;
		}

		lmat = matLow;
		hmat = matHigh;

		if (!reload) {
			if (!Functions.isSafeToRemove(lmat)) {
				lmat = -1;
			}

			if (!Functions.isSafeToRemove(hmat)) {
				hmat = -1;
			}
		}

		matLine = "" + hmat + ":" + hdat + " " + lmat + ":" + ldat;

		if (!reload) {
			this.setLine(3, matLine, event);
		}

		if (!reload) {
			int ySize = Math.abs(y1 - y2) + 1;
			int zSize = Math.abs(z1 - z2) + 1;

			this.data = new NBTTagCompound();

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
						NBTTagCompound currentBlock = (NBTTagCompound) new NBTTagCompound().setNameAndGet("block" + ((tempX * zSize + tempZ) * ySize + tempY));

						currentBlock.insert("typeId", this.getWorld().getBlockAt(x, y, z).getTypeId());
						currentBlock.insert("data", this.getWorld().getBlockAt(x, y, z).getData());

						data.insertCompound(currentBlock.getName(), currentBlock);

						tempZ++;
					}
					tempY++;
				}
				tempX++;
			}
		}

		Control = main.bcr.register(this.getWorld(), x1, y1, z1, x2, y2, z2, data);

		if (Control == null) {
			if (!reload) {
				event.setCancelled(true);
				this.main.alert(this.getOwnerName(), "Cuboids can't overlap eachother!", ChatColor.RED);
			} else {
				main.sgc.invalidate(this, "Cuboid is somehow overlapping with another cuboid.");
			}
			return false;
		}

		main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			this.init("Cuboid sign accepted.");
		}

		return true;
	}

	private int validate(int id) {
		if (id == -1 || isValidID(id)) {
			return id;
		} else {
			return -1;
		}
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
		main.bcr.remove(Control);
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.REDSTONE_CHANGE.name();
	}
}