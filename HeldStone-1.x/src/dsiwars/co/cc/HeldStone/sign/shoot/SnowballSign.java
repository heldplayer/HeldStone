package dsiwars.co.cc.HeldStone.sign.shoot;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.Direction;
import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class SnowballSign extends HeldSign {

	protected void triggersign(TriggerType type, Object args) {
		if (args == null) {
			return;
		}
		if (this.getInputId((BlockRedstoneEvent) args) != 1 || this.getInput(1, (BlockRedstoneEvent) args) != InputState.HIGH) {
			return;
		}

		Location spawnLoc = new Location(this.getWorld(), x + 0.5, y + 0.5, z + 0.5);

		Snowball snowball = this.getWorld().spawn(spawnLoc, Snowball.class);

		snowball.setVelocity(Direction.getVelocityVector(spawnDir, this.main.rand, speed));
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private Direction spawnDir;
	int x, y, z = 0;
	Double speed = 1.0;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		if (this.getLines(event)[1].equals("")) {
			if (this.main.players.exists(this.getOwnerName())) {
				HeldPlayer p = this.main.players.get(this.getOwnerName());

				if (p.loc3 != null) {
					x = p.loc3.getBlockX() - this.getHostLocation().getBlockX();
					y = p.loc3.getBlockY() - this.getHostLocation().getBlockY();
					z = p.loc3.getBlockZ() - this.getHostLocation().getBlockZ();
				} else {
					this.main.alert(this.getOwnerName(), "You did not supply any arguments. You must either designate the block location on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
					if (!reload) {
						event.setCancelled(true);
					}
					return false;
				}
			} else {
				this.main.alert(this.getOwnerName(), "You did not supply any arguments. You must either designate the block location on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		} else {
			String[] split1;
			split1 = this.getLines(event)[1].split(" ");

			try {
				x = Integer.parseInt(split1[0]);
				y = Integer.parseInt(split1[1]);
				z = Integer.parseInt(split1[2]);
			} catch (Exception e) {
				this.main.alert(this.getOwnerName(), "The coordinates you specified are either invalid or not formatted properly.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		}

		String dirLine = this.getLines(event)[2];

		if (dirLine.equals("") || (dirLine == null)) {
			spawnDir = Direction.UP;
		} else {
			spawnDir = Direction.fromString(dirLine.toUpperCase());
			if ((spawnDir == Direction.ERROR) || (spawnDir == null)) {
				spawnDir = Direction.UP;
			}
		}

		String speedLine = this.getLines(event)[3];

		if (speedLine.equals("")) {
			speed = 1.0;
		} else {
			try {
				speed = Double.parseDouble(speedLine);
			} catch (Exception ex) {
				speed = 1.0;
			}
		}

		if (!reload) {
			this.clearArgLines();
			this.setLine(1, x + " " + y + " " + z, event);
			this.setLine(2, spawnDir.toString(), event);
			this.setLine(3, speed + "", event);
		}

		x += this.getHostLocation().getBlockX();
		y += this.getHostLocation().getBlockY();
		z += this.getHostLocation().getBlockZ();

		main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			this.init("Snowball sign accepted.");
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