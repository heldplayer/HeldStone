
package dsiwars.co.cc.HeldStone.sign.chat;

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
import dsiwars.co.cc.HeldStone.NBT.NBTTagString;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class GlAnnounceSign extends HeldSign {

    private boolean lastState = false;

    @Override
    protected void triggersign(TriggerType type, Object args) {
        InputState is = this.getInput(1, (BlockRedstoneEvent) args);

        if (is != InputState.HIGH) {
            this.lastState = false;
            return;
        }
        else {
            if (this.lastState == true) {
                return;
            }
            this.lastState = true;
        }

        this.main.getServer().broadcastMessage(this.color + "[HeldStone] " + this.message);
    }

    @Override
    protected void setNBTData(NBTBase tag) {
        this.message = ((NBTTagString) tag).value;
    }

    @Override
    public NBTBase getNBTData() {
        return new NBTTagString(this.message);
    }

    String message = null;
    ChatColor color = ChatColor.WHITE;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {

        if (!reload) {
            this.message = this.main.players.safelyGet(getOwnerName(), this.main).message;
            if (this.message == null) {
                this.main.alert(getOwnerName(), "You must set a message first. " + ChatColor.AQUA + "/hs msg <message>", ChatColor.RED);
                return false;
            }
        }

        try {
            this.color = ChatColor.valueOf(this.getLines()[1].toUpperCase());
        }
        catch (Exception ex) {
            try {
                this.color = ChatColor.getByChar(this.getLines()[1].charAt(0));
            }
            catch (Exception ex2) {
                this.color = ChatColor.WHITE;
            }
        }

        if (!reload) {
            this.clearArgLines(event);
            this.setLine(1, this.color.name(), event);
        }

        this.main.sgc.register(this, TriggerType.REDSTONE_CHANGE);
        if (!reload) {
            init("GlAnnounce sign accepted.");
        }
        return true;
    }

    @Override
    public void invalidate() {}

    @Override
    public String getTriggerTypesString() {
        return TriggerType.REDSTONE_CHANGE.name();
    }
}
