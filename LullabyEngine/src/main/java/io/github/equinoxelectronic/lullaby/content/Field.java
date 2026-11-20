package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Field {
    public String placeholder;
    public String targetSceneId;
    public String boundVariableKey;
    public Consumer<GameState> onEnteredAction;
    public List<BiConsumer<GameState, String>> inputActions = new ArrayList<>();
    public List<Predicate<GameState>> displayConditions = new ArrayList<>();
    public List<Predicate<GameState>> hiddenConditions = new ArrayList<>();
    public List<SceneOverride> sceneOverrides = new ArrayList<>();

    public static class SceneOverride {
        public BiPredicate<GameState, String> condition;
        public String targetSceneId;

        public SceneOverride(BiPredicate<GameState, String> condition, String targetSceneId) {
            this.condition = condition;
            this.targetSceneId = targetSceneId;
        }

        public boolean test(GameState state, String input) {
            return condition != null && condition.test(state, input);
        }
    }

    public Field() {
    }

    public Field(String placeholder) {
        this.placeholder = placeholder;
    }

    public Field(String placeholder, String targetSceneId) {
        this.placeholder = placeholder;
        this.targetSceneId = targetSceneId;
    }

    public Field(String placeholder, String targetSceneId, String boundVariableKey) {
        this.placeholder = placeholder;
        this.targetSceneId = targetSceneId;
        this.boundVariableKey = boundVariableKey;
    }

    public static Field make(String placeholder, String targetSceneId) {
        return new Field(placeholder, targetSceneId);
    }

    public static Field make(String placeholder, String targetSceneId, String boundVariableKey) {
        return new Field(placeholder, targetSceneId, boundVariableKey);
    }

    public Field placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public Field targetScene(String targetSceneId) {
        this.targetSceneId = targetSceneId;
        return this;
    }

    // Run action when submitted
    public Field onEntered(Consumer<GameState> onEnteredAction) {
        this.onEnteredAction = onEnteredAction;
        return this;
    }

    public Field onEntered(BiConsumer<GameState, String> inputAction) {
        this.inputActions.add(inputAction);
        return this;
    }

    // Bind input directly to variable
    public Field putAsVar(String varKey) {
        this.boundVariableKey = varKey;
        this.inputActions.add((state, input) -> state.setVar(varKey, input));
        return this;
    }

    public Field putAsString(String varKey) {
        this.boundVariableKey = varKey;
        this.inputActions.add((state, input) -> state.setString(varKey, input));
        return this;
    }

    public Field putAsInt(String counterKey) {
        this.inputActions.add((state, input) -> {
            try {
                int val = Integer.parseInt(input.trim());
                state.setCounter(counterKey, val);
            } catch (NumberFormatException ignored) {
            }
        });
        return this;
    }

    public Field putAsBoolean(String flagKey) {
        this.inputActions.add((state, input) -> {
            String trimmed = input.trim();
            boolean val = Boolean.parseBoolean(trimmed) || "yes".equalsIgnoreCase(trimmed) || "1".equals(trimmed);
            state.setFlag(flagKey, val);
        });
        return this;
    }

    public Field putAsStat(String statKey) {
        this.inputActions.add((state, input) -> {
            try {
                int val = Integer.parseInt(input.trim());
                state.setStatValue(statKey, val);
            } catch (NumberFormatException ignored) {
            }
        });
        return this;
    }

    public Field putAsVar(String varKey, Function<String, Object> parser) {
        this.inputActions.add((state, input) -> {
            try {
                Object val = parser.apply(input);
                state.setVar(varKey, val);
            } catch (Exception ignored) {
            }
        });
        return this;
    }

    // Scene overrides
    public Field sceneOverideIf(Predicate<GameState> condition, String overrideSceneId) {
        this.sceneOverrides.add(new SceneOverride((state, input) -> condition.test(state), overrideSceneId));
        return this;
    }

    public Field sceneOverrideIf(Predicate<GameState> condition, String overrideSceneId) {
        return sceneOverideIf(condition, overrideSceneId);
    }

    public Field sceneOverideIf(BiPredicate<GameState, String> condition, String overrideSceneId) {
        this.sceneOverrides.add(new SceneOverride(condition, overrideSceneId));
        return this;
    }

    public Field sceneOverrideIf(BiPredicate<GameState, String> condition, String overrideSceneId) {
        return sceneOverideIf(condition, overrideSceneId);
    }

    public Field sceneOverideIfFlag(String flag, String overrideSceneId) {
        return sceneOverideIf(state -> state.getFlag(flag), overrideSceneId);
    }

    public Field sceneOverrideIfFlag(String flag, String overrideSceneId) {
        return sceneOverideIfFlag(flag, overrideSceneId);
    }

    public Field sceneOverideIfFlagFalse(String flag, String overrideSceneId) {
        return sceneOverideIf(state -> !state.getFlag(flag), overrideSceneId);
    }

    public Field sceneOverrideIfFlagFalse(String flag, String overrideSceneId) {
        return sceneOverideIfFlagFalse(flag, overrideSceneId);
    }

    public Field sceneOverideIfVarEquals(String varName, Object value, String overrideSceneId) {
        return sceneOverideIf(state -> state.isVarEqual(varName, value), overrideSceneId);
    }

    public Field sceneOverrideIfVarEquals(String varName, Object value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, value, overrideSceneId);
    }

    public Field sceneOverideIfVarEquals(String varName, String value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, (Object) value, overrideSceneId);
    }

    public Field sceneOverrideIfVarEquals(String varName, String value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, (Object) value, overrideSceneId);
    }

    public Field sceneOverideIfVarEquals(String varName, int value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Integer.valueOf(value), overrideSceneId);
    }

    public Field sceneOverrideIfVarEquals(String varName, int value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Integer.valueOf(value), overrideSceneId);
    }

    public Field sceneOverideIfVarEquals(String varName, boolean value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Boolean.valueOf(value), overrideSceneId);
    }

    public Field sceneOverrideIfVarEquals(String varName, boolean value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Boolean.valueOf(value), overrideSceneId);
    }

    public Field sceneOverideIfVarEquals(String varName, double value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Double.valueOf(value), overrideSceneId);
    }

    public Field sceneOverrideIfVarEquals(String varName, double value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Double.valueOf(value), overrideSceneId);
    }

    public Field sceneOverideIfVarEquals(String varName, long value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Long.valueOf(value), overrideSceneId);
    }

    public Field sceneOverrideIfVarEquals(String varName, long value, String overrideSceneId) {
        return sceneOverideIfVarEquals(varName, Long.valueOf(value), overrideSceneId);
    }

    public Field sceneOverideIfVarNotEqual(String varName, Object value, String overrideSceneId) {
        return sceneOverideIf(state -> !state.isVarEqual(varName, value), overrideSceneId);
    }

    public Field sceneOverrideIfVarNotEqual(String varName, Object value, String overrideSceneId) {
        return sceneOverideIfVarNotEqual(varName, value, overrideSceneId);
    }

    public Field sceneOverideIfInputEquals(String expectedInput, String overrideSceneId) {
        return sceneOverideIf((state, input) -> Objects.equals(input, expectedInput), overrideSceneId);
    }

    public Field sceneOverrideIfInputEquals(String expectedInput, String overrideSceneId) {
        return sceneOverideIfInputEquals(expectedInput, overrideSceneId);
    }

    public Field sceneOverideIfInputMatches(Predicate<String> inputMatcher, String overrideSceneId) {
        return sceneOverideIf((state, input) -> inputMatcher != null && inputMatcher.test(input), overrideSceneId);
    }

    public Field sceneOverrideIfInputMatches(Predicate<String> inputMatcher, String overrideSceneId) {
        return sceneOverideIfInputMatches(inputMatcher, overrideSceneId);
    }

    public Field sceneOverideIfHasItem(String itemClassPointer, String overrideSceneId) {
        return sceneOverideIf(state -> state.hasItem(itemClassPointer), overrideSceneId);
    }

    public Field sceneOverrideIfHasItem(String itemClassPointer, String overrideSceneId) {
        return sceneOverideIfHasItem(itemClassPointer, overrideSceneId);
    }

    public Field sceneOverideIfDoesntHaveItem(String itemClassPointer, String overrideSceneId) {
        return sceneOverideIf(state -> !state.hasItem(itemClassPointer), overrideSceneId);
    }

    public Field sceneOverrideIfDoesntHaveItem(String itemClassPointer, String overrideSceneId) {
        return sceneOverideIfDoesntHaveItem(itemClassPointer, overrideSceneId);
    }

    // Visibility
    public Field hiddenIf(Predicate<GameState> condition) {
        this.hiddenConditions.add(condition);
        return this;
    }

    public Field displayIf(Predicate<GameState> condition) {
        this.displayConditions.add(condition);
        return this;
    }

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

    public void submit(GameState state, String input) {
        if (state == null) return;
        if (boundVariableKey != null) {
            state.getVars().put(boundVariableKey, input);
            state.getStrings().put(boundVariableKey, input);
        }
        for (BiConsumer<GameState, String> action : inputActions) {
            if (action != null) {
                action.accept(state, input);
            }
        }
        if (onEnteredAction != null) {
            onEnteredAction.accept(state);
        }
    }

    public String resolveTargetSceneId(GameState state, String input) {
        if (state != null) {
            for (SceneOverride override : sceneOverrides) {
                if (override != null && override.test(state, input)) {
                    return override.targetSceneId;
                }
            }
        }
        return targetSceneId;
    }
}
