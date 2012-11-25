package me.heldplayer.HeldStone.event;

import me.heldplayer.HeldStone.Player;

public class PlayerEvent extends HeldStoneEvent {
	protected Player player;

	public PlayerEvent(Player player) {
		this.player = player;
	}

	public Player getSign() {
		return player;
	}
}
