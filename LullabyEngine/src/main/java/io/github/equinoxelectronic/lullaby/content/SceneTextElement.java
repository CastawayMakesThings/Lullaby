package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

import java.util.function.Function;
import java.util.function.Predicate;

public class SceneTextElement implements SceneElement {
    private final Function<GameState, String> textGenerator;
    private final Predicate<GameState> condition;

    public SceneTextElement(Function<GameState, String> textGenerator) {
        this(null, textGenerator);
    }

    public SceneTextElement(Predicate<GameState> condition, Function<GameState, String> textGenerator) {
        this.condition = condition;
        this.textGenerator = textGenerator != null ? textGenerator : s -> "";
    }

    public Function<GameState, String> getTextGenerator() {
        return textGenerator;
    }

    public Predicate<GameState> getCondition() {
        return condition;
    }

    @Override
    public boolean isVisible(GameState state) {
        if (condition != null && !condition.test(state)) {
            return false;
        }
        return true;
    }

    public String getText(GameState state) {
        return textGenerator.apply(state);
    }
}
