package dsiwars.co.cc.HeldStone.sign;

/*
 * This code is Copyright (C) 2011 Chris Bode, Some Rights Reserved.
 * Copyright (C) 1999-2002 Technical Pursuit Inc., All Rights Reserved. Patent
 * Pending, Technical Pursuit Inc.
 * Unless explicitly acquired and licensed from Licensor under the Technical
 * Pursuit License ("TPL") Version 1.0 or greater, the contents of this file are
 * subject to the Reciprocal Public License ("RPL") Version 1.1, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in
 * either source code or executable form, except in compliance with the terms
 * and conditions of the RPL.
 * You may obtain a copy of both the TPL and the RPL (the "Licenses") from
 * Technical Pursuit Inc. at http://www.technicalpursuit.com.
 * All software distributed under the Licenses is provided strictly on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND
 * TECHNICAL PURSUIT INC. HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING
 * WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the Licenses
 * for specific language governing rights and limitations under the Licenses.
 */
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import dsiwars.co.cc.HeldStone.HeldStone;
import dsiwars.co.cc.HeldStone.sign.HeldSign.ValidationState;

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
				main.sgc.invalidate(toTrigger, "Sign was either gone or had different text.");
			} else if (valid == ValidationState.BLANK) {
				if (main.cfgWipeProtection) {
					if (this.type == TriggerType.PING) {
						return;
					}

					this.main.e("Sign lost its text, repairing...");

					this.main.alert(toTrigger.getOwnerName(), "Sign lost text! Repairing...", ChatColor.RED);

					Sign s = (Sign) (toTrigger.getBlock().getState());
					String[] knownLines = toTrigger.getLines();
					for (int j = 0; j < 4; j++) {
						s.setLine(j, knownLines[j]);
					}
					s.update();

				} else {
					main.sgc.invalidate(toTrigger, "Sign was blank. (wipe-protection is off)");
				}
			} else if (valid == ValidationState.VALID) {
				toTrigger.trigger(this.type, args);
			}
		}
	}
}