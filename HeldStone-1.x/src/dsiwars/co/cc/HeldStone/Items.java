
package dsiwars.co.cc.HeldStone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;

public class Items {

    public HashMap<Integer, Item> items = new HashMap<Integer, Item>();
    private HashSet<ItemStack> itemStackReference;
    public int size = 0;
    static final Logger log = Logger.getLogger("HeldStone");

    public Items(HashSet<ItemStack> items) {
        this.itemStackReference = items;

        Iterator<ItemStack> i = this.itemStackReference.iterator();

        while (i.hasNext()) {
            ItemStack stack = i.next();

            //log.log(Level.INFO, "[HeldStone] [DEBUG] [Items] Item of type " + stack.getTypeId() + "(amount: " + stack.getAmount() + ")");

            for (int j = 0; j < stack.getAmount(); j++) {
                //log.log(Level.INFO, "[HeldStone] [DEBUG] [Items] J is " + j);
                this.items.put(this.size, new Item(stack.getTypeId(), stack.getDurability()));

                this.size++;
            }
        }
    }

    public void add(ItemStack stack) {
        for (int j = 0; j < stack.getAmount(); j++) {
            this.items.put(this.size, new Item(stack.getTypeId(), stack.getDurability()));

            this.size++;
        }
    }

    public void destroyAll() {
        Iterator<ItemStack> i = this.itemStackReference.iterator();

        while (i.hasNext()) {
            ItemStack stack = i.next();

            stack.setAmount(0);
            stack.setTypeId(0);
        }
    }

    public boolean matches(Items otherItems) {
        if (otherItems.size != this.size) {
            //log.log(Level.INFO, "[HeldStone] [DEBUG] [ItemMatching] Match failed because the amount of items did not match! (" + otherItems.size + " != " + this.size + ")");
            return false;
        }

        HashMap<Integer, Item> tempItems = this.items;

        for (int i = 0; i < otherItems.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (tempItems.containsKey(j)) {
                    if (otherItems.items.get(i).itemID == tempItems.get(j).itemID) {
                        if (otherItems.items.get(i).damage == tempItems.get(j).damage || otherItems.items.get(i).damage < 0) {
                            tempItems.remove(j);
                        }
                    }
                }
            }
        }

        if (tempItems.isEmpty()) {
            return true;
        }

        //log.log(Level.INFO, "[HeldStone] [DEBUG] [ItemMatching] Match failed because not all items got matched!");

        return false;
    }

    public class Item {

        public int itemID = 0;
        public short damage = 0;

        public Item(int itemID, short damage) {
            this.itemID = itemID;
            this.damage = damage;
        }
    }
}
