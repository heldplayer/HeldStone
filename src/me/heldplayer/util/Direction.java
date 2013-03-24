
package me.heldplayer.util;

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

import org.bukkit.Location;
import org.bukkit.util.Vector;

public enum Direction {

    EAST, WEST, NORTH, SOUTH, UP, DOWN, ERROR;

    public static Direction opposite(Direction d) {

        switch (d) {
        case EAST:
            return WEST;
        case WEST:
            return EAST;
        case NORTH:
            return SOUTH;
        case SOUTH:
            return NORTH;
        case UP:
            return DOWN;
        case DOWN:
            return UP;
        case ERROR:
            return null;
        }

        return null;
    }

    public static Location shift(Location l, Direction d, int distance) {

        Location l2 = new Location(l.getWorld(), 0, 0, 0);

        l2.setX(l.getBlockX());
        l2.setY(l.getBlockY());
        l2.setZ(l.getBlockZ());

        if (d == EAST) {
            l2.setZ(l.getBlockZ() - distance);
        }

        if (d == WEST) {
            l2.setZ(l.getBlockZ() + distance);
        }

        if (d == NORTH) {
            l2.setX(l.getBlockX() - distance);
        }

        if (d == SOUTH) {
            l2.setX(l.getBlockX() + distance);
        }

        if (d == UP) {
            l2.setY(l.getBlockY() + distance);
        }

        if (d == DOWN) {
            l2.setY(l.getBlockY() - distance);
        }

        return l2;

    }

    public static int[] shift(int[] l, Direction d, int distance) {

        int x, y, z;

        x = l[0];
        y = l[1];
        z = l[2];

        if (d == EAST) {
            z -= (distance);
        }

        if (d == WEST) {
            z += distance;
        }

        if (d == NORTH) {
            x -= (distance);
        }

        if (d == SOUTH) {
            x += distance;
        }

        if (d == UP) {
            y += distance;
        }

        if (d == DOWN) {
            y -= (distance + 1);
        }

        int lo[] = new int[3];

        lo[0] = x;
        lo[1] = y;
        lo[2] = z;

        return lo;

    }

    public static Direction right(Direction d) {

        Direction d2 = d;

        if (d == EAST) {
            d2 = SOUTH;
        }

        if (d == WEST) {
            d2 = NORTH;
        }

        if (d == NORTH) {
            d2 = EAST;
        }

        if (d == SOUTH) {
            d2 = WEST;
        }

        return d2;
    }

    public static Direction left(Direction d) {

        Direction d2 = d;

        if (d == EAST) {
            d2 = NORTH;
        }

        if (d == WEST) {
            d2 = SOUTH;
        }

        if (d == NORTH) {
            d2 = WEST;
        }

        if (d == SOUTH) {
            d2 = EAST;
        }

        return d2;
    }

    public static Direction fromString(String line) {

        Direction t;

        try {
            t = Direction.valueOf(line);
        }
        catch (Exception e) {
            t = ERROR;
        }

        return t;

    }

    public static Vector getVelocityVector(Direction dir, Random rand, Double speed) {
        Vector vec = new Vector();
        if (dir == EAST) {
            vec.setZ(-speed);
            vec.setX((rand.nextDouble() - 0.5) / 8);
            vec.setY((rand.nextDouble() - 0.5) / 8);
        }

        if (dir == WEST) {
            vec.setZ(speed);
            vec.setX((rand.nextDouble() - 0.5) / 8);
            vec.setY((rand.nextDouble() - 0.5) / 8);
        }

        if (dir == NORTH) {
            vec.setX(-speed);
            vec.setY((rand.nextDouble() - 0.5) / 8);
            vec.setZ((rand.nextDouble() - 0.5) / 8);
        }

        if (dir == SOUTH) {
            vec.setX(speed);
            vec.setY((rand.nextDouble() - 0.5) / 8);
            vec.setZ((rand.nextDouble() - 0.5) / 8);
        }

        if (dir == UP) {
            vec.setY(speed);
            vec.setX((rand.nextDouble() - 0.5) / 8);
            vec.setZ((rand.nextDouble() - 0.5) / 8);
        }

        if (dir == DOWN) {
            vec.setY(-speed);
            vec.setX((rand.nextDouble() - 0.5) / 8);
            vec.setZ((rand.nextDouble() - 0.5) / 8);
        }

        return vec;
    }

    public static Vector getVelocityVector(Direction dir) {
        Vector vec = new Vector(0, 0, 0);
        if (dir == EAST) {
            vec.setZ(-2);
        }

        if (dir == WEST) {
            vec.setZ(2);
        }

        if (dir == NORTH) {
            vec.setX(-2);
        }

        if (dir == SOUTH) {
            vec.setX(2);
        }

        if (dir == UP) {
            vec.setY(2);
        }

        if (dir == DOWN) {
            vec.setY(-2);
        }

        return vec;
    }

    public static Location getVelocityLocation(Direction dir, Location baseLoc) {
        Location loc = baseLoc.clone();
        loc.setPitch(90.0F);
        loc.setYaw(0.0F);
        if (dir == EAST) {
            loc.setYaw(90.0F);
        }

        if (dir == WEST) {
            loc.setYaw(240.0F);
        }

        if (dir == NORTH) {
            loc.setYaw(0.0F);
        }

        if (dir == SOUTH) {
            loc.setYaw(180.0F);
        }

        if (dir == UP) {
            loc.setPitch(360.0F);
        }

        if (dir == DOWN) {
            loc.setPitch(90.0F);
        }

        return loc;
    }
}
