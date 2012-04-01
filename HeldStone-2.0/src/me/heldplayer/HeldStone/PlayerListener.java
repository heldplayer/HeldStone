package me.heldplayer.HeldStone;

import me.heldplayer.HeldStone.sign.HeldSign;
import me.heldplayer.util.Constants;
import me.heldplayer.util.Direction;
import me.heldplayer.util.Functions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
	private final HeldStone main;

	public PlayerListener(HeldStone plugin) {
		main = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType() == Material.GLOWSTONE_DUST) {
			if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				Player player = main.pmng.safelyGet(event.getPlayer().getName(), main);

				player.setPrimaryLoc(event.getClickedBlock().getLocation());
				Functions.sendMessage(player.getPlayer(), "Set location 1", Constants.info);

				event.setCancelled(true);
			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player player = main.pmng.safelyGet(event.getPlayer().getName(), main);

				player.setSecondaryLoc(event.getClickedBlock().getLocation());
				Functions.sendMessage(player.getPlayer(), "Set location 2", Constants.info);

				event.setCancelled(true);
			}
		} else if (event.getPlayer().getItemInHand().getType() == Material.SULPHUR) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player player = main.pmng.safelyGet(event.getPlayer().getName(), main);

				player.setSingleLoc(event.getClickedBlock().getLocation());
				Functions.sendMessage(player.getPlayer(), "Set location 3", Constants.info);

				event.setCancelled(true);
			}
		} else if (event.getPlayer().getItemInHand().getType() == Material.SLIME_BALL) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				HeldSign sign = main.smng.signAtLocation(event.getClickedBlock().getLocation());

				if (sign == null) {
					Functions.sendMessage(event.getPlayer(), "No HeldStone sign found at this location.", Constants.error);
				} else {
					Location loc = sign.getLocation();
					Functions.sendMessage(event.getPlayer(), sign.getType().displayName + " (" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ")", Constants.debug);
					Functions.sendMessage(event.getPlayer(), "Owner: " + Constants.debugVal + sign.getOwner(), Constants.debug);
					Functions.sendMessage(event.getPlayer(), "Data: " + Constants.debugVal + sign.getData().toString(), Constants.debug);
					Functions.sendMessage(event.getPlayer(), "Front: " + sign.getInput(1).displayString, Constants.debug);
					Functions.sendMessage(event.getPlayer(), "Left: " + sign.getInput(2).displayString, Constants.debug);
					Functions.sendMessage(event.getPlayer(), "Right: " + sign.getInput(0).displayString, Constants.debug);
					Functions.sendMessage(event.getPlayer(), "Back: " + sign.getOutput().displayString, Constants.debug);

					event.setCancelled(true);
				}
			}
		} else if (event.getPlayer().getItemInHand().getType() == Material.CLAY_BALL) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getClickedBlock().getType() == Material.WALL_SIGN && !main.smng.isSignAtLocation(event.getClickedBlock().getLocation())) {
					Sign sign = (Sign) event.getClickedBlock().getState();
					Direction d = HeldSign.getDirection(sign);

					HeldSign.create(sign.getLines(), event.getPlayer().getName(), sign, event.getClickedBlock().getLocation(), d, null, main, true, null);

					event.setCancelled(true);
				}
			}
		}
	}
}
