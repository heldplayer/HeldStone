package dsiwars.co.cc.HeldStone;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class ListenerCuboid implements Listener {

	private final HeldStone main;

	public ListenerCuboid(HeldStone main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
			main.alert(event.getPlayer().getName(), "This block is part of a cuboid!", ChatColor.RED);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBurn(BlockBurnEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockFade(BlockFadeEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockForm(BlockFormEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockFromTo(BlockFromToEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		} else if (main.bcr.contains(event.getToBlock().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		for (Block block : event.getBlocks()) {
			if (main.bcr.contains(block.getLocation())) {
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPistonRetract(BlockPistonRetractEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (!event.isSticky()) {
			return;
		}

		Location tempPos = event.getBlock().getLocation().clone();

		BlockFace face = event.getDirection();

		switch (face) {
		case NORTH:
			tempPos.add(-2, 0, 0);
		break;
		case EAST:
			tempPos.add(0, 0, -2);
		break;
		case SOUTH:
			tempPos.add(2, 0, 0);
		break;
		case WEST:
			tempPos.add(0, 0, 2);
		break;
		case UP:
			tempPos.add(0, 2, 0);
		break;
		case DOWN:
			tempPos.add(0, -2, 0);
		break;
		default:
			this.main.e("Unexpected direction from a sticky piston!");
		}

		if (main.bcr.contains(tempPos)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
			main.alert(event.getPlayer().getName(), "This block is part of a cuboid!", ChatColor.RED);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockSpread(BlockSpreadEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		} else if (main.bcr.contains(event.getSource().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}

		if (main.bcr.contains(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}
}