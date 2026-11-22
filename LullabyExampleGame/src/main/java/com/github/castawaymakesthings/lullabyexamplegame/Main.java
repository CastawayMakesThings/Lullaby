package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;

/**
 * Main entry point for the Lullaby Example Game: "Whispers of the Clockwork Manor".
 * Demonstrates a feature-rich, modular narrative game powered by the Lullaby engine.
 */
public class Main {

    public static LullabyGame createGame() {
        LullabyGame game = new LullabyGame();

        // 1. Setup metadata, window, stats, items, and initial time
        GameSetup.configure(game);

        // 2. Register modular scenes
        IntroScenes.register(game);
        CourtyardScenes.register(game);
        LibraryScenes.register(game);
        AlchemyLabScenes.register(game);
        ClockTowerScenes.register(game);
        EndingScenes.register(game);

        return game;
    }

    public static void main(String[] args) {
        LullabyGame game = createGame();
        game.start();
    }
}
