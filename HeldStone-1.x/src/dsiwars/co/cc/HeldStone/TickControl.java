package dsiwars.co.cc.HeldStone;

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
import java.util.Iterator;

import dsiwars.co.cc.HeldStone.sign.HeldSign;

public class TickControl implements Runnable {

	private volatile ArrayList<HeldSign> nextTick;
	private ArrayList<HeldSign> thisTick;
	private ArrayList<HeldSign> kill;
	@SuppressWarnings("unused")
	private final HeldStone main;

	public TickControl(HeldStone main) {
		nextTick = new ArrayList<HeldSign>();
		kill = new ArrayList<HeldSign>();
		this.main = main;
	}

	public void register(HeldSign sign) {
		nextTick.add(sign);
	}

	public void run() {

		thisTick = nextTick;
		nextTick = new ArrayList<HeldSign>();

		Iterator<HeldSign> i = thisTick.iterator();
		HeldSign csign = null;
		while (i.hasNext()) {
			csign = i.next();
			if ((!kill.contains(csign)) && csign.tick()) {
				this.register(csign);
			}
		}
	}

	public synchronized void purge(HeldSign sign) {
		kill.add(sign);
	}
}
