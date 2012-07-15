package dsiwars.co.cc.HeldStone;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTTagEnd;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.HeldSign.InputState;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class ListenerPlayer implements Listener {

	private final HeldStone main;
	private final HeldPlayers players;

	public ListenerPlayer(HeldStone main, HeldPlayers players) {
		this.main = main;
		this.players = players;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!this.main.enabled) {
			return;
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getPlayer().getItemInHand().getType() == Material.GLOWSTONE_DUST) {
				if (this.main.hasPermission(event.getPlayer(), "heldstone.select.cuboid")) {
					if (this.players.exists(event.getPlayer().getName())) {
						this.players.get(event.getPlayer().getName()).setLoc(event.getClickedBlock().getLocation());
					} else {
						this.players.safelyGet(event.getPlayer().getName(), this.main).setLoc(event.getClickedBlock().getLocation());
					}
				}
			} else if (event.getPlayer().getItemInHand().getType() == Material.SULPHUR) {
				if (this.main.hasPermission(event.getPlayer(), "heldstone.select.single")) {
					if (this.players.exists(event.getPlayer().getName())) {
						this.players.get(event.getPlayer().getName()).setLoc3(event.getClickedBlock().getLocation());
					} else {
						this.players.safelyGet(event.getPlayer().getName(), this.main).setLoc3(event.getClickedBlock().getLocation());
					}
				}
			} else if (event.getPlayer().getItemInHand().getType() == Material.SLIME_BALL) {
				if (this.main.hasPermission(event.getPlayer(), "heldstone.debug")) {
					ArrayList<HeldSign> signs = this.main.sgc.getAllSigns();

					Iterator<HeldSign> i = signs.iterator();

					HeldSign theSign = null;

					while (i.hasNext()) {
						HeldSign sign = i.next();

						if (sign.getLocation().equals(event.getClickedBlock().getLocation())) {
							theSign = sign;
							break;
						}
					}

					if (theSign == null) {
						this.main.alert(event.getPlayer().getName(), "No HeldStone sign found at this location.", ChatColor.RED);
						return;
					}

					this.main.alert(event.getPlayer().getName(), "Displaying HeldStone sign info at (" + ChatColor.BLUE + theSign.getLocation().getX() + ChatColor.YELLOW + "," + ChatColor.BLUE + theSign.getLocation().getY() + ChatColor.YELLOW + "," + ChatColor.BLUE + theSign.getLocation().getZ() + ChatColor.YELLOW + ")", ChatColor.YELLOW);
					this.main.alert(event.getPlayer().getName(), "-=GENERAL INFO=-", ChatColor.GOLD, "");
					this.main.alert(event.getPlayer().getName(), "Type: " + ChatColor.BLUE + theSign.getType().name(), ChatColor.YELLOW, "");
					this.main.alert(event.getPlayer().getName(), "Owner: " + ChatColor.BLUE + theSign.getOwnerName(), ChatColor.YELLOW, "");
					this.main.alert(event.getPlayer().getName(), "Data: " + ChatColor.BLUE + theSign.getNBTData().toString(), ChatColor.YELLOW, "");
					this.main.alert(event.getPlayer().getName(), "Trigger Type(s): " + ChatColor.BLUE + theSign.getTriggerTypesString(), ChatColor.YELLOW, "");
					this.main.alert(event.getPlayer().getName(), "-=INPUTS/OUTPUTS=-", ChatColor.GOLD, "");
					String powerMessage = theSign.getInput(1) == InputState.HIGH ? ChatColor.DARK_GREEN + "[X]" : (theSign.getInput(1) == InputState.LOW ? ChatColor.DARK_RED + "[O]" : ChatColor.DARK_GRAY + "[ ]");
					this.main.alert(event.getPlayer().getName(), "Front: " + powerMessage, ChatColor.YELLOW, "");
					powerMessage = theSign.getInput(2) == InputState.HIGH ? ChatColor.DARK_GREEN + "[X]" : (theSign.getInput(2) == InputState.LOW ? ChatColor.DARK_RED + "[O]" : ChatColor.DARK_GRAY + "[ ]");
					this.main.alert(event.getPlayer().getName(), "Left: " + powerMessage, ChatColor.YELLOW, "");
					powerMessage = theSign.getInput(0) == InputState.HIGH ? ChatColor.DARK_GREEN + "[X]" : (theSign.getInput(0) == InputState.LOW ? ChatColor.DARK_RED + "[O]" : ChatColor.DARK_GRAY + "[ ]");
					this.main.alert(event.getPlayer().getName(), "Right: " + powerMessage, ChatColor.YELLOW, "");
					powerMessage = theSign.getOutput() == InputState.HIGH ? ChatColor.DARK_GREEN + "[X]" : (theSign.getOutput() == InputState.LOW ? ChatColor.DARK_RED + "[O]" : ChatColor.DARK_GRAY + "[ ]");
					this.main.alert(event.getPlayer().getName(), "Back: " + powerMessage, ChatColor.YELLOW, "");
				}
			} else if (event.getPlayer().getItemInHand().getType() == Material.CLAY_BALL) {
				if (event.getClickedBlock().getState() instanceof Sign) {
					this.main.sgc.trigger(TriggerType.PING, null);

					Sign sign = (Sign) event.getClickedBlock().getState();

					Direction d = HeldSign.getDirection(sign);

					boolean result = HeldSign.signFactory(sign.getLines(), event.getPlayer().getName(), new NBTTagEnd(), sign.getWorld().getName(), sign.getLocation(), d, false, null, this.main);
					if (result) {
						this.main.alert(event.getPlayer().getName(), "Initialized a new HeldStone sign instance!", ChatColor.GREEN);
					} else {
						this.main.alert(event.getPlayer().getName(), "No HeldStone sign found at this location.", ChatColor.RED);
					}
				}
			}
		}
	}
}