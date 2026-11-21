package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Choice {
    public String label;
    public Function<GameState, String> dynamicLabel;
    public String targetSceneId;
    public Consumer<GameState> action;
    public List<Predicate<GameState>> displayConditions = new ArrayList<>();
    public List<Predicate<GameState>> hiddenConditions = new ArrayList<>();

    public Choice() {
    }

    public Choice(String label) {
        this.label = label;
    }

    public Choice(String label, String targetSceneId) {
        this.label = label;
        this.targetSceneId = targetSceneId;
    }

    public Choice(Function<GameState, String> dynamicLabel, String targetSceneId) {
        this.dynamicLabel = dynamicLabel;
        this.targetSceneId = targetSceneId;
    }

    public static Choice make(String label, String targetSceneId) {
        return new Choice(label, targetSceneId);
    }

    public static Choice make(Function<GameState, String> dynamicLabel, String targetSceneId) {
        return new Choice(dynamicLabel, targetSceneId);
    }

    public static Choice of(String label, String targetSceneId) {
        return new Choice(label, targetSceneId);
    }

    public static Choice of(Function<GameState, String> dynamicLabel, String targetSceneId) {
        return new Choice(dynamicLabel, targetSceneId);
    }

    public static Choice create(String label, String targetSceneId) {
        return new Choice(label, targetSceneId);
    }

    public static Choice create(Function<GameState, String> dynamicLabel, String targetSceneId) {
        return new Choice(dynamicLabel, targetSceneId);
    }

    // Set label text
    public Choice setLabel(String label) {
        this.label = label;
        return this;
    }

    public Choice label(String label) {
        this.label = label;
        return this;
    }

    // Set dynamic label text generator
    public Choice setDynamicText(Function<GameState, String> dynamicLabel) {
        this.dynamicLabel = dynamicLabel;
        return this;
    }

    public Choice dynamicLabel(Function<GameState, String> dynamicLabel) {
        this.dynamicLabel = dynamicLabel;
        return this;
    }

    public Choice target(String targetSceneId) {
        this.targetSceneId = targetSceneId;
        return this;
    }

    public Choice targetSceneId(String targetSceneId) {
        this.targetSceneId = targetSceneId;
        return this;
    }

    // Run when the choice is selected
    public Choice onSelect(Consumer<GameState> action) {
        if (this.action == null) {
            this.action = action;
        } else {
            Consumer<GameState> prev = this.action;
            this.action = state -> {
                prev.accept(state);
                action.accept(state);
            };
        }
        return this;
    }

    public void execute(GameState state) {
        if (this.action != null && state != null) {
            this.action.accept(state);
        }
    }

    public String getLabel(GameState state) {
        if (this.dynamicLabel != null && state != null) {
            return this.dynamicLabel.apply(state);
        }
        return this.label != null ? this.label : "";
    }

    // Check visibility: hiddenIf overrides displayIf
    public boolean isVisible(GameState state) {
        if (state == null) return true;
        for (Predicate<GameState> hidden : hiddenConditions) {
            if (hidden != null && hidden.test(state)) {
                return false;
            }
        }
        for (Predicate<GameState> display : displayConditions) {
            if (display != null && !display.test(state)) {
                return false;
            }
        }
        return true;
    }

    // Hides choice if condition is met. Overrides displayIf
    public Choice hiddenIf(Predicate<GameState> condition) {
        this.hiddenConditions.add(condition);
        return this;
    }

    public Choice hiddenIfFlag(String flag) {
        return hiddenIf(state -> state.getFlag(flag));
    }

    public Choice hiddenIfFlagFalse(String flag) {
        return hiddenIf(state -> !state.getFlag(flag));
    }

    public Choice hiddenIfVarEquals(String varName, Object value) {
        return hiddenIf(state -> state.isVarEqual(varName, value));
    }

    public Choice hiddenIfVarEquals(String varName, String value) {
        return hiddenIfVarEquals(varName, (Object) value);
    }

    public Choice hiddenIfVarEquals(String varName, int value) {
        return hiddenIfVarEquals(varName, Integer.valueOf(value));
    }

    public Choice hiddenIfVarEquals(String varName, boolean value) {
        return hiddenIfVarEquals(varName, Boolean.valueOf(value));
    }

    public Choice hiddenIfVarEquals(String varName, double value) {
        return hiddenIfVarEquals(varName, Double.valueOf(value));
    }

    public Choice hiddenIfVarEquals(String varName, long value) {
        return hiddenIfVarEquals(varName, Long.valueOf(value));
    }

    public Choice hiddenIfVarNotEqual(String varName, Object value) {
        return hiddenIf(state -> !state.isVarEqual(varName, value));
    }

    public Choice hiddenIfVarNotEqual(String varName, String value) {
        return hiddenIfVarNotEqual(varName, (Object) value);
    }

    public Choice hiddenIfVarNotEqual(String varName, int value) {
        return hiddenIfVarNotEqual(varName, Integer.valueOf(value));
    }

    public Choice hiddenIfVarNotEqual(String varName, boolean value) {
        return hiddenIfVarNotEqual(varName, Boolean.valueOf(value));
    }

    public Choice hiddenIfVarNotEqual(String varName, double value) {
        return hiddenIfVarNotEqual(varName, Double.valueOf(value));
    }

    public Choice hiddenIfVarNotEqual(String varName, long value) {
        return hiddenIfVarNotEqual(varName, Long.valueOf(value));
    }

    public Choice hiddenIfHasItem(String itemClassPointer) {
        return hiddenIfHasItem(itemClassPointer, 1);
    }

    public Choice hiddenIfHasItem(String itemClassPointer, int quantity) {
        return hiddenIf(state -> state.hasItem(itemClassPointer, quantity));
    }

    public Choice hiddenIfDoesntHaveItem(String itemClassPointer) {
        return hiddenIfDoesntHaveItem(itemClassPointer, 1);
    }

    public Choice hiddenIfDoesntHaveItem(String itemClassPointer, int quantity) {
        return hiddenIf(state -> !state.hasItem(itemClassPointer, quantity));
    }

    // Only display if condition is met
    public Choice displayIf(Predicate<GameState> condition) {
        this.displayConditions.add(condition);
        return this;
    }

    public Choice displayIfFlag(String flag) {
        return displayIf(state -> state.getFlag(flag));
    }

    public Choice displayIfFlagFalse(String flag) {
        return displayIf(state -> !state.getFlag(flag));
    }

    public Choice displayIfVarEquals(String varName, Object value) {
        return displayIf(state -> state.isVarEqual(varName, value));
    }

    public Choice displayIfVarEquals(String varName, String value) {
        return displayIfVarEquals(varName, (Object) value);
    }

    public Choice displayIfVarEquals(String varName, int value) {
        return displayIfVarEquals(varName, Integer.valueOf(value));
    }

    public Choice displayIfVarEquals(String varName, boolean value) {
        return displayIfVarEquals(varName, Boolean.valueOf(value));
    }

    public Choice displayIfVarEquals(String varName, double value) {
        return displayIfVarEquals(varName, Double.valueOf(value));
    }

    public Choice displayIfVarEquals(String varName, long value) {
        return displayIfVarEquals(varName, Long.valueOf(value));
    }

    public Choice displayIfVarNotEqual(String varName, Object value) {
        return displayIf(state -> !state.isVarEqual(varName, value));
    }

    public Choice displayIfVarNotEqual(String varName, String value) {
        return displayIfVarNotEqual(varName, (Object) value);
    }

    public Choice displayIfVarNotEqual(String varName, int value) {
        return displayIfVarNotEqual(varName, Integer.valueOf(value));
    }

    public Choice displayIfVarNotEqual(String varName, boolean value) {
        return displayIfVarNotEqual(varName, Boolean.valueOf(value));
    }

    public Choice displayIfVarNotEqual(String varName, double value) {
        return displayIfVarNotEqual(varName, Double.valueOf(value));
    }

    public Choice displayIfVarNotEqual(String varName, long value) {
        return displayIfVarNotEqual(varName, Long.valueOf(value));
    }

    public Choice displayIfHasItem(String itemClassPointer) {
        return displayIfHasItem(itemClassPointer, 1);
    }

    public Choice displayIfHasItem(String itemClassPointer, int quantity) {
        return displayIf(state -> state.hasItem(itemClassPointer, quantity));
    }

    public Choice displayIfDoesntHaveItem(String itemClassPointer) {
        return displayIfDoesntHaveItem(itemClassPointer, 1);
    }

    public Choice displayIfDoesntHaveItem(String itemClassPointer, int quantity) {
        return displayIf(state -> !state.hasItem(itemClassPointer, quantity));
    }

    // Sets a selected variable to a selected value
    public Choice setVar(String key, Object value) {
        return onSelect(state -> state.setVar(key, value));
    }

    public Choice setVar(String key, String value) {
        return setVar(key, (Object) value);
    }

    public Choice setVar(String key, int value) {
        return setVar(key, Integer.valueOf(value));
    }

    public Choice setVar(String key, boolean value) {
        return setVar(key, Boolean.valueOf(value));
    }

    public Choice setVar(String key, double value) {
        return setVar(key, Double.valueOf(value));
    }

    public Choice setVar(String key, long value) {
        return setVar(key, Long.valueOf(value));
    }

    public Choice setFlag(String key, boolean value) {
        return onSelect(state -> state.setFlag(key, value));
    }

    public Choice setCounter(String key, int value) {
        return onSelect(state -> state.setCounter(key, value));
    }

    public Choice incrementCounter(String key) {
        return onSelect(state -> state.incrementCounter(key));
    }

    public Choice setString(String key, String value) {
        return onSelect(state -> state.setString(key, value));
    }

    public Choice setStat(String statId, int value) {
        return onSelect(state -> state.setStatValue(statId, value));
    }

    public Choice modifyStat(String statId, int delta) {
        return onSelect(state -> state.modifyStat(statId, delta));
    }

    public Choice advanceTime(long seconds) {
        return onSelect(state -> state.advanceTime(seconds));
    }

    // Give item
    public Choice giveItem(String itemClassPointer) {
        return giveItem(itemClassPointer, 1);
    }

    public Choice giveItem(String itemClassPointer, int quantity) {
        return onSelect(state -> state.giveItem(itemClassPointer, quantity));
    }

    public Choice givesItem(String itemClassPointer) {
        return giveItem(itemClassPointer, 1);
    }

    public Choice givesItem(String itemClassPointer, int quantity) {
        return giveItem(itemClassPointer, quantity);
    }

    public Choice giveItem(Item item) {
        return onSelect(state -> state.getInventory().addItem(item));
    }

    public Choice givesItem(Item item) {
        return giveItem(item);
    }

    // Requires item (only displays if item is in inventory)
    public Choice requiresItem(String itemClassPointer) {
        return requiresItem(itemClassPointer, 1);
    }

    public Choice requiresItem(String itemClassPointer, int quantity) {
        return displayIf(state -> state.hasItem(itemClassPointer, quantity));
    }

    // Consumes item (should also require it)
    public Choice consumesItem(String itemClassPointer) {
        return consumesItem(itemClassPointer, 1);
    }

    public Choice consumesItem(String itemClassPointer, int quantity) {
        requiresItem(itemClassPointer, quantity);
        return onSelect(state -> state.removeItem(itemClassPointer, quantity));
    }
}
