package io.github.equinoxelectronic.lullaby.gamedata;

import io.github.equinoxelectronic.lullaby.mechanics.Time;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//EVERYTHING THAT IS PERSISTENT AND CONTEXT-DEPENDENT IN A USER'S SESSION
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private String previousSceneID;
    private String currentSceneID;
    private ArrayList<String> visitedScenes = new ArrayList<>();
    private String playerName;

    private HashMap<String, Boolean> flags = new HashMap<>();
    private HashMap<String, Integer> counters = new HashMap<>();
    private HashMap<String, String> strings = new HashMap<>();
    private HashMap<String, Object> vars = new HashMap<>();
    private HashMap<String, Stat> stats = new HashMap<>();
    private HashMap<String, Integer> sceneVisitCounts = new HashMap<>();
    private HashMap<String, Integer> choiceSelectedCounts = new HashMap<>();
    private Inventory inventory = new Inventory();
    private Time gameTime = new Time(0);
    private String owner;

    private transient List<Runnable> changeListeners = new ArrayList<>();

    public void init(String owner) {
        this.owner = owner;
    }

    public void addChangeListener(Runnable listener) {
        if (changeListeners == null) {
            changeListeners = new ArrayList<>();
        }
        changeListeners.add(listener);
    }


    public void notifyStateChanged() {
        if (changeListeners != null) {
            for (Runnable listener : changeListeners) {
                listener.run();
            }
        }
    }

    public HashMap<String, Stat> getStats() {
        return stats;
    }

    public void addStat(String statID, String displayName, int value) {
        stats.put(statID, new Stat(statID, displayName, value));
        notifyStateChanged();
    }

    public void addStat(Stat stat) {
        if (stat != null && stat.getIdentifier() != null) {
            stats.put(stat.getIdentifier(), stat);
            notifyStateChanged();
        }
    }

    public Stat getStat(String statID) {
        return stats.get(statID);
    }

    public int getStatValue(String statID) {
        Stat stat = stats.get(statID);
        return stat != null ? stat.getValue() : 0;
    }

    public void setStatValue(String statID, int value) {
        Stat stat = stats.get(statID);
        if (stat != null) {
            stat.setValue(value);
            notifyStateChanged();
        }
    }

    public void modifyStat(String statID, int delta) {
        Stat stat = stats.get(statID);
        if (stat != null) {
            stat.setValue(stat.getValue() + delta);
            notifyStateChanged();
        }
    }

    public boolean hasStat(String statID) {
        return stats.containsKey(statID);
    }

    public Time getGameTime() {
        return gameTime;
    }

    public void setGameTime(Time gameTime) {
        this.gameTime = gameTime;
        notifyStateChanged();
    }

    public void advanceTime(long seconds) {
        long current = gameTime != null ? gameTime.seconds() : 0;
        setGameTime(new Time(current + seconds));
    }

    public void addTime(long seconds) {
        advanceTime(seconds);
    }


    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getCurrentSceneID() {
        return currentSceneID;
    }

    public void setCurrentSceneID(String currentSceneID) {
        this.previousSceneID = this.currentSceneID;
        this.currentSceneID = currentSceneID;
        if (currentSceneID != null) {
            this.visitedScenes.add(currentSceneID);
            this.sceneVisitCounts.put(currentSceneID, this.sceneVisitCounts.getOrDefault(currentSceneID, 0) + 1);
        }
    }

    public String getPreviousSceneID() {
        return previousSceneID;
    }

    public ArrayList<String> getVisitedScenes() {
        return visitedScenes;
    }

    public HashMap<String, Boolean> getFlags() {
        return flags;
    }

    public HashMap<String, Object> getVars() {
        return vars;
    }

    public HashMap<String, String> getStrings() {
        return strings;
    }

    public HashMap<String, Integer> getCounters() {
        return counters;
    }

    public HashMap<String, Integer> getSceneVisitCounts() {
        return sceneVisitCounts;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        notifyStateChanged();
    }

    public String getOwner() {
        return owner;
    }

    public boolean getFlag(String flagName) {
        return flags.getOrDefault(flagName, false);
    }

    public void setFlag(String flagName, boolean value) {
        flags.put(flagName, value);
        vars.put(flagName, value);
        notifyStateChanged();
    }

    public int getCounter(String counterName) {
        return counters.getOrDefault(counterName, 0);
    }

    public void setCounter(String counterName, int value) {
        counters.put(counterName, value);
        vars.put(counterName, value);
        notifyStateChanged();
    }

    public int incrementCounter(String counterName) {
        return incrementCounter(counterName, 1);
    }

    public int incrementCounter(String counterName, int amount) {
        int newVal = getCounter(counterName) + amount;
        setCounter(counterName, newVal);
        return newVal;
    }

    public int decrementCounter(String counterName) {
        return decrementCounter(counterName, 1);
    }

    public int decrementCounter(String counterName, int amount) {
        int newVal = getCounter(counterName) - amount;
        setCounter(counterName, newVal);
        return newVal;
    }

    public String getString(String stringName) {
        if (strings.containsKey(stringName)) {
            return strings.get(stringName);
        }
        Object varVal = vars.get(stringName);
        return varVal != null ? varVal.toString() : null;
    }

    public void setString(String stringName, String value) {
        strings.put(stringName, value);
        vars.put(stringName, value);
        notifyStateChanged();
    }

    public Object getVar(String varName) {
        if (vars.containsKey(varName)) {
            return vars.get(varName);
        }
        if (strings.containsKey(varName)) {
            return strings.get(varName);
        }
        if (counters.containsKey(varName)) {
            return counters.get(varName);
        }
        if (flags.containsKey(varName)) {
            return flags.get(varName);
        }
        if (stats.containsKey(varName)) {
            return stats.get(varName).getValue();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getVar(String varName, Class<T> type) {
        Object val = getVar(varName);
        if (val == null) return null;
        if (type.isInstance(val)) {
            return (T) val;
        }
        if (type == String.class) {
            return (T) val.toString();
        }
        if (type == Integer.class && val instanceof Number) {
            return (T) Integer.valueOf(((Number) val).intValue());
        }
        if (type == Double.class && val instanceof Number) {
            return (T) Double.valueOf(((Number) val).doubleValue());
        }
        if (type == Long.class && val instanceof Number) {
            return (T) Long.valueOf(((Number) val).longValue());
        }
        if (type == Boolean.class && val instanceof String) {
            return (T) Boolean.valueOf(Boolean.parseBoolean((String) val));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getVar(String varName, T defaultValue) {
        Object val = getVar(varName);
        if (val == null) return defaultValue;
        try {
            return (T) val;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void setVar(String varName, Object value) {
        vars.put(varName, value);
        if (value instanceof String) {
            strings.put(varName, (String) value);
        } else if (value instanceof Integer) {
            counters.put(varName, (Integer) value);
        } else if (value instanceof Boolean) {
            flags.put(varName, (Boolean) value);
        }
        notifyStateChanged();
    }

    public boolean isVarEqual(String varName, Object expectedValue) {
        Object actual = getVar(varName);
        if (actual == null && expectedValue == null) {
            return true;
        }
        if (actual == null || expectedValue == null) {
            return false;
        }
        if (actual.equals(expectedValue)) {
            return true;
        }
        if (actual instanceof Number && expectedValue instanceof Number) {
            return Double.compare(((Number) actual).doubleValue(), ((Number) expectedValue).doubleValue()) == 0;
        }
        if (actual instanceof Number && expectedValue instanceof String) {
            try {
                double parsed = Double.parseDouble((String) expectedValue);
                return Double.compare(((Number) actual).doubleValue(), parsed) == 0;
            } catch (NumberFormatException ignored) {
            }
        }
        if (expectedValue instanceof Number && actual instanceof String) {
            try {
                double parsed = Double.parseDouble((String) actual);
                return Double.compare(parsed, ((Number) expectedValue).doubleValue()) == 0;
            } catch (NumberFormatException ignored) {
            }
        }
        return actual.toString().equals(expectedValue.toString());
    }

    public boolean hasItem(String classPointer) {
        return inventory != null && inventory.hasItem(classPointer);
    }

    public boolean hasItem(String classPointer, int quantity) {
        return inventory != null && inventory.hasItem(classPointer, quantity);
    }

    public int getItemQuantity(String classPointer) {
        return inventory != null ? inventory.getItemQuantity(classPointer) : 0;
    }

    public void giveItem(String classPointer, int quantity) {
        if (inventory != null) {
            inventory.addItem(classPointer, quantity);
            notifyStateChanged();
        }
    }

    public void giveItem(String classPointer) {
        giveItem(classPointer, 1);
    }

    public void giveItem(Item item) {
        if (inventory != null && item != null) {
            inventory.addItem(item);
            notifyStateChanged();
        }
    }

    public void removeItem(String classPointer, int quantity) {
        if (inventory != null) {
            inventory.removeItem(classPointer, quantity);
            notifyStateChanged();
        }
    }

    public void removeItem(String classPointer) {
        removeItem(classPointer, 1);
    }

    public void removeItem(Item item) {
        if (inventory != null && item != null) {
            inventory.removeItem(item);
            notifyStateChanged();
        }
    }

    public int getSceneVisitCount(String sceneId) {
        return sceneVisitCounts.getOrDefault(sceneId, 0);
    }

    public int getChoiceSelectedCount(String choiceLabel) {
        return choiceSelectedCounts.getOrDefault(choiceLabel, 0);
    }

    public void recordChoiceSelected(String choiceLabel) {
        if (choiceLabel != null) {
            choiceSelectedCounts.put(choiceLabel, getChoiceSelectedCount(choiceLabel) + 1);
        }
    }

    public HashMap<String, Integer> getChoiceSelectedCounts() {
        return choiceSelectedCounts;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.changeListeners = new ArrayList<>();
    }

    public void copyFrom(GameState other) {
        if (other == null) return;
        this.previousSceneID = other.previousSceneID;
        this.currentSceneID = other.currentSceneID;
        this.visitedScenes = other.visitedScenes != null ? new ArrayList<>(other.visitedScenes) : new ArrayList<>();
        this.playerName = other.playerName;
        this.flags = other.flags != null ? new HashMap<>(other.flags) : new HashMap<>();
        this.counters = other.counters != null ? new HashMap<>(other.counters) : new HashMap<>();
        this.strings = other.strings != null ? new HashMap<>(other.strings) : new HashMap<>();
        this.vars = other.vars != null ? new HashMap<>(other.vars) : new HashMap<>();
        this.stats = other.stats != null ? new HashMap<>(other.stats) : new HashMap<>();
        this.sceneVisitCounts = other.sceneVisitCounts != null ? new HashMap<>(other.sceneVisitCounts) : new HashMap<>();
        this.choiceSelectedCounts = other.choiceSelectedCounts != null ? new HashMap<>(other.choiceSelectedCounts) : new HashMap<>();
        this.inventory = other.inventory;
        this.gameTime = other.gameTime;
        this.owner = other.owner;
        notifyStateChanged();
    }

    public void loadFrom(GameState other) {
        copyFrom(other);
    }

    public void save(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        }
    }

    public void save(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        save(new File(filePath));
    }

    public void saveToFile(File file) throws IOException {
        save(file);
    }

    public void saveToFile(String filePath) throws IOException {
        save(filePath);
    }

    public static GameState load(File file) throws IOException, ClassNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            GameState state = (GameState) ois.readObject();
            if (state.changeListeners == null) {
                state.changeListeners = new ArrayList<>();
            }
            return state;
        }
    }

    public static GameState load(String filePath) throws IOException, ClassNotFoundException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        return load(new File(filePath));
    }

    public static GameState loadFromFile(File file) throws IOException, ClassNotFoundException {
        return load(file);
    }

    public static GameState loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        return load(filePath);
    }
}
