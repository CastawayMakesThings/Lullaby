package io.github.equinoxelectronic.lullaby.gamedata;

import java.io.Serializable;
import java.util.HashMap;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    public HashMap<String, Item> items = new HashMap<>();
    private HashMap<String, ItemClass> gameItems = new HashMap<>();

    public void registerItemClass(ItemClass itemClass) {
        if (itemClass != null && itemClass.id != null) {
            gameItems.put(itemClass.id, itemClass);
        }
    }

    public HashMap<String, ItemClass> getGameItems() {
        return gameItems;
    }

    public boolean hasItem(String classPointer) {
        return hasItem(classPointer, 1);
    }

    public boolean hasItem(String classPointer, int quantity) {
        if (classPointer == null) return false;
        Item item = items.get(classPointer);
        return item != null && item.quantity >= quantity;
    }

    public int getItemQuantity(String classPointer) {
        if (classPointer == null) return 0;
        Item item = items.get(classPointer);
        return item != null ? item.quantity : 0;
    }

    public void addItem(Item item) {
        if (item == null || item.classPointer == null) return;
        if (items.containsKey(item.classPointer)) {
            items.get(item.classPointer).quantity += item.quantity;
        } else {
            items.put(item.classPointer, item);
        }
    }

    public void addItem(String classPointer, int quantity) {
        if (classPointer == null || quantity <= 0) return;
        Item item = items.get(classPointer);
        if (item != null) {
            item.quantity += quantity;
        } else {
            Item newItem = new Item(classPointer, quantity);
            items.put(classPointer, newItem);
        }
    }

    public void removeItem(Item item) {
        if (item == null || item.classPointer == null) return;
        removeItem(item.classPointer, item.quantity);
    }

    public void removeItem(String classPointer, int quantity) {
        if (classPointer == null || quantity <= 0) return;
        if (items.containsKey(classPointer)) {
            Item existing = items.get(classPointer);
            existing.quantity -= quantity;
            if (existing.quantity <= 0) {
                items.remove(classPointer);
            }
        }
    }

    public void setItem(String classPointer, int quantity) {
        if (classPointer == null) return;
        if (quantity <= 0) {
            items.remove(classPointer);
        } else {
            items.put(classPointer, new Item(classPointer, quantity));
        }
    }

    public void setItem(Item item) {
        if (item == null || item.classPointer == null) return;
        if (item.quantity <= 0) {
            items.remove(item.classPointer);
        } else {
            items.put(item.classPointer, item);
        }
    }
}
