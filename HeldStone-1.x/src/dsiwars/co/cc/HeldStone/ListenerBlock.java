package dsiwars.co.cc.HeldStone;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import dsiwars.co.cc.HeldStone.NBT.NBTTagEnd;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class ListenerBlock implements Listener {

	private final HeldStone main;

	public ListenerBlock(HeldStone main) {
		this.main = main;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if (event.getBlock().getState() instanceof Sign) {
			int i = 0;
			for (String line : event.getLines()) {
				char[] b = line.toCharArray();
				for (int i2 = 0; i2 < b.length - 1; i2++) {
					if (b[i2] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i2 + 1]) > -1) {
						b[i2] = ChatColor.COLOR_CHAR;
						b[i2 + 1] = Character.toLowerCase(b[i2 + 1]);
					}
				}
				event.setLine(i, new String(b));
				i++;
			}
			if (event.getLines().length > 0) {
				this.main.sgc.trigger(TriggerType.PING, null);
				Direction d = HeldSign.getDirection((Sign) (event.getBlock().getState()));
				HeldSign.signFactory(event.getLines(), event.getPlayer().getName(), new NBTTagEnd(), event.getBlock().getWorld().getName(), event.getBlock().getLocation(), d, false, event, main);
			}
		} else {
			this.main.e("SignChangeEvent triggered on a non-sign block.");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		Location loc = event.getBlock().getLocation();

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					Location currentLoc = loc.clone();
					currentLoc.add(x, y, z);

					World world = event.getBlock().getWorld();

					if (world.getBlockAt(currentLoc).getTypeId() == 87) {
						Location fireLoc = currentLoc.clone();
						fireLoc.add(0, 1, 0);

						if (world.getBlockAt(fireLoc).getTypeId() == 0 || world.getBlockAt(fireLoc).getTypeId() == 51) {

							if (event.getNewCurrent() >= 1) {
								//world.getBlockAt(fireLoc).setTypeIdAndData(51, (byte) 15, false);
							} else {
								//world.getBlockAt(fireLoc).setTypeIdAndData(0, (byte) 0, false);
							}
						}
					}

					if (world.getBlockAt(currentLoc).getTypeId() == 88 && event.getNewCurrent() >= 1) {
						//world.getBlockAt(currentLoc).setTypeIdAndData(89, (byte) 0, false);
					} else if (world.getBlockAt(currentLoc).getTypeId() == 89 && event.getNewCurrent() == 0) {
						//world.getBlockAt(currentLoc).setTypeIdAndData(88, (byte) 0, false);
					} else if (world.getBlockAt(currentLoc).getTypeId() == 86 && event.getNewCurrent() >= 1) {
						//world.getBlockAt(currentLoc).setTypeIdAndData(91, world.getBlockAt(currentLoc).getData(), false);
					} else if (world.getBlockAt(currentLoc).getTypeId() == 91 && event.getNewCurrent() == 0) {
						//world.getBlockAt(currentLoc).setTypeIdAndData(86, world.getBlockAt(currentLoc).getData(), false);
					} else if (world.getBlockAt(currentLoc).getTypeId() == 84 && event.getNewCurrent() >= 1) {
						Jukebox jb = (Jukebox) world.getBlockAt(currentLoc).getState();
						if (jb.getPlaying() != null) {
							world.playEffect(currentLoc, Effect.RECORD_PLAY, jb.getPlaying().getId());
						} else {
							world.playEffect(currentLoc, Effect.RECORD_PLAY, 0);
						}
					} else if (world.getBlockAt(currentLoc).getTypeId() == 84 && event.getNewCurrent() == 0) {
						world.playEffect(currentLoc, Effect.RECORD_PLAY, 0);
					}
				}
			}
		}

		this.main.sgc.trigger(TriggerType.REDSTONE_CHANGE, event);
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlockPlaced();
		
		if ((block.getTypeId() == 78 || block.getTypeId() == 80)) {
			HeldPlayer psp = this.main.players.safelyGet(event.getPlayer().getName(), this.main);
			
			if(psp.snow != 0){
				block.setTypeIdAndData(78, (byte) psp.snow, false);
			}
		}
	}

	//@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getTypeId() == 18) {
			if (main.rand.nextInt(100) <= main.cfgAppleDropChance) {
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
			}
		}
		if (event.getBlock().getTypeId() == 47) {
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.BOOK, 1));
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.BOOK, 1));
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.BOOK, 1));
		}
	}

	//@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (event.getBlock().getTypeId() == 18) {
			if (main.rand.nextInt(100) <= main.cfgAppleDropChance) {
				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
			}
		}
	}
}