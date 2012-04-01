package me.heldplayer.HeldStone.sign;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import me.heldplayer.HeldStone.HeldStone;
import me.heldplayer.HeldStone.event.SignCreatedEvent;
import me.heldplayer.util.Constants;
import me.heldplayer.util.Direction;
import me.heldplayer.util.Functions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.mojang.NBT.NBTBase;

public abstract class HeldSign {
	private HeldStone main;
	private Location loc;
	private SignType type;
	private String[] lines;
	private String owner;
	private Direction dir;
	private Location[] inputLocation;
	@SuppressWarnings("unused")
	private Location hostblockLocation, leverLocation;
	private List<TriggerType> registeredTypes;

	public abstract void triggerSign(TriggerType type, Object data);

	public abstract boolean initialize(boolean reload);

	public abstract void invalidate();

	public abstract void formatLines();

	public abstract NBTBase getData();

	public abstract void setData(NBTBase value);

	private boolean init(SignType type, String[] lines, String owner, Location loc, Direction dir, NBTBase data, HeldStone main, boolean reload, SignChangeEvent signEvent) {
		this.main = main;
		this.loc = loc;
		this.type = type;
		this.owner = owner;
		this.dir = dir;
		this.lines = new String[4];
		this.registeredTypes = new ArrayList<TriggerType>();

		this.inputLocation = new Location[3];
		this.inputLocation[0] = Direction.shift(loc, Direction.right(dir), 1);
		this.inputLocation[1] = Direction.shift(loc, dir, 1);
		this.inputLocation[2] = Direction.shift(loc, Direction.left(dir), 1);

		this.hostblockLocation = Direction.shift(loc, Direction.opposite(dir), 1);
		this.leverLocation = Direction.shift(loc, Direction.opposite(dir), 2);

		setLine(0, lines[0]);
		setLine(1, lines[1]);
		setLine(2, lines[2]);
		setLine(3, lines[3]);

		if (data != null)
			this.setData(data);

		if (initialize(reload)) {
			main.smng.register(this, TriggerType.PING);
			main.smng.register(this, TriggerType.VALID_CHECK);

			if (!reload) {
				formatLines();

				signEvent.setLine(0, this.lines[0]);
				signEvent.setLine(1, this.lines[1]);
				signEvent.setLine(2, this.lines[2]);
				signEvent.setLine(3, this.lines[3]);
			}

			return true;
		} else {
			return false;
		}
	}

	public static synchronized void trigger(HeldSign sign, TriggerType type, Object data) {
		if (type == TriggerType.PING || type == TriggerType.VALID_CHECK) {
			return;
		}

		if (type == TriggerType.REDSTONE_CHANGE) {
			BlockRedstoneEvent event = (BlockRedstoneEvent) data;

			boolean ismine = false;
			int pos = sign.getInputId(event);

			if (pos != -1) {
				ismine = true;
			}

			if (!ismine) {
				return;
			}

			if ((event.getNewCurrent() > 0 && event.getOldCurrent() > 0) || (event.getNewCurrent() <= 0 && event.getOldCurrent() <= 0)) {
				return;
			}
		}

		sign.triggerSign(type, data);
	}

