package me.heldplayer.HeldStone.sign.chat;

import me.heldplayer.HeldStone.Player;
import me.heldplayer.HeldStone.sign.HeldSign;
import me.heldplayer.HeldStone.sign.TriggerType;
import me.heldplayer.util.Constants;
import me.heldplayer.util.Functions;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.mojang.NBT.NBTBase;
import com.mojang.NBT.NBTTagString;

public class DispSign extends HeldSign {
	private String message;
	private String prefix;
	private String target;
	private InputState lastState;

	public void triggerSign(TriggerType type, Object data) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) data);
		
		org.bukkit.entity.Player player = getPlugin().getServer().getPlayerExact(target);

		if(player == null){
			return;
		}
		
		if (lastState != InputState.HIGH && is == InputState.HIGH) {
			Functions.sendMessage(player, ChatColor.translateAlternateColorCodes('&', message), ChatColor.translateAlternateColorCodes('&', prefix));
		}

		lastState = is;
	}

	public boolean initialize(boolean reload) {
		if (!reload) {
			Player player = getPlugin().pmng.safelyGet(getOwner(), getPlugin());

			if (player.message == null || player.message == "") {
				Functions.sendMessage(player.getPlayer(), "You must specify a message using /heldstone msg first!", Constants.error);

				return false;
			} else {
				message = player.message;
			}
		}
		
		target = getLine(1);

		prefix = getLine(2);

		register(TriggerType.REDSTONE_CHANGE);

		return true;
	}

	public void formatLines() {
		setLine(1, target);
		setLine(2, prefix);
	}

	public void invalidate() {
	}

	public NBTBase getData() {
		return new NBTTagString(message);
	}

	public void setData(NBTBase value) {
		message = value.toString();
	}
}
