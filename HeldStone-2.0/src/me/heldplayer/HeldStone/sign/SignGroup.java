package me.heldplayer.HeldStone.sign;

import java.util.ArrayList;

import me.heldplayer.HeldStone.HeldStone;
import me.heldplayer.HeldStone.sign.HeldSign.ValidationState;

public class SignGroup {
	private final TriggerType type;
	private final ArrayList<HeldSign> signs;
	private final HeldStone main;

	public SignGroup(TriggerType type, HeldStone main) {
		this.type = type;
		this.signs = new ArrayList<HeldSign>();
		this.main = main;
	}

	public TriggerType getType() {
		return type;
	}

	public boolean isType(TriggerType ctype) {
		return (getType() == ctype);
	}

	public void add(HeldSign sign) {
		signs.add(sign);
	}

	public void invalidate(HeldSign sign) {
		signs.remove(sign);
	}

	public ArrayList<HeldSign> getSigns() {
		return signs;
	}

	public void trigger(Object args) {
		for (int i = 0; i < signs.size(); i++) {
			HeldSign toTrigger = signs.get(i);
			ValidationState valid = toTrigger.isValid();
			if (valid == ValidationState.INVALID) {
				main.smng.invalidate(toTrigger, "Sign text was changed");
			} else if (valid == ValidationState.REMOVED) {
				main.smng.invalidate(toTrigger, "Sign was removed");
			} else if (valid == ValidationState.BLANK) {
				main.smng.invalidate(toTrigger, "Sign was blank");
			} else if (valid == ValidationState.VALID) {
				HeldSign.trigger(toTrigger, this.type, args);
			}
		}
	}
}
