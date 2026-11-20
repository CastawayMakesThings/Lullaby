package io.github.equinoxelectronic.lullaby.content;

import java.util.LinkedHashMap;
import java.util.Map;

public class Story {
    public Map<String, Scene> scenes = new LinkedHashMap<>();

    public Scene scene(String id) {
        return scenes.computeIfAbsent(id, Scene::new);
    }

    public Scene getScene(String id) {
        return scenes.get(id);
    }

    public void addScene(Scene scene) {
        scenes.put(scene.id, scene);
    }
}
