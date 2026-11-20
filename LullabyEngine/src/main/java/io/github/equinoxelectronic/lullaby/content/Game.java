package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameData;

public class Game {
    public GameData gameData = new GameData();
    public Story story = new Story();

    public Scene scene(String id) {
        return story.scene(id);
    }
}
