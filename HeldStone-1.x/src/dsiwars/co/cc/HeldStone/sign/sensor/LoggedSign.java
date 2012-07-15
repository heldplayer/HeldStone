package dsiwars.co.cc.HeldStone.sign.sensor;

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
import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class LoggedSign extends HeldSign {

	private boolean isNew = true;
	private boolean lastState = false;

	@Override
	protected void triggersign(TriggerType type, Object args) {
		Iterator<Player> i = getWorld().getPlayers().iterator();
		boolean inWorld = false;
		while (i.hasNext()) {
			if (i.next().getName().equalsIgnoreCase(this.playerName)) {
				inWorld = true;
			}
		}

		if (this.lastState != inWorld || this.isNew) {
			this.isNew = false;
			this.lastState = inWorld;
			setOutput(inWorld);
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	private String playerName;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {
		String playerLine = this.getLines(event)[1];
		playerLine = playerLine.trim();
		playerLine = playerLine.substring(0, Math.min(16, playerLine.length()));

		if (playerLine.length() < 1) {
			playerLine = getOwnerName();
		}

		if (!reload) {
			this.clearArgLines(event);
			this.setLine(1, playerLine, event);
		}

		this.playerName = playerLine;

		this.main.sgc.register(this, TriggerType.TIMER_SECOND);

		if (!reload) {
			init("Logged sign accepted.");
		}

		return true;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public String getTriggerTypesString() {
		return TriggerType.TIMER_SECOND.name();
	}
}