
package me.heldplayer.HeldStone.event;

import me.heldplayer.HeldStone.Player;

import org.bukkit.event.Cancellable;

public class PlayerTrackerCreatedEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled = false;

    public PlayerTrackerCreatedEvent(Player player) {
        super(player);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