	public InputState getOutput() {
		if (leverLocation.getBlock().getTypeId() != 69) {
			return InputState.DISCONNECTED;
		}

		byte s = leverLocation.getBlock().getState().getRawData();

		if (s >= 8) {
			return InputState.HIGH;
		} else {
			return InputState.LOW;
		}
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

	public int getInputId(BlockRedstoneEvent e) {
		int id = -1;
		for (int i = 0; i < this.inputLocation.length; i++) {
			if (this.inputLocation[i].equals(e.getBlock().getLocation())) {
				id = i;
				break;
			}
		}
		return id;
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

		HIGH(ChatColor.DARK_GREEN + "[+]" + ChatColor.RESET), LOW(ChatColor.DARK_RED + "[-]" + ChatColor.RESET), DISCONNECTED(ChatColor.DARK_GRAY + "[_]" + ChatColor.RESET);

		public final String displayString;

		private InputState(String displayed) {
			displayString = displayed;
		}
	}

	public Block getInputBlock(int direction) {
		return this.getLocation().getWorld().getBlockAt(getInputLocation(direction));
	}

	public Location getInputLocation(int direction) {
		return inputLocation[direction];
	}

	public Location getLocation() {
		return loc;
	}

	public SignType getType() {
		return type;
	}

	public String getLine(int index) {
		if (index < 0 || index > 3) {
			throw new IllegalArgumentException("Index must be between 0 and 3!");
		}

		return lines[index];
	}

	public String getOwner() {
		return owner;
	}

	public Direction getDirection() {
		return dir;
	}

	public HeldStone getPlugin() {
		return main;
	}

	public void clearLines() {
		setLine(1, "");
		setLine(2, "");
		setLine(3, "");
	}

	public void setLine(int id, String value) {
		this.lines[id] = value;
		Sign sign = getSign();
		sign.setLine(id, value);
		sign.update(true);
	}

	public Sign getSign() {
		Block block = this.getLocation().getBlock();

		if (block.getState() instanceof Sign) {
			return (Sign) block.getState();
		}

		return null;
	}

	public void register(TriggerType type) {
		if (!registeredTypes.contains(type)) {
			registeredTypes.add(type);
		}
		main.smng.register(this, type);
	}

	public List<TriggerType> getRegistered() {
		return registeredTypes;
	}

	public ValidationState isValid() {
		if (this.getLocation().getChunk().isLoaded()) {
			Block myBlock = this.getLocation().getBlock();

			if (myBlock.getTypeId() != 68) {
				return ValidationState.REMOVED;
			} else {
				Sign mySign = (Sign) myBlock.getState();
				String[] plines = Functions.formatLines(mySign.getLines());
				boolean matches = true;
				boolean hasText = false;
				for (int i = 0; i < 4; i++) {
					if (!lines[i].trim().equalsIgnoreCase(plines[i].trim())) {
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
		VALID, BLANK, INVALID, UNLOADED, REMOVED;
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

	public static boolean create(String[] lines, String owner, Sign sign, Location loc, Direction dir, NBTBase data, HeldStone main, boolean reload, SignChangeEvent signEvent) {
		String[] rlines = Functions.formatLines(lines);

		if (rlines[0].length() <= 2 || !(rlines[0].charAt(0) == '[' && rlines[0].charAt(rlines[0].length() - 1) == ']')) {
			return false;
		}

		String signName = rlines[0].substring(1, rlines[0].length() - 1);
		SignType type = SignType.UNKNOWN;

		for (SignType value : SignType.values()) {
			if (value.displayName.equalsIgnoreCase(signName)) {
				type = value;
				break;
			}
		}

		if (type == SignType.UNKNOWN && reload) {
			HeldStone.warning("An unknown type of sign has been loaded, the sign type may have been changed or removed.");
			HeldStone.warning("Please check the sign in " + loc.getWorld() + " at " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
			return false;
		} else if (type == SignType.UNKNOWN) {
			Functions.sendMessage(main.getServer().getPlayer(owner), "This is an unknown HeldStone sign!", Constants.error);
			return false;
		}

		if (!reload) {
			SignCreatedEvent event = new SignCreatedEvent(sign);

			if (event.isCancelled()) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Creation cancelled by a plugin!", Constants.error);
				return false;
			}
		}

		HeldSign result = null;

		try {
			Constructor<?> constructor = type.signClass.getConstructor(new Class[0]);
			result = (HeldSign) constructor.newInstance(new Object[0]);
		} catch (SecurityException e) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Failed to instantiate a HeldStone sign class: " + e.getLocalizedMessage(), Constants.fatal);
			}
			HeldStone.warning("Failed to instantiate a HeldStone sign class!");
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Failed to instantiate a HeldStone sign class: " + e.getLocalizedMessage(), Constants.fatal);
			}
			HeldStone.warning("Failed to instantiate a HeldStone sign class!");
			e.printStackTrace();
			return false;
		} catch (IllegalArgumentException e) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Failed to instantiate a HeldStone sign class: " + e.getLocalizedMessage(), Constants.fatal);
			}
			HeldStone.warning("Failed to instantiate a HeldStone sign class!");
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Failed to instantiate a HeldStone sign class: " + e.getLocalizedMessage(), Constants.fatal);
			}
			HeldStone.warning("Failed to instantiate a HeldStone sign class!");
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Failed to instantiate a HeldStone sign class: " + e.getLocalizedMessage(), Constants.fatal);
			}
			HeldStone.warning("Failed to instantiate a HeldStone sign class!");
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Failed to instantiate a HeldStone sign class: " + e.getLocalizedMessage(), Constants.fatal);
			}
			HeldStone.warning("Failed to instantiate a HeldStone sign class!");
			e.printStackTrace();
			return false;
		}

		if (result == null) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), "Failed to instantiate a HeldStone sign class!", Constants.fatal);
			}
			HeldStone.warning("Failed to instantiate a HeldStone sign class!");
			return false;
		}

		rlines[0] = "[" + type.displayName + "]";

		if (result.init(type, rlines, owner, loc, dir, data, main, reload, signEvent)) {
			if (!reload) {
				Functions.sendMessage(main.getServer().getPlayer(owner), type.displayName + " sign accepted!", Constants.success);
				HeldStone.info(owner + " created a new HeldStone sign of type " + type.displayName);
			}
		} else {
			if (!reload) {
				HeldStone.info(owner + " created a new HeldStone sign of type " + type.displayName + " but it failed to load");
			} else {
				HeldStone.info("Sign of type " + type.displayName + " did not load after a reload");
			}
			return false;
		}

		return true;
	}
}
