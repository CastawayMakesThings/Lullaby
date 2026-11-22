package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;

/**
 * The Alchemy Laboratory wing featuring resource management, merchants, and experimental brewing.
 */
public class AlchemyLabScenes {

    public static void register(LullabyGame game) {
        // --- Lab Entrance ---
        game.scene("alchemy_lab")
                .title("The Alchemy Laboratory")
                .text("Glass retorts and brass alembics bubble with iridescent fluids along slate countertops.")
                .text("An automated Clockwork Alchemist stands behind a copper counter, clicking rhythmically as it awaits customers.")
                .choice("Browse the Clockwork Merchant's Wares", "alchemy_shop")
                .choice("Use the Transmutation Cauldron", "alchemy_cauldron")
                .choice("Return to the Courtyard", "courtyard");

        // --- Shop Menu ---
        game.scene("alchemy_shop")
                .title("The Clockwork Merchant")
                .text("The brass automaton whirs: 'Greetings, patron. Insert gold coins to dispense alchemical supplies.'")
                .choice(
                        Choice.make("Buy Crimson Elixir (10 Gold)", "alchemy_shop_buy_potion")
                                .displayIf(s -> s.getStatValue("gold") >= 10)
                                .onSelect(s -> {
                                    s.modifyStat("gold", -10);
                                    s.giveItem("health_potion", 1);
                                })
                )
                .choice(
                        Choice.make("Buy Aether Crystal (15 Gold)", "alchemy_shop_buy_crystal")
                                .displayIf(s -> s.getStatValue("gold") >= 15)
                                .onSelect(s -> {
                                    s.modifyStat("gold", -15);
                                    s.giveItem("mana_crystal", 1);
                                })
                )
                .choice(
                        Choice.make("Buy Golden Resonance Gear (30 Gold)", "alchemy_shop_buy_gear")
                                .displayIf(s -> s.getStatValue("gold") >= 30)
                                .hiddenIfHasItem("clockwork_gear")
                                .onSelect(s -> {
                                    s.modifyStat("gold", -30);
                                    s.giveItem("clockwork_gear", 1);
                                })
                )
                .choice("Leave Merchant Counter", "alchemy_lab");

        // --- Shop Purchase Feedbacks ---
        game.scene("alchemy_shop_buy_potion")
                .title("Item Dispensed")
                .text("The automaton clicks and dispenses a sealed Crimson Elixir into the brass chute. (-10 Gold)")
                .choice("Buy More Items", "alchemy_shop")
                .choice("Return to Lab", "alchemy_lab");

        game.scene("alchemy_shop_buy_crystal")
                .title("Item Dispensed")
                .text("A glowing Aether Crystal slides down the velvet-lined tray. (-15 Gold)")
                .choice("Buy More Items", "alchemy_shop")
                .choice("Return to Lab", "alchemy_lab");

        game.scene("alchemy_shop_buy_gear")
                .title("Rare Item Dispensed")
                .text("The automaton opens a heavy brass safe and hands you a Golden Resonance Gear! (-30 Gold)")
                .choice("Buy More Items", "alchemy_shop")
                .choice("Return to Lab", "alchemy_lab");

        // --- Transmutation Cauldron ---
        game.scene("alchemy_cauldron")
                .title("The Transmutation Cauldron")
                .text("A violet flame burns steadily beneath a heavy iron cauldron. You can channel your arcane power to create wealth or brew elixirs.")
                .choice(
                        Choice.make("Transmute Mana into Gold (-25 Mana, +20 Gold)", "alchemy_transmute")
                                .displayIf(s -> s.getStatValue("mana") >= 25)
                                .onSelect(s -> {
                                    s.modifyStat("mana", -25);
                                    s.modifyStat("gold", 20);
                                    s.advanceTime(15 * 60L);
                                })
                )
                .choice(
                        Choice.make("Synthesize Crimson Elixir (-15 Mana, -5 Gold)", "alchemy_synthesize")
                                .displayIf(s -> s.getStatValue("mana") >= 15 && s.getStatValue("gold") >= 5)
                                .onSelect(s -> {
                                    s.modifyStat("mana", -15);
                                    s.modifyStat("gold", -5);
                                    s.giveItem("health_potion", 1);
                                    s.advanceTime(15 * 60L);
                                })
                )
                .choice("Step Away from the Cauldron", "alchemy_lab");

        game.scene("alchemy_transmute")
                .title("Transmutation Successful")
                .text("You focus your magical energy into the boiling violet liquid. Solid gold nuggets precipitate out of the solution! (+20 Gold)")
                .choice("Return to Laboratory", "alchemy_lab");

        game.scene("alchemy_synthesize")
                .title("Elixir Synthesized")
                .text("You carefully blend herbs and distilled aether into the mixture. A fresh Crimson Elixir is bottled successfully.")
                .choice("Return to Laboratory", "alchemy_lab");
    }
}
