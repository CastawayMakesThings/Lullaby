package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
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
    public List<SceneElement> elements = new ArrayList<>();
    public boolean isEnding = false;

    public Scene(String id) {
        this.id = id;
    }

    public static Scene make(String id) {
        return new Scene(id);
    }

    public static Scene create(String id) {
        return new Scene(id);
    }

    public static Scene of(String id) {
        return new Scene(id);
    }

    //Set the title
    public Scene title(String title) {
        this.titleText = title;
        return this;
    }

    //Add a basic text block
    public Scene text(String text) {
        Function<GameState, String> gen = state -> text;
        this.textGenerators.add(gen);
        this.elements.add(new SceneTextElement(gen));
        return this;
    }

    //Add a fancy text block
    public Scene text(Function<GameState, String> textGenerator) {
        this.textGenerators.add(textGenerator);
        this.elements.add(new SceneTextElement(textGenerator));
        return this;
    }

    //Add a choice
    public Scene choice(Choice choice) {
        this.choices.add(choice);
        this.elements.add(new SceneChoiceElement(choice));
        return this;
    }

    public Scene choice(String label, String targetSceneId) {
        return choice(new Choice(label, targetSceneId));
    }

    public Scene choice(String label, String targetSceneId, Consumer<GameState> action) {
        Choice c = new Choice(label, targetSceneId);
        c.onSelect(action);
        return choice(c);
    }

    public Scene choice(Function<GameState, String> dynamicLabel, String targetSceneId) {
        return choice(new Choice(dynamicLabel, targetSceneId));
    }

    //Add a field
    public Scene field(Field field) {
        this.fields.add(field);
        this.elements.add(new SceneFieldElement(field));
        return this;
    }

    public Scene field(String placeholder, String targetSceneId) {
        return field(new Field(placeholder, targetSceneId));
    }

    public Scene field(String placeholder, String targetSceneId, String boundVariableKey) {
        return field(new Field(placeholder, targetSceneId, boundVariableKey));
    }

    //Add an image block
    public Scene image(Object imageSource) {
        this.elements.add(new SceneImageElement(imageSource));
        return this;
    }

    public Scene image(String path) {
        return image((Object) path);
    }

    public Scene image(File file) {
        return image((Object) file);
    }

    public Scene image(Image image) {
        return image((Object) image);
    }

    public Scene image(ImageIcon icon) {
        return image((Object) icon);
    }

    public Scene image(URL url) {
        return image((Object) url);
    }

    public Scene image(Function<GameState, Object> dynamicImageSource) {
        this.elements.add(new SceneImageElement(dynamicImageSource));
        return this;
    }

    //Add a conditional image
    public Scene imageIf(Predicate<GameState> condition, Object imageSource) {
        this.elements.add(new SceneImageElement(condition, imageSource));
        return this;
    }

    public Scene imageIf(Predicate<GameState> condition, String path) {
        return imageIf(condition, (Object) path);
    }

    public Scene imageIf(Predicate<GameState> condition, File file) {
        return imageIf(condition, (Object) file);
    }

    public Scene imageIf(Predicate<GameState> condition, Image image) {
        return imageIf(condition, (Object) image);
    }

    public Scene imageIf(Predicate<GameState> condition, ImageIcon icon) {
        return imageIf(condition, (Object) icon);
    }

    public Scene imageIf(Predicate<GameState> condition, URL url) {
        return imageIf(condition, (Object) url);
    }

    public Scene imageIf(Predicate<GameState> condition, Function<GameState, Object> dynamicImageSource) {
        this.elements.add(new SceneImageElement(condition, dynamicImageSource));
        return this;
    }

    public Scene imageIfFlag(String flag, Object imageSource) {
        return imageIf(state -> state.getFlag(flag), imageSource);
    }

    public Scene imageIfFlag(String flag, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> state.getFlag(flag), dynamicImageSource);
    }

    public Scene imageIfFlagFalse(String flag, Object imageSource) {
        return imageIf(state -> !state.getFlag(flag), imageSource);
    }

    public Scene imageIfFlagFalse(String flag, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> !state.getFlag(flag), dynamicImageSource);
    }

    public Scene imageIfVarEquals(String varName, Object value, Object imageSource) {
        return imageIf(state -> state.isVarEqual(varName, value), imageSource);
    }

    public Scene imageIfVarEquals(String varName, Object value, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> state.isVarEqual(varName, value), dynamicImageSource);
    }

    public Scene imageIfVarEquals(String varName, String value, Object imageSource) {
        return imageIfVarEquals(varName, (Object) value, imageSource);
    }

    public Scene imageIfVarEquals(String varName, int value, Object imageSource) {
        return imageIfVarEquals(varName, Integer.valueOf(value), imageSource);
    }

    public Scene imageIfVarEquals(String varName, boolean value, Object imageSource) {
        return imageIfVarEquals(varName, Boolean.valueOf(value), imageSource);
    }

    public Scene imageIfVarEquals(String varName, double value, Object imageSource) {
        return imageIfVarEquals(varName, Double.valueOf(value), imageSource);
    }

    public Scene imageIfVarEquals(String varName, long value, Object imageSource) {
        return imageIfVarEquals(varName, Long.valueOf(value), imageSource);
    }

    public Scene imageIfVarNotEqual(String varName, Object value, Object imageSource) {
        return imageIf(state -> !state.isVarEqual(varName, value), imageSource);
    }

    public Scene imageIfVarNotEqual(String varName, Object value, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> !state.isVarEqual(varName, value), dynamicImageSource);
    }

    public Scene imageIfVarNotEqual(String varName, String value, Object imageSource) {
        return imageIfVarNotEqual(varName, (Object) value, imageSource);
    }

    public Scene imageIfVarNotEqual(String varName, int value, Object imageSource) {
        return imageIfVarNotEqual(varName, Integer.valueOf(value), imageSource);
    }

    public Scene imageIfVarNotEqual(String varName, boolean value, Object imageSource) {
        return imageIfVarNotEqual(varName, Boolean.valueOf(value), imageSource);
    }

    public Scene imageIfVarNotEqual(String varName, double value, Object imageSource) {
        return imageIfVarNotEqual(varName, Double.valueOf(value), imageSource);
    }

    public Scene imageIfVarNotEqual(String varName, long value, Object imageSource) {
        return imageIfVarNotEqual(varName, Long.valueOf(value), imageSource);
    }

    public Scene imageIfFirst(Object imageSource) {
        return imageIf(state -> state.getSceneVisitCount(this.id) <= 1, imageSource);
    }

    public Scene imageIfFirst(Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> state.getSceneVisitCount(this.id) <= 1, dynamicImageSource);
    }

    public Scene imageIfIteration(int iteration, Object imageSource) {
        return imageIf(state -> state.getSceneVisitCount(this.id) == iteration, imageSource);
    }

    public Scene imageIfIteration(int iteration, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> state.getSceneVisitCount(this.id) == iteration, dynamicImageSource);
    }

    public Scene imageIfIteration(Predicate<Integer> iterationPredicate, Object imageSource) {
        return imageIf(state -> iterationPredicate.test(state.getSceneVisitCount(this.id)), imageSource);
    }

    public Scene imageIfIteration(Predicate<Integer> iterationPredicate, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> iterationPredicate.test(state.getSceneVisitCount(this.id)), dynamicImageSource);
    }

    public Scene imageIfHasItem(String itemClassPointer, Object imageSource) {
        return imageIfHasItem(itemClassPointer, 1, imageSource);
    }

    public Scene imageIfHasItem(String itemClassPointer, int quantity, Object imageSource) {
        return imageIf(state -> state.hasItem(itemClassPointer, quantity), imageSource);
    }

    public Scene imageIfHasItem(String itemClassPointer, Function<GameState, Object> dynamicImageSource) {
        return imageIfHasItem(itemClassPointer, 1, dynamicImageSource);
    }

    public Scene imageIfHasItem(String itemClassPointer, int quantity, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> state.hasItem(itemClassPointer, quantity), dynamicImageSource);
    }

    public Scene imageIfDoesntHaveItem(String itemClassPointer, Object imageSource) {
        return imageIfDoesntHaveItem(itemClassPointer, 1, imageSource);
    }

    public Scene imageIfDoesntHaveItem(String itemClassPointer, int quantity, Object imageSource) {
        return imageIf(state -> !state.hasItem(itemClassPointer, quantity), imageSource);
    }

    public Scene imageIfDoesntHaveItem(String itemClassPointer, Function<GameState, Object> dynamicImageSource) {
        return imageIfDoesntHaveItem(itemClassPointer, 1, dynamicImageSource);
    }

    public Scene imageIfDoesntHaveItem(String itemClassPointer, int quantity, Function<GameState, Object> dynamicImageSource) {
        return imageIf(state -> !state.hasItem(itemClassPointer, quantity), dynamicImageSource);
    }

    //Add a text block that only appears if a condition is met
    public Scene textIf(Predicate<GameState> condition, String text) {
        Function<GameState, String> gen = state -> condition.test(state) ? text : "";
        this.textGenerators.add(gen);
        this.elements.add(new SceneTextElement(gen));
        return this;
    }

    //Add a text block that only appears if a condition is met that allows for dynamic text generation
    public Scene textIf(Predicate<GameState> condition, Function<GameState, String> textGenerator) {
        Function<GameState, String> gen = state -> condition.test(state) ? textGenerator.apply(state) : "";
        this.textGenerators.add(gen);
        this.elements.add(new SceneTextElement(gen));
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
