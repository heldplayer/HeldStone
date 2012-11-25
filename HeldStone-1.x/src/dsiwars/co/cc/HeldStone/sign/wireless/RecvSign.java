
package dsiwars.co.cc.HeldStone.sign.wireless;

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
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagInt;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;
import dsiwars.co.cc.HeldStone.sign.WirelessPacket;

public class RecvSign extends HeldSign {

    @Override
    protected void triggersign(TriggerType type, Object args) {
        if (type == TriggerType.SEND_DATA) {
            WirelessPacket p = (WirelessPacket) args;

            if (this.p.sameChannel(p)) {
                setOutput(p.getState());
            }
        }
    }

    @Override
    protected void setNBTData(NBTBase tag) {}

    @Override
    public NBTBase getNBTData() {
        return new NBTTagInt(0);
    }

    private WirelessPacket p;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {

        String band = this.getLines()[2];
        String channel = this.getLines()[1];

        if (!reload) {

            if (band.trim().equals("")) {
                band = getOwnerName();
            }
            else if (!band.equalsIgnoreCase(getOwnerName())) {
                if (band.charAt(0) == '$') {
                    // GOOD!
                }
                else {
                    band = "$" + band;
                }
            }

            this.clearArgLines(event);
            this.setLine(1, channel, event);
            this.setLine(2, band, event);

        }

        this.p = new WirelessPacket(band, channel, false);

        this.main.sgc.register(this, TriggerType.SEND_DATA);
        if (!reload) {
            init("Recv sign accepted.");
        }

        return true;
    }

    @Override
    public void invalidate() {}

    @Override
    public String getTriggerTypesString() {
        return TriggerType.SEND_DATA.name();
    }
}
