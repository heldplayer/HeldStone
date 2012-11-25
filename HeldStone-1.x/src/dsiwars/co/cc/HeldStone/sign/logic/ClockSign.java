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
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class ClockSign extends HeldSign {

	private boolean ticking;
	private int clock = 0;
	private boolean clockState = false;

	@Override
	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is == InputState.HIGH) {
			startClock();
		} else if ((is == InputState.LOW || is == InputState.DISCONNECTED)) {
			stopClock();
			setOutput(false);
		}
	}

	public void startClock() {
		if (!this.ticking) {
			startTicking();
			this.ticking = true;
			setOutput(true);
			this.clockState = true;
			this.clock = this.onTime;
		}
	}

	public void stopClock() {
		this.ticking = false;
	}

	@Override
	public boolean tick() {
		if (this.ticking) {
			this.clock--;
			if (this.clock <= 0) {
				this.clock = this.clockState ? this.offTime : this.onTime;
				this.clockState = !this.clockState;
				setOutput(this.clockState);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private int onTime, offTime;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {

		String onLine = this.getLines(event)[1].trim();
		String offLine = this.getLines(event)[2].trim();

		try {
			this.onTime = Integer.parseInt(onLine);
		} catch (Exception e) {
			this.onTime = 20;
		}

		try {
			this.offTime = Integer.parseInt(offLine);
		} catch (Exception e) {
			this.offTime = this.onTime;
		}

		this.onTime = (this.onTime > 0) ? this.onTime : 20;
		this.offTime = (this.offTime > 0) ? this.offTime : 20;

		if (!reload) {
			this.clearArgLines(event);
			this.setLine(1, Integer.toString(this.onTime), event);
			this.setLine(2, Integer.toString(this.offTime), event);
		}

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("Clock sign accepted.");
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