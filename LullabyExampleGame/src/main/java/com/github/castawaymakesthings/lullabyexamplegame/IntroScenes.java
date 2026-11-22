package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;
import io.github.equinoxelectronic.lullaby.content.Scene;

/**
 * Introduction, character creation with interactive text input, and class selection.
 */
public class IntroScenes {

    public static void register(LullabyGame game) {
        // --- 1. Intro Screen ---
        game.scene("intro")
                .title("Whispers of the Clockwork Manor")
                .image("Lullaby.png")
                .text("Thunder rumbles across the desolate moorlands as you arrive before the towering iron gates of Chronos Manor.")
                .text("Rumors speak of a master clockmaker who trapped time itself within the manor's grand clock tower.")
                .text("Armed with only your wits and ambition, you step into the courtyard...")
                .choice("Begin Your Journey", "char_creation")
                .choice(
                        Choice.make("Examine Manor Gates (Passes 15 min)", "gate_inspection")
                                .onSelect(s -> s.advanceTime(15 * 60))
                );

        // Optional exploration scene
        game.scene("gate_inspection")
                .title("The Iron Gates")
                .text("The towering wrought-iron gates are etched with zodiac symbols and interlocking brass gears.")
                .text("A small bronze inscription reads: 'Only those who balance knowledge, fortune, and vitality may unlock the true ending.'")
                .choice("Proceed Inside", "char_creation");

        // --- 2. Character Creation (Text Field Input) ---
        game.scene("char_creation")
                .title("Identity of the Explorer")
                .text("Before setting foot in the manor, you write your name in your expedition journal.")
                .field(
                        Field.make("Enter your adventurer name...", "welcome_named")
                                .bindTo("player_name")
                                .onEntered(state -> {
                                    String entered = state.getVar("player_name", "Adventurer");
                                    if (entered.trim().isEmpty()) {
                                        entered = "Adventurer";
                                    }
                                    state.setPlayerName(entered);
                                    state.setVar("player_name", entered);
                                })
                );

        // --- 3. Name Confirmation & Class Archetype Selection ---
        game.scene("welcome_named")
                .title("Choose Your Archetype")
                .text("Welcome, explorer. What background prepared you for the hazards of Chronos Manor?")
                .choice(
                        Choice.make("Arcane Scholar (+25 Mana, Chronos Manuscript)", "courtyard")
                                .onSelect(state -> {
                                    state.setVar("archetype", "Arcane Scholar");
                                    state.modifyStat("mana", 25);
                                    state.giveItem("ancient_tome", 1);
                                    state.giveItem("mana_crystal", 1);
                                    state.setFlag("has_scholar_training", true);
                                })
                )
                .choice(
                        Choice.make("Daring Rogue (+30 Gold, Rusty Key, +10 Mana)", "courtyard")
                                .onSelect(state -> {
                                    state.setVar("archetype", "Daring Rogue");
                                    state.modifyStat("gold", 30);
                                    state.modifyStat("mana", 10);
                                    state.giveItem("rusty_key", 1);
                                    state.setFlag("has_lockpicking", true);
                                })
                )
                .choice(
                        Choice.make("Valiant Knight (+25 Health, 2x Crimson Elixirs)", "courtyard")
                                .onSelect(state -> {
                                    state.setVar("archetype", "Valiant Knight");
                                    state.modifyStat("health", 25);
                                    state.giveItem("health_potion", 2);
                                    state.setFlag("has_heavy_armor", true);
                                })
                );
    }
}
