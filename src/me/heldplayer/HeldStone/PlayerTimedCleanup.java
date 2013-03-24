
package me.heldplayer.HeldStone;

import java.util.ArrayList;

public class PlayerTimedCleanup implements Runnable {
    private final HeldStone main;

    public PlayerTimedCleanup(HeldStone plugin) {
        main = plugin;
    }

    public void run() {
        ArrayList<Player> players = main.pmng.getAll();

        for (Player player : players) {
            if (player.lastAccess + 3600000L < System.currentTimeMillis()) {
                main.pmng.remove(player);
            }
        }
    }
}
