package dsiwars.co.cc.HeldStone.sign.functional;

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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class BoltSign extends HeldSign {

	private boolean lastState = false;

	@Override
	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is != InputState.HIGH) {
			this.lastState = false;
			return;
		} else {
			if (this.lastState == true) {
				return;
			}
			this.lastState = true;
		}

		Location strikeTarget = getHostLocation().clone();
		strikeTarget.setY(127);

		for (int y = 127; y > 0; y--) {
			if (strikeTarget.getBlock().getType() != Material.AIR) {
				break;
			}
			strikeTarget.setY(y);
		}

		if (!this.isEffect) {
			getWorld().strikeLightning(strikeTarget);
		} else {
			getWorld().strikeLightningEffect(strikeTarget);
		}
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	boolean isEffect = true;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {

		String type = this.getLines(event)[1];

		if (type.equalsIgnoreCase("REAL")) {
			this.isEffect = false;
		} else if (type.equalsIgnoreCase("FAKE")) {
			this.isEffect = true;
		} else {
		}

		if (!reload) {
			this.clearArgLines(event);
			if (this.isEffect) {
				this.setLine(1, "FAKE", event);
			} else {
				this.setLine(1, "REAL", event);
			}
		}

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("Bolt sign accepted.");
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