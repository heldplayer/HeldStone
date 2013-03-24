
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
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import dsiwars.co.cc.HeldStone.CommandArgWrapper;
import dsiwars.co.cc.HeldStone.NBT.NBTBase;
import dsiwars.co.cc.HeldStone.NBT.NBTTagString;
import dsiwars.co.cc.HeldStone.sign.HeldSign;
import dsiwars.co.cc.HeldStone.sign.TriggerType;

public class TriggerSign extends HeldSign {

    @Override
    protected void triggersign(TriggerType type, Object args) {
        if (type == TriggerType.TRIGGER_COMMAND) {
            CommandArgWrapper command = (CommandArgWrapper) args;

            if (command.args.length >= 1) {
                String id = command.args[0];
                if (this.command.equalsIgnoreCase(id)) {
                    if (this.player.equalsIgnoreCase("@")) {
                        this.state = !this.state;
                    }
                    else if (command.commandSender instanceof Player) {
                        Player p = (Player) command.commandSender;
                        boolean isNamed = this.player.equalsIgnoreCase(p.getName());
                        if (isNamed) {
                            this.state = !this.state;
                        }
                    }
                }
            }
        }

        if (isLoaded()) {
            setOutput(this.state);
        }
    }

    @Override
    protected void setNBTData(NBTBase tag) {
        NBTTagString TagString = (NBTTagString) tag;

        String data = TagString.value;

        if (data != null && (!data.equals(""))) {
            if (data.equals("H")) {
                this.state = true;
            }
        }
    }

    @Override
    public NBTBase getNBTData() {
        if (this.state) {
            return new NBTTagString("H");
        }
        else {
            return new NBTTagString("L");
        }
    }

    boolean state = false;
    String command, player;

    @Override
    protected boolean declare(boolean reload, SignChangeEvent event) {
        String nullCmd = getOwnerName().trim();

        this.command = this.getLines(event)[1].replace("/", "");
        this.player = this.getLines(event)[2];

        if (this.player == null || this.player.equals("")) {
            this.player = "@";
            if (!reload) {
                this.main.alert(getOwnerName(), "Player name defaulted to \"@\" because you did not specify a name.", ChatColor.AQUA);
                this.main.alert(getOwnerName(), "This sign will be triggerable by anyone.", ChatColor.AQUA);
            }
        }

        if (this.command == null || this.command.equals("")) {
            this.command = nullCmd;
            if (!reload) {
                this.main.alert(getOwnerName(), "Trigger identifier defaulted to your name because you did not specify your own.", ChatColor.AQUA);
            }
        }

        if (!reload) {
            this.clearArgLines(event);
            this.setLine(1, this.command, event);
            this.setLine(2, this.player, event);
        }

        this.main.sgc.register(this, TriggerType.TRIGGER_COMMAND);
        this.main.sgc.register(this, TriggerType.TIMER_SECOND);

        if (!reload) {
            init("Trigger sign accepted.");
        }

        return true;
    }

    @Override
    public void invalidate() {}

    @Override
    public String getTriggerTypesString() {
        return TriggerType.TRIGGER_COMMAND.name() + "; " + TriggerType.TIMER_SECOND.name();
    }
}
