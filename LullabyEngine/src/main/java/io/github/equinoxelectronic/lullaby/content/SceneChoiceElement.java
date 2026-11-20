package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

public class SceneChoiceElement implements SceneElement {
    private final Choice choice;

    public SceneChoiceElement(Choice choice) {
        this.choice = choice;
    }

    public Choice getChoice() {
        return choice;
    }

    @Override
    public boolean isVisible(GameState state) {
        return choice != null && choice.isVisible(state);
    }
}
