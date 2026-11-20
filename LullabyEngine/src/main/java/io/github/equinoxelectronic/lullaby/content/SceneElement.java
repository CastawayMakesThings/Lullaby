package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;

public interface SceneElement {
    default boolean isVisible(GameState state) {
        return true;
    }
}
