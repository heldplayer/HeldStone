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
import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class DelaySign extends HeldSign {

	private boolean lastState;
	private boolean out = false;
	private boolean ticking = false;

	@Override
	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is == InputState.HIGH && !this.lastState) {
			this.lastState = true;
			if (!this.ticking) {
				startTicking();
			}
			this.ticking = true;
		} else if ((is == InputState.LOW || is == InputState.DISCONNECTED) && this.lastState) {
			this.lastState = false;
			if (!this.ticking) {
				startTicking();
			}
			this.ticking = true;
		} else {
			return;
		}

	}

	private boolean[] states;

	@Override
	public boolean tick() {
		short sum = 0;
		for (int i = 0; i < (this.states.length - 1); i++) {
			this.states[i] = this.states[i + 1];
			sum += this.states[i] ? 1 : -1;
		}

		this.states[this.states.length - 1] = this.lastState;
		sum += this.lastState ? 1 : -1;

		if (this.out != this.states[0]) {
			setOutput(this.states[0]);
			this.out = this.states[0];
		}

		if (Math.abs(sum) == this.period) {
			this.ticking = false;
			return false;
		}
		return true;
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private int period = 20;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {
		String periodLine = this.getLines(event)[1].trim();

		if (periodLine.length() > 0) {
			try {
				this.period = Integer.parseInt(periodLine);
			} catch (Exception e) {
				if (!reload) {
					this.main.alert(getOwnerName(), "Could not understand period, defaulting to 20. (1sec)", ChatColor.RED);
				}
				this.period = 20;
			}
			if (this.period > 500 || this.period <= 0) {
				this.period = 20;
				if (!reload) {
					this.main.alert(getOwnerName(), "The period was either too long or too short. Allowed: 1-500", ChatColor.RED);
				}
			}
		} else {
			this.period = 20;
		}

		if (!reload) {
			this.clearArgLines(event);
			this.setLine(1, Integer.toString(this.period), event);
		}

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("Delay sign accepted.");
		}

		this.states = new boolean[this.period];
		for (int i = 0; i < this.states.length; i++) {
			this.states[i] = false;
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