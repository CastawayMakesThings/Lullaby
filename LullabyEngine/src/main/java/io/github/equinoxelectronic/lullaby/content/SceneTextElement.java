package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

import java.util.function.Function;

public class SceneTextElement implements SceneElement {
    private final Function<GameState, String> textGenerator;

    public SceneTextElement(Function<GameState, String> textGenerator) {
        this.textGenerator = textGenerator != null ? textGenerator : s -> "";
    }

    public Function<GameState, String> getTextGenerator() {
        return textGenerator;
    }

    public String getText(GameState state) {
        return textGenerator.apply(state);
    }
}
