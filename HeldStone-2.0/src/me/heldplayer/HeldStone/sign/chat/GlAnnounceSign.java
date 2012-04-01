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

public class GlAnnounceSign extends HeldSign {
	private String message;
	private String prefix;
	private InputState lastState;

	public void triggerSign(TriggerType type, Object data) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) data);

		if (lastState != InputState.HIGH && is == InputState.HIGH) {
			Functions.sendMessage(null, ChatColor.translateAlternateColorCodes('&', message), ChatColor.translateAlternateColorCodes('&', prefix));
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

		prefix = getLine(1);

		register(TriggerType.REDSTONE_CHANGE);

		return true;
	}

	public void formatLines() {
		setLine(1, prefix);
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
