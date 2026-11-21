package io.github.equinoxelectronic.lullaby.gamedata;

import java.io.Serializable;

public class ItemClass implements Serializable {
    private static final long serialVersionUID = 1L;

    public String displayName;
    public String id;
    public String description;

    public ItemClass() {}

    public ItemClass(String id, String displayName, String description) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
    }

    public static ItemClass make(String id, String displayName, String description) {
        return new ItemClass(id, displayName, description);
    }

    public static ItemClass create(String id, String displayName, String description) {
        return new ItemClass(id, displayName, description);
    }

    public ItemClass id(String id) {
        this.id = id;
        return this;
    }

    public ItemClass displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemClass description(String description) {
        this.description = description;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
