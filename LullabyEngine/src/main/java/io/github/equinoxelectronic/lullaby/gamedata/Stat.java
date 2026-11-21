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

    public static Stat make(String id, String displayName, int value) {
        return new Stat(id, displayName, value);
    }

    public static Stat create(String id, String displayName, int value) {
        return new Stat(id, displayName, value);
    }

    public Stat id(String id) {
        this.identifier = id;
        return this;
    }

    public Stat identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public Stat displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Stat value(int value) {
        this.value = value;
        return this;
    }

    public Stat withValue(int value) {
        this.value = value;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
