package dsiwars.co.cc.HeldStone.sign.sensor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class PSensorSign extends HeldSign {

	boolean lastState = false;

	protected void triggersign(TriggerType type, Object args) {
		boolean tripped = false;

		int xmin, xmax, ymin, ymax, zmin, zmax;
		xmin = Math.min(x1, x2);
		xmax = Math.max(x1, x2);

		ymin = Math.min(y1, y2);
		ymax = Math.max(y1, y2);

		zmin = Math.min(z1, z2);
		zmax = Math.max(z1, z2);

		boolean x, y, z;

		Location l;

		Player p = this.main.getServer().getPlayer(PlayerName);

		if (p.isOnline()) {
			l = p.getLocation();

			x = (xmin <= l.getBlockX()) && (l.getBlockX() <= xmax);
			y = (ymin <= l.getBlockY()) && (l.getBlockY() <= ymax);
			z = (zmin <= l.getBlockZ()) && (l.getBlockZ() <= zmax);

			if (x && y && z) {
				tripped = true;
			}
		}

		if (tripped != lastState) {
			lastState = tripped;
			this.setOutput(tripped);
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private int x1, y1, z1, x2, y2, z2;
	private String PlayerName;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		if (this.getLines()[1].equals("") && this.getLines()[2].equals("")) {
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
					this.main.alert(this.getOwnerName(), "You did not supply any arguments. You must either designate the sensor area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
					if (!reload) {
						event.setCancelled(true);
					}
					return false;
				}
			} else {
				this.main.alert(this.getOwnerName(), "You did not supply any arguments. You must either designate the sensor area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
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
				x1 = Integer.parseInt(split1[0]);
				y1 = Integer.parseInt(split1[1]);
				z1 = Integer.parseInt(split1[2]);

				x2 = Integer.parseInt(split2[0]);
				y2 = Integer.parseInt(split2[1]);
				z2 = Integer.parseInt(split2[2]);
			} catch (Exception e) {
				this.main.alert(this.getOwnerName(), "The coordinates you specified are either invalid or formatted incorrectly.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		}

		if (this.getLines()[3].equalsIgnoreCase("")) {
			this.main.alert(this.getOwnerName(), "TYou need to specify a player to look for.", ChatColor.RED);
			if (!reload) {
				event.setCancelled(true);
			}
			return false;
		} else {
			this.PlayerName = this.getLines()[3];
		}

		if (!reload) {
			this.clearArgLines(event);
			this.setLine(1, x1 + " " + y1 + " " + z1, event);
			this.setLine(2, x2 + " " + y2 + " " + z2, event);
			this.setLine(3, PlayerName, event);
		}

		x1 += this.getHostLocation().getBlockX();
		y1 += this.getHostLocation().getBlockY();
		z1 += this.getHostLocation().getBlockZ();

		x2 += this.getHostLocation().getBlockX();
		y2 += this.getHostLocation().getBlockY();
		z2 += this.getHostLocation().getBlockZ();

		main.sgc.register(this, TriggerType.TIMER_HALF_SECOND);
		if (!reload) {
			this.init("PSensor sign accepted.");
		}

		return true;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.TIMER_HALF_SECOND.name();
	}
}