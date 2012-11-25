package me.heldplayer.HeldStone.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class HeldStoneEvent extends Event {
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
