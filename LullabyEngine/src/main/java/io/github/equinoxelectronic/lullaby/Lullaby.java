package io.github.equinoxelectronic.lullaby;

import java.util.HashMap;

public class Lullaby {
    public static HashMap<String, LullabyGame> engines = new HashMap<>();

    public static LullabyGame create() {
        return new LullabyGame();
    }

    public static LullabyGame create(String name) {
        LullabyGame engine = new LullabyGame().name(name);
        engines.put(name, engine);
        return engine;
    }

    public static LullabyGame get(String name) {
        return engines.get(name);
    }

    public static LullabyGame getDefault() {
        if (!engines.isEmpty()) {
            return engines.values().iterator().next();
        }
        return create();
    }

    public static void register(LullabyGame game) {
        if (game != null && game.getName() != null) {
            engines.put(game.getName(), game);
        }
    }
}
