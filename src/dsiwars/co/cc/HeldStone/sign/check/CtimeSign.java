
package dsiwars.co.cc.HeldStone.sign.check;

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

public class CtimeSign extends HeldSign {

    @Override
    protected void triggersign(TriggerType type, Object args) {
        int ctime = (int) getWorld().getTime();

        boolean ncurrent = false;

        if (ctime <= this.highTime && ctime >= this.lowTime) {
            ncurrent = this.betweenState;
        }
        else {
            ncurrent = !this.betweenState;
        }

        if (ncurrent) {
            setOutput(true);
            this.current = true;
        }
        else {
            setOutput(false);
            this.current = false;
        }
    }

    @Override
    protected void setNBTData(NBTBase tag) {}

    @Override
    public NBTBase getNBTData() {
        return new NBTTagInt(0);
    }

    boolean betweenState;
    int lowTime, highTime;
    boolean current = false;
    boolean autoTrigger = false;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {
        int l1, l2;
        String autoTriggerline;

        String[] lines = this.getLines(event);

        autoTriggerline = lines[3];

        if (lines[1].equals("") && lines[2].equals("")) {
            this.lowTime = 0;
            l1 = this.lowTime;
            this.highTime = 12000;
            l2 = this.highTime;
            this.betweenState = true;
        }
        else {
            l1 = fixTime(parseTime(lines[1]));
            l2 = fixTime(parseTime(lines[2]));

            if (l1 <= l2) {
                this.betweenState = true;
                this.highTime = l2;
                this.lowTime = l1;
            }
            else {

                this.betweenState = false;
                this.highTime = l1;
                this.lowTime = l2;
            }
        }

        if (autoTriggerline.equalsIgnoreCase("FALSE")) {
            autoTriggerline = "FALSE";
            this.autoTrigger = false;
        }
        else {
            autoTriggerline = "TRUE";
            this.autoTrigger = true;
        }

        if (!reload) {
            this.clearArgLines(event);
            this.setLine(1, "" + l1, event);
            this.setLine(2, "" + l2, event);
            this.setLine(3, autoTriggerline, event);
        }

        if (this.autoTrigger) {
            this.main.sgc.register(this, TriggerType.TIMER_SECOND);
        }
        else {
            this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
        }

        if (!reload) {
            init("ctime sign accepted.");
        }

        triggersign(null, null);

        return true;
    }

    private int parseTime(String ts) {
        if (isInteger(ts)) {
            return Integer.parseInt(ts);
        }
        else {
            return 0;
        }
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private int fixTime(int time) {
        if (time > 24000) {
            time = 24000;
        }
        if (time < 0) {
            time = 0;
        }
        return time;
    }

    @Override
    public void invalidate() {}

    @Override
    public String getTriggerTypesString() {
        if (this.autoTrigger) {
            return TriggerType.TIMER_SECOND.name();
        }
        else {
            return TriggerType.REDSTONE_CHANGE.name();
        }
    }
}
