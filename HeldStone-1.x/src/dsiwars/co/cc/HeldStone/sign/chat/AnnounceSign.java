package dsiwars.co.cc.HeldStone.sign.chat;

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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagString;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class AnnounceSign extends HeldSign {

	private boolean lastState = false;

	@Override
	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is != InputState.HIGH) {
			this.lastState = false;
			return;
		} else {
			if (this.lastState == true) {
				return;
			}
			this.lastState = true;
		}

		for (Player p : this.main.getServer().getOnlinePlayers()) {
			Location l = p.getLocation();

			boolean x, y, z;

			x = (Math.min(this.x1, this.x2) <= l.getBlockX()) && (l.getBlockX() <= Math.max(this.x1, this.x2));
			y = (Math.min(this.y1, this.y2) <= l.getBlockY()) && (l.getBlockY() <= Math.max(this.y1, this.y2));
			z = (Math.min(this.z1, this.z2) <= l.getBlockZ()) && (l.getBlockZ() <= Math.max(this.z1, this.z2));

			if (x && y && z) {
				this.main.alert(p.getName(), this.message, this.color);
				break;
			}
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
		this.message = ((NBTTagString) tag).value;
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagString(this.message);
	}

	private int x1, y1, z1, x2, y2, z2;
	String message = null;
	ChatColor color = ChatColor.WHITE;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {

		if (!reload) {
			this.message = this.main.players.safelyGet(getOwnerName(), this.main).message;
			if (this.message == null) {
				this.main.alert(getOwnerName(), "You must set a message first. " + ChatColor.AQUA + "/hs msg <message>", ChatColor.RED);
				return false;
			}
		}

		if (this.getLines()[1].equals("") && this.getLines()[2].equals("")) {
			if (this.main.players.exists(getOwnerName())) {
				HeldPlayer p = this.main.players.get(getOwnerName());

				if (p.l1 && (p.loc2 != null)) {
					this.x1 = p.loc1.getBlockX() - getHostLocation().getBlockX();
					this.y1 = p.loc1.getBlockY() - getHostLocation().getBlockY();
					this.z1 = p.loc1.getBlockZ() - getHostLocation().getBlockZ();

					this.x2 = p.loc2.getBlockX() - getHostLocation().getBlockX();
					this.y2 = p.loc2.getBlockY() - getHostLocation().getBlockY();
					this.z2 = p.loc2.getBlockZ() - getHostLocation().getBlockZ();
				} else {
					this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the announce area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
					if (!reload) {
						event.setCancelled(true);
					}
					return false;
				}
			} else {
				this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the announce area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}

		} else {
			String[] split1, split2;
			split1 = this.getLines()[1].split(" ");
			split2 = this.getLines()[2].split(" ");

			try {
				this.x1 = Integer.parseInt(split1[0]);
				this.y1 = Integer.parseInt(split1[1]);
				this.z1 = Integer.parseInt(split1[2]);

				this.x2 = Integer.parseInt(split2[0]);
				this.y2 = Integer.parseInt(split2[1]);
				this.z2 = Integer.parseInt(split2[2]);
			} catch (Exception e) {
				this.main.alert(getOwnerName(), "The coordinates you specified are either invalid or formatted incorrectly.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		}

		try {
			this.color = ChatColor.valueOf(this.getLines()[3].toUpperCase());
		} catch (Exception ex) {
			try {
				this.color = ChatColor.getByChar(this.getLines()[3].charAt(0));
			} catch (Exception ex2) {
				this.color = ChatColor.WHITE;
			}
		}

		if (!reload) {
			this.clearArgLines(event);
			this.setLine(1, this.x1 + " " + this.y1 + " " + this.z1, event);
			this.setLine(2, this.x2 + " " + this.y2 + " " + this.z2, event);
			this.setLine(3, this.color.name(), event);
		}

		this.x1 += getHostLocation().getBlockX();
		this.y1 += getHostLocation().getBlockY();
		this.z1 += getHostLocation().getBlockZ();

		this.x2 += getHostLocation().getBlockX();
		this.y2 += getHostLocation().getBlockY();
		this.z2 += getHostLocation().getBlockZ();

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("Announce sign accepted.");
		}
		return true;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.REDSTONE_CHANGE.name();
	}
}