package dsiwars.co.cc.HeldStone.sign;

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
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.MaterialData;

import dsiwars.co.cc.HeldStone.Direction;
import dsiwars.co.cc.HeldStone.Functions;
import dsiwars.co.cc.HeldStone.HeldStone;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagEnd;
import dsiwars.co.cc.HeldStone.sign.chat.AnnounceSign;
import dsiwars.co.cc.HeldStone.sign.chat.DispSign;
import dsiwars.co.cc.HeldStone.sign.chat.GlAnnounceSign;
import dsiwars.co.cc.HeldStone.sign.check.CtimeSign;
import dsiwars.co.cc.HeldStone.sign.check.CweatherSign;
import dsiwars.co.cc.HeldStone.sign.functional.AreaToggleSign;
import dsiwars.co.cc.HeldStone.sign.functional.BlackHoleSign;
import dsiwars.co.cc.HeldStone.sign.functional.BoltSign;
import dsiwars.co.cc.HeldStone.sign.functional.CauldronSign;
import dsiwars.co.cc.HeldStone.sign.functional.EffectSign;
import dsiwars.co.cc.HeldStone.sign.logic.ClockSign;
import dsiwars.co.cc.HeldStone.sign.logic.DelaySign;
import dsiwars.co.cc.HeldStone.sign.logic.LogicSign;
import dsiwars.co.cc.HeldStone.sign.logic.RandSign;
import dsiwars.co.cc.HeldStone.sign.logic.ToggleSign;
import dsiwars.co.cc.HeldStone.sign.logic.TriggerSign;
import dsiwars.co.cc.HeldStone.sign.sensor.ISensorSign;
import dsiwars.co.cc.HeldStone.sign.sensor.LaSensorSign;
import dsiwars.co.cc.HeldStone.sign.sensor.LiSensorSign;
import dsiwars.co.cc.HeldStone.sign.sensor.LoggedSign;
import dsiwars.co.cc.HeldStone.sign.sensor.MSensorSign;
import dsiwars.co.cc.HeldStone.sign.sensor.PSensorSign;
import dsiwars.co.cc.HeldStone.sign.sensor.PlayersSign;
import dsiwars.co.cc.HeldStone.sign.sensor.SensorSign;
import dsiwars.co.cc.HeldStone.sign.sensor.WaSensorSign;
import dsiwars.co.cc.HeldStone.sign.set.StimeSign;
import dsiwars.co.cc.HeldStone.sign.set.SweatherSign;
import dsiwars.co.cc.HeldStone.sign.spawn.CuboidSign;
import dsiwars.co.cc.HeldStone.sign.spawn.ItemSign;
import dsiwars.co.cc.HeldStone.sign.spawn.SpawnSign;
import dsiwars.co.cc.HeldStone.sign.wireless.RecvSign;
import dsiwars.co.cc.HeldStone.sign.wireless.SendSign;

public abstract class HeldSign {

	public boolean initialize(SignType type, String[] lines, String owner, NBTBase data, String world, Location l, Direction facing, boolean reload, SignChangeEvent event, HeldStone main) {
		this.safeLines = lines;
		this.location = l;
		this.world = world;
		this.owner = owner;
		this.main = main;
		this.myType = type;

		this.facing = facing;
		this.inputLocation = new Location[3];
		this.inputLocation[0] = Direction.shift(l, Direction.right(facing), 1);
		this.inputLocation[1] = Direction.shift(l, facing, 1);
		this.inputLocation[2] = Direction.shift(l, Direction.left(facing), 1);

		this.hostblockLocation = Direction.shift(l, Direction.opposite(facing), 1);
		this.leverLocation = Direction.shift(l, Direction.opposite(facing), 2);

		if (!(data instanceof NBTTagEnd)) {
			this.setNBTData(data);
		}
		if (!this.declare(reload, event)) {
			return false;
		}
		this.main.sgc.register(this, TriggerType.PING);
		this.main.sgc.register(this, TriggerType.VALID_CHECK);
		return true;
	}

	private String[] safeLines;
	private Location location;
	private String world;
	private String owner;
	protected HeldStone main;
	private Direction facing;
	private Location[] inputLocation;
	private Location hostblockLocation, leverLocation;
	private SignType myType;

