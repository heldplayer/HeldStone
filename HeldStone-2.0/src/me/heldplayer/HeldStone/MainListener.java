package me.heldplayer.HeldStone;

import me.heldplayer.HeldStone.sign.HeldSign;
import me.heldplayer.HeldStone.sign.TriggerType;
import me.heldplayer.util.Direction;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

public class MainListener implements Listener {
	private final HeldStone main;

	public MainListener(HeldStone plugin) {
		main = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if (event.getBlock().getState() instanceof Sign) {
			if (event.getLines().length > 0) {
				Direction d = HeldSign.getDirection((Sign) (event.getBlock().getState()));
				HeldSign.create(event.getLines(), event.getPlayer().getName(), (Sign) event.getBlock().getState(), event.getBlock().getLocation(), d, null, main, false, event);
			}
		} else {
			HeldStone.warning("Tried to trigger a non-sign!");
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockRedstone(BlockRedstoneEvent event) {
		this.main.smng.trigger(TriggerType.REDSTONE_CHANGE, event);
	}
}
