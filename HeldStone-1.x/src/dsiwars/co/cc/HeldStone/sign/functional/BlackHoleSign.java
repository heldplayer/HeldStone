package dsiwars.co.cc.HeldStone.sign.functional;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class BlackHoleSign extends HeldSign {

	private boolean lastState = false;

	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is != InputState.HIGH) {
			lastState = false;
			return;
		} else {
			if (lastState == true) {
				return;
			}
			lastState = true;
		}

		List<Entity> entities = this.getWorld().getEntities();

		Iterator<Entity> entityIterator = entities.iterator();

		blockLocation.getBlock().setTypeIdAndData(0, (byte) 0, false);

		while (entityIterator.hasNext()) {
			Entity currentEntity = entityIterator.next();

			if (currentEntity.getLocation().getBlockX() == blockLocation.getBlockX() && currentEntity.getLocation().getBlockY() == blockLocation.getBlockY() && currentEntity.getLocation().getBlockZ() == blockLocation.getBlockZ()) {
				if (currentEntity instanceof LivingEntity) {
					LivingEntity currentLivingEntity = (LivingEntity) currentEntity;

					currentLivingEntity.setHealth(0);
				} else {
					currentEntity.remove();
				}
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

	Location blockLocation;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		int x = 0, y = 0, z = 0;

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
			String[] split;
			split = this.getLines(event)[1].split(" ");

			try {
				x = Integer.parseInt(split[0]);
				y = Integer.parseInt(split[1]);
				z = Integer.parseInt(split[2]);
			} catch (Exception e) {
				this.main.alert(this.getOwnerName(), "The coordinate you specified is either invalid or not formatted properly.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		}

		x += this.getHostLocation().getBlockX();
		y += this.getHostLocation().getBlockY();
		z += this.getHostLocation().getBlockZ();

		blockLocation = new Location(this.getWorld(), x, y, z);

		x -= this.getHostLocation().getBlockX();
		y -= this.getHostLocation().getBlockY();
		z -= this.getHostLocation().getBlockZ();

		if (!reload) {
			this.setLine(1, x + " " + y + " " + z, event);
		}

		main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			this.init("BlackHole sign accepted.");
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