	public SignType getType() {
		return myType;
	}

	public Direction getFacing() {
		return facing;
	}

	public InputState getInput(int direction, BlockRedstoneEvent event) {
		if (event != null) {
			Block iblock = this.getInputBlock(direction);
			if (!iblock.getLocation().equals(event.getBlock().getLocation())) {
				return this.getInput(direction);
			} else {
				if (event.getBlock().getType() == Material.REDSTONE_WIRE) {
					if (event.getNewCurrent() > 0) {
						return InputState.HIGH;
					} else {
						return InputState.LOW;
					}
				} else {
					return InputState.DISCONNECTED;
				}
			}
		} else {
			return this.getInput(direction);
		}
	}

	public InputState getInput(int direction) {
		Block iblock = this.getInputBlock(direction);
		if (iblock.getType() == Material.REDSTONE_WIRE) {
			if (iblock.getState().getRawData() > 0) {
				return InputState.HIGH;
			} else {
				return InputState.LOW;
			}
		} else {
			return InputState.DISCONNECTED;
		}
	}

	public static enum InputState {

		HIGH, LOW, DISCONNECTED;
	}

	public Block getLeverBlock() {
		return this.getWorld().getBlockAt(this.getLeverLocation());
	}

	public Location getLeverLocation() {
		return leverLocation.clone();
	}

	public Block getHostBlock() {
		return this.getWorld().getBlockAt(this.getHostLocation());
	}

	public Location getHostLocation() {
		return hostblockLocation.clone();
	}

	public Block getInputBlock(int direction) {
		return this.getWorld().getBlockAt(getInputLocation(direction));
	}

	public Location getInputLocation(int direction) {
		return inputLocation[direction];
	}

	public Block getBlock() {
		return main.getServer().getWorld(world).getBlockAt(location);
	}

	public Location getLocation() {
		return location.clone();
	}

	public World getWorld() {
		return this.main.getServer().getWorld(this.world);
	}

	public Player getOwner() {
		return this.main.getServer().getPlayer(this.getOwnerName());
	}

	public String getOwnerName() {
		return this.owner;
	}

	public boolean isLoaded() {
		return this.getWorld().isChunkLoaded(this.getWorld().getChunkAt(this.getLocation()));
	}

	public boolean isOutputLever() {
		return this.getLeverBlock().getType() == Material.LEVER;
	}

	public void clearArgLines() {
		setLine(1, " ");
		setLine(2, " ");
		setLine(3, " ");
	}

	public void setLine(int line, String newLine) {
		Sign mySign = (Sign) this.getBlock().getState();
		mySign.setLine(line, newLine);
		mySign.update(true);
		this.safeLines[line] = newLine;
		this.safeLines = Functions.formatLines(this.safeLines);
	}

	public void clearArgLines(SignChangeEvent event) {
		setLine(1, " ", event);
		setLine(2, " ", event);
		setLine(3, " ", event);
	}

	public void setLine(int line, String newLine, SignChangeEvent event) {
		if (event == null) {
			this.setLine(line, newLine);
		} else {
			event.setLine(line, newLine);
			this.safeLines[line] = newLine;
			this.safeLines = Functions.formatLines(this.safeLines);
		}
	}

	public void init(String message) {
		this.main.alert(this.owner, message, ChatColor.AQUA);
	}

	public void setOutput(boolean out) {
		if (isLoaded() && isOutputLever()) {
			byte s = this.getLeverBlock().getState().getRawData();

			if (out) {
				s = (byte) (s | 8);
			} else {
				s = (byte) (s & 7);
			}

			BlockState state = this.getLeverBlock().getState();
			MaterialData stateData = state.getData();
			stateData.setData(s);
			state.update();
			this.getLeverBlock().getState().update();
			this.getHostBlock().getState().update();
			this.getLeverBlock().getState().getData().setData(s);
		}
	}

	public InputState getOutput() {
		if (this.getLeverBlock().getTypeId() != 69) {
			return InputState.DISCONNECTED;
		}

		byte s = this.getLeverBlock().getState().getRawData();

		if (s >= 8) {
			return InputState.HIGH;
		} else {
			return InputState.LOW;
		}
	}

