package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

import java.io.File;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Scoped configuration context for conditionally adding elements (text, image, choice, field) to a Scene.
 */
public class SceneConditionScope {
    private final Scene scene;
    private final Predicate<GameState> condition;

    public SceneConditionScope(Scene scene, Predicate<GameState> condition) {
        this.scene = scene;
        this.condition = condition != null ? condition : state -> true;
    }

    public Scene getScene() {
        return scene;
    }

    public Predicate<GameState> getCondition() {
        return condition;
    }

    // --- Text Elements ---

    public SceneConditionScope text(String text) {
        scene.textIf(condition, text);
        return this;
    }

    public SceneConditionScope text(Function<GameState, String> textGenerator) {
        scene.textIf(condition, textGenerator);
        return this;
    }

    // --- Image Elements ---

    public SceneConditionScope image(Object imageSource) {
        scene.imageIf(condition, imageSource);
        return this;
    }

    public SceneConditionScope image(String path) {
        return image((Object) path);
    }

    public SceneConditionScope image(File file) {
        return image((Object) file);
    }

    public SceneConditionScope image(URL url) {
        return image((Object) url);
    }

    public SceneConditionScope image(Function<GameState, Object> dynamicImageSource) {
        scene.imageIf(condition, dynamicImageSource);
        return this;
    }

    // --- Choice Elements ---

    public SceneConditionScope choice(Choice choice) {
        if (choice != null) {
            choice.displayIf(condition);
            scene.choice(choice);
        }
        return this;
    }

    public SceneConditionScope choice(String label, String targetSceneId) {
        return choice(new Choice(label, targetSceneId));
    }

    public SceneConditionScope choice(String label, String targetSceneId, Consumer<GameState> action) {
        Choice c = new Choice(label, targetSceneId);
        c.onSelect(action);
        return choice(c);
    }

    public SceneConditionScope choice(Function<GameState, String> dynamicLabel, String targetSceneId) {
        return choice(new Choice(dynamicLabel, targetSceneId));
    }

    // --- Field Elements ---

    public SceneConditionScope field(Field field) {
        if (field != null) {
            field.displayIf(condition);
            scene.field(field);
        }
        return this;
    }

    public SceneConditionScope field(String placeholder, String targetSceneId) {
        return field(new Field(placeholder, targetSceneId));
    }

    public SceneConditionScope field(String placeholder, String targetSceneId, String boundVariableKey) {
        return field(new Field(placeholder, targetSceneId, boundVariableKey));
    }

    // --- Nested Condition Scope Support ---

    public SceneConditionScope when(Predicate<GameState> subCondition, Consumer<SceneConditionScope> configurer) {
        if (subCondition != null && configurer != null) {
            Predicate<GameState> combined = state -> this.condition.test(state) && subCondition.test(state);
            configurer.accept(new SceneConditionScope(this.scene, combined));
        }
        return this;
    }

    public SceneConditionScope whenFlag(String flag, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && state.getFlag(flag), configurer);
    }

    public SceneConditionScope whenFlagFalse(String flag, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && !state.getFlag(flag), configurer);
    }

    public SceneConditionScope whenVarEquals(String varName, Object value, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && state.isVarEqual(varName, value), configurer);
    }

    public SceneConditionScope whenVarNotEqual(String varName, Object value, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && !state.isVarEqual(varName, value), configurer);
    }

    public SceneConditionScope whenHasItem(String itemClassPointer, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && state.hasItem(itemClassPointer), configurer);
    }

    public SceneConditionScope whenHasItem(String itemClassPointer, int quantity, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && state.hasItem(itemClassPointer, quantity), configurer);
    }

    public SceneConditionScope whenDoesntHaveItem(String itemClassPointer, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && !state.hasItem(itemClassPointer), configurer);
    }

    public SceneConditionScope whenDoesntHaveItem(String itemClassPointer, int quantity, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && !state.hasItem(itemClassPointer, quantity), configurer);
    }

    public SceneConditionScope whenStatGreaterThan(String statId, int threshold, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && state.getStatValue(statId) > threshold, configurer);
    }

    public SceneConditionScope whenStatLessThan(String statId, int threshold, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && state.getStatValue(statId) < threshold, configurer);
    }

    public SceneConditionScope whenStatEquals(String statId, int value, Consumer<SceneConditionScope> configurer) {
        return when(state -> state != null && state.getStatValue(statId) == value, configurer);
    }
}
