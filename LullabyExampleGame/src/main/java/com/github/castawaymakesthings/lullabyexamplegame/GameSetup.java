package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.gamedata.ItemClass;
import io.github.equinoxelectronic.lullaby.gamedata.Stat;
import io.github.equinoxelectronic.lullaby.mechanics.Time;

/**
 * Handles core configuration, stat definitions, item catalog, and initial state for the example game.
 */
public class GameSetup {

    public static void configure(LullabyGame game) {
        // Game Metadata & Window Settings via facade
        game.name("Whispers of the Clockwork Manor")
                .author("Castaway & Equinox")
                .saveFileVersion("v1.0.0")
                .startingScene("intro")
                .icon("Lullaby.png")
                .displayTime(true)
                .windowSize(900, 680);

        // Initial Game Time (Nightfall on Day 1, 20:00)
        game.setTime(Time.builder()
                .days(1)
                .hours(20)
                .minutes(0)
                .build());

        // Register Player Stats
        game.addStat(Stat.make("health", "Health Points", 100))
                .addStat(Stat.make("mana", "Mana Points", 50))
                .addStat(Stat.make("gold", "Gold Coins", 25))
                .addStat(Stat.make("reputation", "Reputation", 0));

        // Register Item Catalog
        game.registerItem(ItemClass.make("rusty_key", "Rusty Manor Key", "An ornate iron key inscribed with clockwork gears."))
                .registerItem(ItemClass.make("health_potion", "Crimson Elixir", "Restores up to 35 Health Points."))
                .registerItem(ItemClass.make("mana_crystal", "Aether Crystal", "A glowing azure gem that replenishes 30 Mana."))
                .registerItem(ItemClass.make("ancient_tome", "Chronos Manuscript", "An ancient treatise deciphering temporal clockwork mechanisms."))
                .registerItem(ItemClass.make("clockwork_gear", "Golden Resonance Gear", "A pristine cogwheel radiating subtle chronomantic energy."));
    }
}
