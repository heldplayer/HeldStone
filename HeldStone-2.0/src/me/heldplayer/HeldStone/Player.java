package me.heldplayer.HeldStone;

import org.bukkit.Location;

public class Player {
	public final String name;
	public Location primaryLoc, secondaryLoc, singleLoc;
	public boolean locationsSet = false;
	public String message = null;
	private final HeldStone main;
	public int snowHeight = 0;
	protected long lastAccess = 0;

	public Player(String name, HeldStone main) {
		this.name = name;
		this.main = main;
		this.lastAccess = System.currentTimeMillis();
	}

	/**
	 * Gets the name of the player being managed
	 * 
	 * @return The name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the bukkit player
	 * 
	 * @return The Bukkit player entity
	 */
	public org.bukkit.entity.Player getPlayer() {
		return this.main.getServer().getPlayer(name);
	}

	/**
	 * Checks if the player is offline
	 * 
	 * @return <code>true</code> if the layer is online, <code>false</code>
	 *         otherwise.
	 */
	public boolean isOnline() {
		return getPlayer() != null;
	}

	/**
	 * Sets the primary location
	 * 
	 * @param location
	 *            The new location of the primary selection point
	 */
	public void setPrimaryLoc(Location location) {
		primaryLoc = location;
		locationsSet = (secondaryLoc != null);
	}

	/**
	 * Sets the primary location
	 * 
	 * @param location
	 *            The new location of the primary selection point
	 */
	public void setSecondaryLoc(Location location) {
		secondaryLoc = location;
		locationsSet = (primaryLoc != null);
	}

	/**
	 * Sets the single block location
	 * 
	 * @param location
	 *            The new location of the single block selection
	 */
	public void setSingleLoc(Location location) {
		singleLoc = location;
	}

	/**
	 * Prepares this tracker for deletion
	 */
	public void purge() {
		if (primaryLoc != null)
			primaryLoc.zero();
		if (secondaryLoc != null)
			secondaryLoc.zero();
		if (singleLoc != null)
			singleLoc.zero();
		locationsSet = false;
		message = null;
		snowHeight = 0;
	}
}
