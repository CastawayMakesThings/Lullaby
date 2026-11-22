package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;

/**
 * The Great Library wing containing archives, lore, and an interactive riddle cipher using Field input.
 */
public class LibraryScenes {

    public static void register(LullabyGame game) {
        // --- Library Entrance / Hub ---
        game.scene("library")
                .title("The Great Library")
                .text("Towering mahogany bookshelves reach toward a stained-glass ceiling depicting the solar celestial spheres.")
                .text("Dust motes float in the air. In the center sits an ornate Cipher Desk with brass dials and a lockable compartment.")
                .choice("Search the Archives", "library_search")
                .choice(
                        Choice.make("Examine the Cipher Desk", "library_cipher")
                                .hiddenIfFlag("cipher_solved")
                )
                .choice(
                        Choice.make("Examine the Open Cipher Desk", "library_cipher_success")
                                .displayIfFlag("cipher_solved")
                )
                .choice("Return to the Courtyard", "courtyard");

        // --- Search Archives ---
        game.scene("library_search")
                .title("Searching the Archives")
                .text("You browse through centuries of astronomical records, horology journals, and alchemical treatises.")
                .choice(
                        Choice.make("Study the Chronos Lore (+10 Mana, Passes 30 min)", "library_lore")
                                .onSelect(s -> {
                                    s.advanceTime(30 * 60);
                                    s.modifyStat("mana", 10);
                                    if (!s.hasItem("ancient_tome")) {
                                        s.giveItem("ancient_tome", 1);
                                        s.setFlag("found_tome_in_library", true);
                                    }
                                })
                )
                .choice("Step Back to Library Center", "library");

        game.scene("library_lore")
                .title("Chronomancy Archives")
                .text("You read a faded passage: 'The master clockmaker attuned the tower's pendulum to the primordial name: CHRONOS.'")
                .text("You also find a copy of the Chronos Manuscript to add to your collection.")
                .choice("Return to Library Center", "library");

        // --- Cipher Desk Input Scene ---
        game.scene("library_cipher")
                .title("The Cipher Desk")
                .text("A brass plaque above the desk reads: 'I consume all things, yet give rhythm to the universe. Speak my primordial name to reveal the key.'")
                .field(
                        Field.make("Enter password (e.g. CHRONOS)...", "library_cipher_eval")
                                .bindTo("cipher_input")
                                .onEntered(state -> {
                                    String input = state.getVar("cipher_input", "").trim().toUpperCase();
                                    if ("CHRONOS".equals(input) || "TIME".equals(input)) {
                                        state.setFlag("cipher_solved", true);
                                        state.giveItem("rusty_key", 1);
                                        state.giveItem("clockwork_gear", 1);
                                        state.modifyStat("reputation", 10);
                                        state.incrementCounter("puzzles_solved");
                                    } else {
                                        state.setFlag("cipher_attempt_failed", true);
                                    }
                                })
                )
                .choice("Step Back", "library");

        // --- Cipher Evaluation Scene ---
        game.scene("library_cipher_eval")
                .title("Cipher Mechanism")
                .text("The gears within the desk spin with a whirring hum as the mechanism processes your answer...")
                .choice(
                        Choice.make("Check the Result", "library_cipher_success")
                                .displayIfFlag("cipher_solved")
                )
                .choice(
                        Choice.make("Check the Result", "library_cipher_fail")
                                .hiddenIfFlag("cipher_solved")
                );

        // --- Success Scene ---
        game.scene("library_cipher_success")
                .title("Cipher Unlocked!")
                .text("With a satisfying click, a hidden mahogany drawer slides open!")
                .text("Inside rests the Rusty Manor Key and a pristine Golden Resonance Gear! Your reputation as a scholar rises (+10 Reputation).")
                .choice("Return to Library Center", "library");

        // --- Failure Scene ---
        game.scene("library_cipher_fail")
                .title("Mechanism Jams")
                .text("A harsh grinding sound echoes as the dials reset. That wasn't the correct cipher password.")
                .text("Hint: Perhaps you should search the archives for clues about the primordial name.")
                .choice("Try Again", "library_cipher")
                .choice("Return to Library Center", "library");
    }
}
