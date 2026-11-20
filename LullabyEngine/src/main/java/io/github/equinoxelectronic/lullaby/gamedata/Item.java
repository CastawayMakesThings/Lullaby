package io.github.equinoxelectronic.lullaby.gamedata;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    public String classPointer;
    public int quantity;

    public Item() {}

    public Item(String classPointer, int quantity) {
        this.classPointer = classPointer;
        this.quantity = quantity;
    }
}
