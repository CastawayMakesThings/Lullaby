package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Scene {
    public String id;
    public String titleText;
    public List<Function<GameState, String>> textGenerators = new ArrayList<>();
    public List<Consumer<GameState>> enterActions = new ArrayList<>();
    public List<Choice> choices = new ArrayList<>();
    public List<Field> fields = new ArrayList<>();
    public boolean isEnding = false;

    public Scene(String id) {
        this.id = id;
    }

    //Set the title
    public Scene title(String title) {
        this.titleText = title;
        return this;
    }

    //Add a basic text block
    public Scene text(String text) {
        this.textGenerators.add(state -> text);
        return this;
    }

    //Add a fancy text block
    public Scene text(Function<GameState, String> textGenerator) {
        this.textGenerators.add(textGenerator);
        return this;
    }

    //Add a choice
    public Scene choice(Choice choice) {
        this.choices.add(choice);
        return this;
    }

    public Scene choice(String label, String targetSceneId) {
        this.choices.add(new Choice(label, targetSceneId));
        return this;
    }

    public Scene choice(String label, String targetSceneId, Consumer<GameState> action) {
        Choice c = new Choice(label, targetSceneId);
        c.onSelect(action);
        this.choices.add(c);
        return this;
    }

    public Scene choice(Function<GameState, String> dynamicLabel, String targetSceneId) {
        this.choices.add(new Choice(dynamicLabel, targetSceneId));
        return this;
    }

    //Add a field
    public Scene field(Field field) {
        this.fields.add(field);
        return this;
    }

    public Scene field(String placeholder, String targetSceneId) {
        this.fields.add(new Field(placeholder, targetSceneId));
        return this;
    }

    public Scene field(String placeholder, String targetSceneId, String boundVariableKey) {
        this.fields.add(new Field(placeholder, targetSceneId, boundVariableKey));
        return this;
    }

    //Add a text block that only appears if a condition is met
    public Scene textIf(Predicate<GameState> condition, String text) {
        this.textGenerators.add(state -> condition.test(state) ? text : "");
        return this;
    }

    //Add a text block that only appears if a condition is met that allows for dynamic text generation
    public Scene textIf(Predicate<GameState> condition, Function<GameState, String> textGenerator) {
        this.textGenerators.add(state -> condition.test(state) ? textGenerator.apply(state) : "");
        return this;
    }

    // Displays text if flag matching given string is true
    public Scene textIfFlag(String flag, String text) {
        return textIf(state -> state.getFlag(flag), text);
    }

    public Scene textIfFlag(String flag, Function<GameState, String> textGenerator) {
        return textIf(state -> state.getFlag(flag), textGenerator);
    }

    // Displays text if flag is false
    public Scene textIfFlagFalse(String flag, String text) {
        return textIf(state -> !state.getFlag(flag), text);
    }

    public Scene textIfFlagFalse(String flag, Function<GameState, String> textGenerator) {
        return textIf(state -> !state.getFlag(flag), textGenerator);
    }

    // Displays text if variable equals given value (supports all variable types)
    public Scene textIfVarEquals(String varName, Object value, String text) {
        return textIf(state -> state.isVarEqual(varName, value), text);
    }

    public Scene textIfVarEquals(String varName, Object value, Function<GameState, String> textGenerator) {
        return textIf(state -> state.isVarEqual(varName, value), textGenerator);
    }

    public Scene textIfVarEquals(String varName, String value, String text) {
        return textIfVarEquals(varName, (Object) value, text);
    }

    public Scene textIfVarEquals(String varName, String value, Function<GameState, String> textGenerator) {
        return textIfVarEquals(varName, (Object) value, textGenerator);
    }

    public Scene textIfVarEquals(String varName, int value, String text) {
        return textIfVarEquals(varName, Integer.valueOf(value), text);
    }

    public Scene textIfVarEquals(String varName, int value, Function<GameState, String> textGenerator) {
        return textIfVarEquals(varName, Integer.valueOf(value), textGenerator);
    }

    public Scene textIfVarEquals(String varName, boolean value, String text) {
        return textIfVarEquals(varName, Boolean.valueOf(value), text);
    }

    public Scene textIfVarEquals(String varName, boolean value, Function<GameState, String> textGenerator) {
        return textIfVarEquals(varName, Boolean.valueOf(value), textGenerator);
    }

    public Scene textIfVarEquals(String varName, double value, String text) {
        return textIfVarEquals(varName, Double.valueOf(value), text);
    }

    public Scene textIfVarEquals(String varName, double value, Function<GameState, String> textGenerator) {
        return textIfVarEquals(varName, Double.valueOf(value), textGenerator);
    }

    public Scene textIfVarEquals(String varName, long value, String text) {
        return textIfVarEquals(varName, Long.valueOf(value), text);
    }

    public Scene textIfVarEquals(String varName, long value, Function<GameState, String> textGenerator) {
        return textIfVarEquals(varName, Long.valueOf(value), textGenerator);
    }

    // Displays text if variable does not equal given value
    public Scene textIfVarNotEqual(String varName, Object value, String text) {
        return textIf(state -> !state.isVarEqual(varName, value), text);
    }

    public Scene textIfVarNotEqual(String varName, Object value, Function<GameState, String> textGenerator) {
        return textIf(state -> !state.isVarEqual(varName, value), textGenerator);
    }

    public Scene textIfVarNotEqual(String varName, String value, String text) {
        return textIfVarNotEqual(varName, (Object) value, text);
    }

    public Scene textIfVarNotEqual(String varName, String value, Function<GameState, String> textGenerator) {
        return textIfVarNotEqual(varName, (Object) value, textGenerator);
    }

    public Scene textIfVarNotEqual(String varName, int value, String text) {
        return textIfVarNotEqual(varName, Integer.valueOf(value), text);
    }

    public Scene textIfVarNotEqual(String varName, int value, Function<GameState, String> textGenerator) {
        return textIfVarNotEqual(varName, Integer.valueOf(value), textGenerator);
    }

    public Scene textIfVarNotEqual(String varName, boolean value, String text) {
        return textIfVarNotEqual(varName, Boolean.valueOf(value), text);
    }

    public Scene textIfVarNotEqual(String varName, boolean value, Function<GameState, String> textGenerator) {
        return textIfVarNotEqual(varName, Boolean.valueOf(value), textGenerator);
    }

    public Scene textIfVarNotEqual(String varName, double value, String text) {
        return textIfVarNotEqual(varName, Double.valueOf(value), text);
    }

    public Scene textIfVarNotEqual(String varName, double value, Function<GameState, String> textGenerator) {
        return textIfVarNotEqual(varName, Double.valueOf(value), textGenerator);
    }

    public Scene textIfVarNotEqual(String varName, long value, String text) {
        return textIfVarNotEqual(varName, Long.valueOf(value), text);
    }

    public Scene textIfVarNotEqual(String varName, long value, Function<GameState, String> textGenerator) {
        return textIfVarNotEqual(varName, Long.valueOf(value), textGenerator);
    }

    // Displays text if it is the first time this scene has been shown
    public Scene textIfFirst(String text) {
        return textIf(state -> state.getSceneVisitCount(this.id) <= 1, text);
    }

    public Scene textIfFirst(Function<GameState, String> textGenerator) {
        return textIf(state -> state.getSceneVisitCount(this.id) <= 1, textGenerator);
    }

    // Makes it dependent on how many times the scene has been ran
    public Scene textIfIteration(int iteration, String text) {
        return textIf(state -> state.getSceneVisitCount(this.id) == iteration, text);
    }

    public Scene textIfIteration(int iteration, Function<GameState, String> textGenerator) {
        return textIf(state -> state.getSceneVisitCount(this.id) == iteration, textGenerator);
    }

    public Scene textIfIteration(Predicate<Integer> iterationPredicate, String text) {
        return textIf(state -> iterationPredicate.test(state.getSceneVisitCount(this.id)), text);
    }

    public Scene textIfIteration(Predicate<Integer> iterationPredicate, Function<GameState, String> textGenerator) {
        return textIf(state -> iterationPredicate.test(state.getSceneVisitCount(this.id)), textGenerator);
    }

    // Displays text if player has item
    public Scene textIfHasItem(String itemClassPointer, String text) {
        return textIfHasItem(itemClassPointer, 1, text);
    }

    public Scene textIfHasItem(String itemClassPointer, int quantity, String text) {
        return textIf(state -> state.hasItem(itemClassPointer, quantity), text);
    }

    public Scene textIfHasItem(String itemClassPointer, Function<GameState, String> textGenerator) {
        return textIfHasItem(itemClassPointer, 1, textGenerator);
    }

    public Scene textIfHasItem(String itemClassPointer, int quantity, Function<GameState, String> textGenerator) {
        return textIf(state -> state.hasItem(itemClassPointer, quantity), textGenerator);
    }

    // Displays text if player doesn't have item
    public Scene textIfDoesntHaveItem(String itemClassPointer, String text) {
        return textIfDoesntHaveItem(itemClassPointer, 1, text);
    }

    public Scene textIfDoesntHaveItem(String itemClassPointer, int quantity, String text) {
        return textIf(state -> !state.hasItem(itemClassPointer, quantity), text);
    }

    public Scene textIfDoesntHaveItem(String itemClassPointer, Function<GameState, String> textGenerator) {
        return textIfDoesntHaveItem(itemClassPointer, 1, textGenerator);
    }

    public Scene textIfDoesntHaveItem(String itemClassPointer, int quantity, Function<GameState, String> textGenerator) {
        return textIf(state -> !state.hasItem(itemClassPointer, quantity), textGenerator);
    }

    // Mark scene as ending
    public Scene isEnding(boolean isEnding) {
        this.isEnding = isEnding;
        return this;
    }

    public Scene ending() {
        return isEnding(true);
    }

    //Run some code when the scene is entered
    public Scene onEnter(Consumer<GameState> onEnter) {
        this.enterActions.add(onEnter);
        return this;
    }
}
