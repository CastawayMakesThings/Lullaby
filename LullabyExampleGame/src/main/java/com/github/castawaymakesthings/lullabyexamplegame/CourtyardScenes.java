package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;

/**
 * The Central Courtyard hub connecting all wings of Chronos Manor.
 * Showcases time advancement, resting mechanics, conditional item consumption, and area exploration.
 */
public class CourtyardScenes {

    public static void register(LullabyGame game) {
        // --- Central Hub Scene ---
        game.scene("courtyard")
                .title("The Chronos Courtyard")
                .text("You stand in the expansive central courtyard surrounded by overgrown rosebushes and ticking stone gargoyles.")
                .text("To the West lies the Great Library. To the East is the Alchemy Laboratory. Northward stands the ominous Clock Tower.")
                .choice("Enter the Great Library", "library")
                .choice("Enter the Alchemy Laboratory", "alchemy_lab")
                .choice("Approach the Clock Tower Entrance", "clock_tower_gates")
                .choice(
                        Choice.make("Search the Gargoyle Fountain for Secrets", "courtyard_search")
                                .hiddenIfFlag("found_courtyard_gold")
                                .onSelect(s -> s.advanceTime(10 * 60))
                )
                .choice(
                        Choice.make("Rest by the Fountain (+25 HP, +20 Mana, Passes 1 Hour)", "courtyard_rest")
                                .displayIf(s -> s.getStatValue("health") < 125 || s.getStatValue("mana") < 75)
                                .onSelect(s -> {
                                    s.modifyStat("health", 25);
                                    s.modifyStat("mana", 20);
                                    s.advanceTime(60 * 60);
                                    s.incrementCounter("times_rested");
                                })
                )
                .choice(
                        Choice.make("Drink Crimson Elixir (+35 HP)", "courtyard_drink_potion")
                                .displayIfHasItem("health_potion")
                                .onSelect(s -> {
                                    s.removeItem("health_potion");
                                    s.modifyStat("health", 35);
                                })
                )
                .choice(
                        Choice.make("Meditate with Aether Crystal (+30 Mana)", "courtyard_use_crystal")
                                .displayIfHasItem("mana_crystal")
                                .onSelect(s -> {
                                    s.removeItem("mana_crystal");
                                    s.modifyStat("mana", 30);
                                })
                );

        // --- Courtyard Rest Feedback ---
        game.scene("courtyard_rest")
                .title("Restful Respite")
                .text("You sit by the obsidian fountain, listening to the rhythmic splash of water and the distant tolling bells.")
                .text("Your vitality and arcane energies feel refreshed.")
                .choice("Return to Courtyard", "courtyard");

        // --- Courtyard Search for Secrets ---
        game.scene("courtyard_search")
                .title("A Hidden Cache")
                .text("You brush away damp ivy beneath a stone gargoyle and discover an old brass coin purse left by a previous traveler!")
                .text("You gained 15 Gold Coins!")
                .choice(
                        Choice.make("Pocket the Coins and Continue", "courtyard")
                                .onSelect(s -> {
                                    s.modifyStat("gold", 15);
                                    s.setFlag("found_courtyard_gold", true);
                                    s.incrementCounter("secrets_found");
                                })
                );

        // --- Drink Potion Feedback ---
        game.scene("courtyard_drink_potion")
                .title("Vitality Restored")
                .text("You uncork a Crimson Elixir and drink the glowing liquid. Warmth surges through your veins (+35 HP).")
                .choice("Return to Courtyard", "courtyard");

        // --- Use Crystal Feedback ---
        game.scene("courtyard_use_crystal")
                .title("Aether Infusion")
                .text("You crush the Aether Crystal between your palms. Sparkling blue dust envelops you, restoring your arcane focus (+30 Mana).")
                .choice("Return to Courtyard", "courtyard");
    }
}
