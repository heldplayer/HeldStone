package me.heldplayer.HeldStone;

import java.util.ArrayList;

import me.heldplayer.HeldStone.event.PlayerTrackerCreatedEvent;

/**
 * Manages player info.
 * Used for locations, chat IC-messages and potential snow height.
 * 
 * @author heldplayer
 * 
 */
public class PlayerManager {
	private final ArrayList<Player> players;

	/**
	 * Creates a new instance of the PlayerManager.
	 */
	public PlayerManager() {
		players = new ArrayList<Player>();
	}

	public ArrayList<Player> getAll() {
		return new ArrayList<Player>(players);
	}

	/**
	 * Addes a new player to the PlayerManager.
	 * 
	 * @param player
	 *            The player to add
	 */
	public void add(Player player) {
		PlayerTrackerCreatedEvent event = new PlayerTrackerCreatedEvent(player);

		HeldStone.doEvent(event);

		if (!event.isCancelled()) {
			if (!exists(player.getName())) {
				players.add(player);
			}
		}
	}

	/**
	 * Checks to see if a player has information stored.
	 * 
	 * @param name
	 *            The name of the player being checked
	 * @return <code>true</code> if the player is stored, <code>false</code>
	 *         otherwise
	 */
	public boolean exists(String name) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns player info
	 * 
	 * @param name
	 *            The name of the required player
	 * @return The player info, or <code>null</code> if none is stored
	 */
	public Player get(String name) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getName().equalsIgnoreCase(name)) {
				Player player = players.get(i);

				player.lastAccess = System.currentTimeMillis();

				return player;
			}
		}
		return null;
	}

	/**
	 * Safely returns player info by creating it if it doesn't exist
	 * 
	 * @param name
	 *            The name of the required player
	 * @param main
	 *            Instance of HeldStone
	 * @return The player info
	 */
	public Player safelyGet(String name, HeldStone main) {
		if (exists(name)) {
			return get(name);
		} else {
			add(new Player(name, main));
			return get(name);
		}
	}

	public void remove(Player player) {
		if (players.contains(player)) {
			players.remove(player);
		}
		player.purge();
	}

	/**
	 * Destroys all references to player handles
	 */
	public void purge() {
		while (players.size() > 0) {
			players.remove(0).purge();
		}
	}
}
