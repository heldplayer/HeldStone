
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
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

public class HeldPlayer {

    public final String name;
    public Location loc1, loc2, loc3;
    public boolean l1 = true;
    public String message = null;
    private final HeldStone main;
    public int snow = 0;
    public Minecart ridingCart = null;

    public HeldPlayer(String name, HeldStone main) {
        this.name = name;
        this.main = main;
        //this.main.i("Created new player tracker for " + name + ".");
    }

    public String getName() {
        return this.name;
    }

    public Player getPlayer() {
        return this.main.getServer().getPlayer(this.name);
    }

    public boolean isOnline() {
        return getPlayer() != null;
    }

    public void setLoc1(Location l) {
        this.main.alert(getName(), "Set point one.", ChatColor.GREEN);
        this.loc1 = l;
        this.l1 = false;
    }

    public void setLoc2(Location l) {
        this.main.alert(getName(), "Set point two.", ChatColor.GREEN);
        this.loc2 = l;
        this.l1 = true;
    }

    public void setLoc(Location l) {
        if (this.l1) {
            setLoc1(l);
        }
        else {
            setLoc2(l);
        }
    }

    public void setLoc3(Location l) {
        this.main.alert(getName(), "Set block location.", ChatColor.GREEN);
        this.loc3 = l;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
