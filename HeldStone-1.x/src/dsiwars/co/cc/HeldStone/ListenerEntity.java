
package dsiwars.co.cc.HeldStone;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ListenerEntity implements Listener {

    private final HeldStone main;

    public ListenerEntity(HeldStone main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (this.main.explodingList.contains(event.getEntity())) {
            event.setYield(0);

            this.main.explodingList.remove(event.getEntity());
        }
    }
}
