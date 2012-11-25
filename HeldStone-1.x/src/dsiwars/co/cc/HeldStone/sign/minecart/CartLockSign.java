package dsiwars.co.cc.HeldStone.sign.minecart;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class CartLockSign extends HeldSign {

	@Override
	protected void triggersign(TriggerType type, Object args) {
		int xmin, xmax, ymin, ymax, zmin, zmax;
		xmin = Math.min(this.x1, this.x2);
		xmax = Math.max(this.x1, this.x2);

		ymin = Math.min(this.y1, this.y2);
		ymax = Math.max(this.y1, this.y2);

		zmin = Math.min(this.z1, this.z2);
		zmax = Math.max(this.z1, this.z2);

		List<Entity> entities = getWorld().getEntities();

		Iterator<Entity> entityIterator = entities.iterator();

		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			try {
				if (entity instanceof Minecart) {

					boolean x, y, z;

					Location l;

					l = entity.getLocation();

					x = (xmin <= l.getBlockX()) && (l.getBlockX() <= xmax);
					y = (ymin <= l.getBlockY()) && (l.getBlockY() <= ymax);
					z = (zmin <= l.getBlockZ()) && (l.getBlockZ() <= zmax);

					if (x && y && z) {
						Minecart cart = (Minecart) entity;

						if (lock) {
							if (!cart.isEmpty() && cart.getPassenger() instanceof Player) {
								HeldPlayer player = main.players.safelyGet(((Player) cart.getPassenger()).getName(), main);
								
								player.ridingCart = cart;
							}
						} else {
							if (!cart.isEmpty() && cart.getPassenger() instanceof Player) {
								HeldPlayer player = main.players.safelyGet(((Player) cart.getPassenger()).getName(), main);
								
								player.ridingCart = null;
							}
						}

						continue;
					}
				}
			} catch (Exception ex) {
				this.main.e("Error while checking entity location!");
				ex.printStackTrace();
			}
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	int x1, y1, z1, x2, y2, z2;
	boolean lock;

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
				} else {
					this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the cuboid area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
					if (!reload) {
						event.setCancelled(true);
					}
					return false;
				}
			} else {
				this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the cuboid area on lines two and three, or set the cuboid area by right clicking with a piece of lightstone dust.", ChatColor.RED);
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
				this.x1 = Integer.parseInt(split1[0]);
				this.y1 = Integer.parseInt(split1[1]);
				this.z1 = Integer.parseInt(split1[2]);

				this.x2 = Integer.parseInt(split2[0]);
				this.y2 = Integer.parseInt(split2[1]);
				this.z2 = Integer.parseInt(split2[2]);
			} catch (Exception e) {
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

		if (this.getLines()[3].equalsIgnoreCase("")) {
			this.main.alert(getOwnerName(), "You did not specify a speed", ChatColor.RED);
			if (!reload) {
				event.setCancelled(true);
			}
			return false;
		}

		String lockLine = this.getLines()[3];

		if (lockLine.equalsIgnoreCase("true") || lockLine.equalsIgnoreCase("yes")) {
			lockLine = "TRUE";
			lock = true;
		} else if (lockLine.equalsIgnoreCase("false") || lockLine.equalsIgnoreCase("no")) {
			lockLine = "FALSE";
			lock = false;
		} else {
			this.main.alert(getOwnerName(), "You must enter either \"true\" or \"false\" for the lock", ChatColor.RED);
			if (!reload) {
				event.setCancelled(true);
			}
			return false;
		}

		if (!reload) {
			this.setLine(3, lockLine, event);
		}

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("CartLock sign accepted.");
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
