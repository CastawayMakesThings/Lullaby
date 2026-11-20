package io.github.equinoxelectronic.lullaby.gamedata;

import java.io.Serializable;

public class Stat implements Serializable {
    private static final long serialVersionUID = 1L;

    private String displayName;
    private String identifier;
    private int value;

    public Stat(String id, String displayName, int value) {
        this.identifier = id;
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
