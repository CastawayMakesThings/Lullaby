package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

public class SceneFieldElement implements SceneElement {
    private final Field field;

    public SceneFieldElement(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    @Override
    public boolean isVisible(GameState state) {
        return field != null && field.isVisible(state);
    }
}