	public ValidationState isValid() {
		if (this.isLoaded()) {
			Block myBlock = this.getBlock();

			if (myBlock.getTypeId() != 68) {
				return ValidationState.INVALID;
			} else {
				Sign mySign = (Sign) myBlock.getState();
				String[] plines = Functions.formatLines(mySign.getLines());
				boolean matches = true;
				boolean hasText = false;
				for (int i = 0; i < 4; i++) {
					if (!safeLines[i].trim().equalsIgnoreCase(plines[i].trim())) {
						matches = false;
						if ((!plines[i].trim().equals("")) && plines[i] != null) {
							hasText = true;
						}
						break;
					}
				}
				if (matches) {
					return ValidationState.VALID;
				} else {
					if (hasText) {
						return ValidationState.INVALID;
					}
					return ValidationState.BLANK;
				}
			}

		} else {
			return ValidationState.UNLOADED;
		}
	}

	public static enum ValidationState {

		VALID, BLANK, INVALID, UNLOADED;
	}

	protected int getInputId(BlockRedstoneEvent e) {
		int id = -1;
		for (int i = 0; i < this.inputLocation.length; i++) {
			if (this.inputLocation[i].equals(e.getBlock().getLocation())) {
				id = i;
				break;
			}
		}
		return id;
	}

	private long lastTriggered[] = { 0, 0, 0 };
	private static final long delay = 1;

	public synchronized void trigger(TriggerType type, Object args) {
		if (type == TriggerType.PING) {
			return;
		}
		if (type == TriggerType.VALID_CHECK) {
			return;
		}

		if (type == TriggerType.REDSTONE_CHANGE) {

			BlockRedstoneEvent event = (BlockRedstoneEvent) args;
			boolean ismine = false;
			int pos = getInputId(event);

			if (pos != -1) {
				ismine = true;
			}

			if (!ismine) {
				return;
			}

			if ((event.getNewCurrent() > 0 && event.getOldCurrent() > 0) || (event.getNewCurrent() <= 0 && event.getOldCurrent() <= 0)) {
				return;
			}

			long ctime = this.getWorld().getFullTime();

			if ((ctime - lastTriggered[pos]) > delay || (lastTriggered[pos] > ctime)) {
				lastTriggered[pos] = ctime;
				triggersign(type, args);
				return;
			} else {
				return;
			}

		}

		triggersign(type, args);
	}

	protected abstract void triggersign(TriggerType type, Object args);

	protected void startTicking() {
		this.main.tickctrl.register(this);
	}

	public boolean tick() {
		return false;
	}

	protected abstract boolean declare(boolean reload, SignChangeEvent event);

	protected abstract void setNBTData(NBTBase tag);

	public abstract NBTBase getNBTData();

	public abstract String getTriggerTypesString();

	public abstract void invalidate();

	public String[] getLines() {
		return safeLines;
	}

