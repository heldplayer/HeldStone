package me.heldplayer.HeldStone.event;

import org.bukkit.block.Sign;
import org.bukkit.event.Cancellable;

public class SignCreatedEvent extends SignEvent implements Cancellable {
	private boolean cancelled = false;

	public SignCreatedEvent(Sign sign) {
		super(sign);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
