package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;

/**
 * Endings and Epilogue scenes concluding the player's expedition.
 */
public class EndingScenes {

    public static void register(LullabyGame game) {
        // --- 1. True Chronomancer Ending (Scholar Path) ---
        game.scene("ending_scholar")
                .title("Ending: The Master Chronomancer")
                .text("You place the Golden Resonance Gear into the heart of the great pendulum while chanting the incantation from the Chronos Manuscript.")
                .text("Golden light floods the chamber as the unstable temporal energies harmonize. The Clockwork Guardian bows respectfully before powering down.")
                .text("Time flows peacefully across the moorlands once more. You have unlocked the True Ending of Chronos Manor!")
                .choice("View Expedition Epilogue", "epilogue");

        // --- 2. Arcane Overload Ending (Mage Path) ---
        game.scene("ending_mage")
                .title("Ending: Arcane Dominance")
                .text("Sparks of blue lightning erupt from your hands, overloading the Clockwork Guardian's core!")
                .text("The automaton collapses in a shower of gears and condensed magical energy, leaving behind a hoard of 50 Gold Coins.")
                .text("You claim the manor's treasures as your rightful reward.")
                .choice("View Expedition Epilogue", "epilogue");

        // --- 3. Martial Victory Ending (Warrior Path) ---
        game.scene("ending_warrior")
                .title("Ending: The Iron Victor")
                .text("Dodging the guardian's mechanical fists, you sever its main hydraulic cables with a decisive strike!")
                .text("The behemoth crashes to the floor. You stand victorious, claiming the spoils of the Clockwork Manor.")
                .choice("View Expedition Epilogue", "epilogue");

        // --- 4. Epilogue Scene ---
        game.scene("epilogue")
                .title("Expedition Epilogue")
                .text("Thank you for playing this comprehensive Lullaby Engine demonstration game!")
                .ending();
    }
}
