package me.heldplayer.HeldStone.event;

import org.bukkit.block.Sign;

public abstract class SignEvent extends HeldStoneEvent {
	protected Sign sign;

	public SignEvent(Sign sign) {
		this.sign = sign;
	}

	public Sign getSign() {
		return sign;
	}
}