	public String[] getLines(SignChangeEvent event) {
		if (event != null) {
			return Functions.formatLines(event.getLines());
		} else {
			return getLines();
		}
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof HeldSign && o == this);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + Arrays.deepHashCode(this.safeLines);
		hash = 53 * hash + (this.location != null ? this.location.hashCode() : 0);
		hash = 53 * hash + (this.owner != null ? this.owner.hashCode() : 0);
		hash = 53 * hash + (this.myType != null ? this.myType.hashCode() : 0);
		return hash;
	}

	public static boolean signFactory(String[] lines, String owner, NBTBase data, String world, Location l, Direction facing, boolean reload, SignChangeEvent event, HeldStone main) {
		String[] rlines = null;
		if (reload) {
			rlines = Functions.formatLines(lines);
		} else {
			try {
				rlines = Functions.formatLines(event.getLines());
			} catch (Exception ex) {
				rlines = Functions.formatLines(lines);
			}
		}

		if (rlines[0].length() <= 2 || !(rlines[0].charAt(0) == '[') && !(rlines[0].charAt(rlines[0].length() - 1) == ']')) {
			return false;
		}

		String signName = rlines[0].substring(1, rlines[0].length() - 1);
		SignType type = null;

		for (int i = 0; i < SignType.values().length; i++) {
			if (SignType.values()[i].name().equalsIgnoreCase(signName)) {
				type = SignType.values()[i];
				break;
			}
		}

		if (type == null) {
			return false;
		}

		HeldSign newSign = null;

		String permission = "heldstone.ic." + type.groupName.toLowerCase() + "." + type.name().toLowerCase();

		if ((!reload) && (!main.hasPermission(main.getServer().getPlayer(owner), permission))) {
			main.i(owner + " tried to make a " + type + " sign, but lacks the proper permissions.");
			main.alert(owner, "You do not have permission to create this sign.", ChatColor.RED);
			return false;
		}

		switch (type) {
		case RAND:
			newSign = new RandSign();
		break;
		case LOGIC:
			newSign = new LogicSign();
		break;
		case DELAY:
			newSign = new DelaySign();
		break;
		case TOGGLE:
			newSign = new ToggleSign();
		break;
		case SEND:
			newSign = new SendSign();
		break;
		case RECV:
			newSign = new RecvSign();
		break;
		case TRIGGER:
			newSign = new TriggerSign();
		break;
		case CLOCK:
			newSign = new ClockSign();
		break;
		case CTIME:
			newSign = new CtimeSign();
		break;
		case CWEATHER:
			newSign = new CweatherSign();
		break;
		case LOGGED:
			newSign = new LoggedSign();
		break;
		case SENSOR:
			newSign = new SensorSign();
		break;
		case DISP:
			newSign = new DispSign();
		break;
		case ANNOUNCE:
			newSign = new AnnounceSign();
		break;
		case CUBOID:
			newSign = new CuboidSign();
		break;
		case SPAWN:
			newSign = new SpawnSign();
		break;
		case ITEM:
			newSign = new ItemSign();
		break;
		case BOLT:
			newSign = new BoltSign();
		break;
		case PLAYERS:
			newSign = new PlayersSign();
		break;
		case SWEATHER:
			newSign = new SweatherSign();
		break;
		case STIME:
			newSign = new StimeSign();
		break;
		case CAULDRON:
			newSign = new CauldronSign();
		break;
		case EFFECT:
			newSign = new EffectSign();
		break;
		case PSENSOR:
			newSign = new PSensorSign();
		break;
		case ISENSOR:
			newSign = new ISensorSign();
		break;
		case MSENSOR:
			newSign = new MSensorSign();
		break;
		case WaSENSOR:
			newSign = new WaSensorSign();
		break;
		case LaSENSOR:
			newSign = new LaSensorSign();
		break;
		case LiSENSOR:
			newSign = new LiSensorSign();
		break;
		case GlANNOUNCE:
			newSign = new GlAnnounceSign();
		break;
		case BLACKHOLE:
			newSign = new BlackHoleSign();
		break;
		//case SNOWBALL: //XXX: removed
		//	newSign = new SnowballSign();
		//	break;
		//case ExplBall: //XXX: removed
		//	newSign = new ExplBallSign();
		//	break;
		case AreaToggle:
			newSign = new AreaToggleSign();
		break;
		}

		if (newSign != null) {
			if (!newSign.initialize(type, lines, owner, data, world, l, facing, reload, event, main)) {
				if (!reload) {
					main.i(owner + " tried to make a " + type + " sign, but the sign refused to initialize.");
				}

				return false;
			}
		} else if (!reload) {
			main.alert(owner, "Error while creating sign.", ChatColor.RED);
			try {
				event.setCancelled(true);
			} catch (Exception ex) {
			}
		}

		if (!reload) {
			main.i(owner + " created a " + type + " sign.");
		}

		return true;
	}

	public static int getNumericDirection(Sign sign) {
		return sign.getRawData();
	}

	public static Direction getDirection(Sign sign) {

		switch (getNumericDirection(sign)) {

		case 2:
			return Direction.EAST;
		case 3:
			return Direction.WEST;
		case 4:
			return Direction.NORTH;
		case 5:
			return Direction.SOUTH;

		}

		return Direction.NORTH;
	}
}