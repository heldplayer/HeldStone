package dsiwars.co.cc.HeldStone.sign.functional;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.HeldPlayer;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class EffectSign extends HeldSign {

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

		getWorld().playEffect(this.hostLocation, this.effectType, this.effectData);
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	Location hostLocation;
	Effect effectType;
	Integer effectData = 0;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {
		int x = 0, y = 0, z = 0;

		if (this.getLines(event)[1].equals("")) {
			if (this.main.players.exists(getOwnerName())) {
				HeldPlayer p = this.main.players.get(getOwnerName());

				if (p.loc3 != null) {
					x = p.loc3.getBlockX() - getHostLocation().getBlockX();
					y = p.loc3.getBlockY() - getHostLocation().getBlockY();
					z = p.loc3.getBlockZ() - getHostLocation().getBlockZ();
				} else {
					this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the block location on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
					if (!reload) {
						event.setCancelled(true);
					}
					return false;
				}
			} else {
				this.main.alert(getOwnerName(), "You did not supply any arguments. You must either designate the block location on line two, or set the block by right clicking with a piece of gunpowder.", ChatColor.RED);
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
				this.main.alert(getOwnerName(), "The coordinates you specified are either invalid or not formatted properly.", ChatColor.RED);
				if (!reload) {
					event.setCancelled(true);
				}
				return false;
			}
		}

		String effectLine = this.getLines()[2];

		try {
			this.effectType = Effect.valueOf(effectLine.toUpperCase());
		} catch (Exception ex) {
			if (!reload) {
				this.main.alert(getOwnerName(), "This is not a valid effect type!", ChatColor.RED);
				event.setCancelled(true);
				return false;
			}
		}
		//Effect.BOW_FIRE;
		//Effect.CLICK1;
		//Effect.CLICK2;
		//Effect.DOOR_TOGGLE;
		//Effect.EXTINGUISH;
		//Effect.RECORD_PLAY;
		//Effect.SMOKE;
		//Effect.STEP_SOUND;
		try {
			this.effectData = Integer.parseInt(this.getLines()[3]);
		} catch (Exception ex) {
			if (!reload) {
				this.main.alert(getOwnerName(), "The suplied data is not valid!", ChatColor.RED);
				event.setCancelled(true);
				return false;
			}
		}

		if (!reload) {
			this.clearArgLines();
			this.setLine(1, x + " " + y + " " + z, event);
			this.setLine(2, effectLine.toString(), event);
			this.setLine(3, this.effectData.toString(), event);
		}

		x += getHostLocation().getBlockX();
		y += getHostLocation().getBlockY();
		z += getHostLocation().getBlockZ();

		this.hostLocation = getWorld().getBlockAt(x, y, z).getLocation();

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("Effect sign accepted.");
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