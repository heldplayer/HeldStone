package dsiwars.co.cc.HeldStone.sign.logic;

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
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagString;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class ToggleSign extends HeldSign {

	private boolean lastState = false;
	private boolean out = false;

	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is == InputState.HIGH && !lastState) {
			lastState = true;

			if (rise) {
				this.setOutput(!out);
				out = !out;
			}

		} else if ((is == InputState.LOW || is == InputState.DISCONNECTED) && lastState) {
			lastState = false;

			if (!rise) {
				this.setOutput(!out);
				out = !out;
			}

		} else {
			return;
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
		NBTTagString TagString = (NBTTagString) tag;

		String data = TagString.value;

		if (data == null || data.length() <= 0) {
			return;
		}
		String[] dataArgs = data.split("-");
		if (dataArgs.length == 2) {
			lastState = Boolean.parseBoolean(dataArgs[0]);
			out = Boolean.parseBoolean(dataArgs[1]);
		}
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagString(Boolean.toString(lastState) + "-" + Boolean.toString(out));
	}

	private boolean rise = true;

	protected boolean declare(boolean reload, SignChangeEvent event) {
		String typeLine = this.getLines(event)[1].trim();

		if (typeLine.equalsIgnoreCase("RISING")) {
			rise = true;
		} else if (typeLine.equalsIgnoreCase("ON")) {
			rise = true;
		} else if (typeLine.equalsIgnoreCase("FALLING")) {
			rise = false;
		} else if (typeLine.equalsIgnoreCase("OFF")) {
			rise = false;
		} else {
			rise = true;
		}

		if (!reload) {
			this.clearArgLines();
			this.setLine(1, rise ? "RISING" : "FALLING", event);
		}

		main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			this.init("Toggle sign accepted.");
		}

		return true;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.REDSTONE_CHANGE.name();
	}
}