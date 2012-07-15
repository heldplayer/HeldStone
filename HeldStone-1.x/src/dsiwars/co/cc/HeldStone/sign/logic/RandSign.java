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
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class RandSign extends HeldSign {

	private boolean lastState = false;
	private Random rnd = new Random();

	@Override
	protected void triggersign(TriggerType type, Object args) {
		InputState is = this.getInput(1, (BlockRedstoneEvent) args);

		if (is == InputState.HIGH && !this.lastState) {
			this.lastState = true;
			randomize();
		} else if ((is == InputState.LOW || is == InputState.DISCONNECTED) && this.lastState) {
			this.lastState = false;
			setOutput(false);
		}
	}

	public void randomize() {
		setOutput((this.rnd.nextInt(100) <= this.chance ? true : false));
	}

	@Override
	protected void setNBTData(NBTBase tag) {
	}

	@Override
	public NBTBase getNBTData() {
		return new NBTTagInt(0);
	}

	int chance = 0;

	@Override
	protected boolean declare(boolean reload, SignChangeEvent event) {
		try {
			this.chance = Integer.parseInt(this.getLines()[1]);
		} catch (Exception ex) {
			if (!reload) {
				this.main.alert(getOwnerName(), "The entered chance was not a number!", ChatColor.RED);
				event.setCancelled(true);
			}
			return false;
		}

		if (this.chance <= 0) {
			if (!reload) {
				this.main.alert(getOwnerName(), "The entered chance was too small.", ChatColor.RED);
				event.setCancelled(true);
			}
			return false;
		}

		if (this.chance >= 100) {
			if (!reload) {
				this.main.alert(getOwnerName(), "The entered chance was too big.", ChatColor.RED);
				event.setCancelled(true);
			}
			return false;
		}

		if (!reload) {
			this.clearArgLines();
			this.setLine(1, this.chance + "", event);
		}

		this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
		if (!reload) {
			init("Rand sign accepted.");
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