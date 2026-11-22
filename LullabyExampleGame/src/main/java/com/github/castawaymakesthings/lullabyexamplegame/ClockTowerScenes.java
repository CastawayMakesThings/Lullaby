package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;

/**
 * The Clock Tower wing: gates, ascent, and the climatic encounter with the Clockwork Guardian.
 */
public class ClockTowerScenes {

    public static void register(LullabyGame game) {
        // --- Tower Gates Entrance ---
        game.scene("clock_tower_gates")
                .title("The Clock Tower Gates")
                .text("A colossal double door of brass and steel bars the path upward into the tower's summit.")
                .text("A complex gear-shaped keyhole locks the mechanism in place.")
                .choice(
                        Choice.make("Unlock Gates with Rusty Manor Key", "clock_tower_ascent")
                                .displayIfHasItem("rusty_key")
                                .onSelect(s -> s.setFlag("tower_unlocked", true))
                )
                .choice(
                        Choice.make("Pick the Lock (Rogue Training)", "clock_tower_ascent")
                                .displayIfFlag("has_lockpicking")
                                .hiddenIfHasItem("rusty_key")
                                .onSelect(s -> {
                                    s.setFlag("tower_unlocked", true);
                                    s.incrementCounter("locks_picked");
                                })
                )
                .choice(
                        Choice.make("Force Gates Open with Brute Force (-25 Health)", "clock_tower_forced")
                                .displayIf(s -> s.getStatValue("health") > 25)
                                .hiddenIfFlag("tower_unlocked")
                                .onSelect(s -> {
                                    s.modifyStat("health", -25);
                                    s.setFlag("tower_unlocked", true);
                                })
                )
                .choice("Return to the Courtyard", "courtyard");

        // --- Forced Open Gate Feedback ---
        game.scene("clock_tower_forced")
                .title("Gates Smashed Open")
                .text("You slam your shoulder into the gate with tremendous force. The heavy iron bars buckle and give way, but flying brass splinters wound you (-25 HP).")
                .choice("Ascend the Spiral Stairs", "clock_tower_ascent");

        // --- Ascent Scene ---
        game.scene("clock_tower_ascent")
                .title("Ascending the Clock Tower")
                .text("You climb a spiraling iron staircase winding past massive ticking pendulum shafts and spinning escapement wheels.")
                .text("As you step onto the apex observation platform, a colossal Clockwork Guardian rises from a nest of copper cables!")
                .choice(
                        Choice.make("Confront the Guardian", "clock_tower_apex")
                                .onSelect(s -> s.advanceTime(20 * 60L))
                );

        // --- Climax Encounter: Apex of the Tower ---
        game.scene("clock_tower_apex")
                .title("The Clockwork Guardian")
                .text("Eyes of glowing sapphire light flare to life on the automaton's bronze faceplate.")
                .text("The central temporal pendulum behind it pulses with unstable chronomantic energy, warping the flow of time!")
                .choice(
                        Choice.make("Align Chronos Manuscript and Golden Resonance Gear (True Ending)", "ending_scholar")
                                .displayIf(s -> s.hasItem("ancient_tome") && s.hasItem("clockwork_gear"))
                                .onSelect(s -> {
                                    s.modifyStat("reputation", 50);
                                    s.setVar("ending_type", "scholar");
                                })
                )
                .choice(
                        Choice.make("Overload the Core with an Arcane Blast (-35 Mana)", "ending_mage")
                                .displayIf(s -> s.getStatValue("mana") >= 35)
                                .onSelect(s -> {
                                    s.modifyStat("mana", -35);
                                    s.modifyStat("gold", 50);
                                    s.setVar("ending_type", "mage");
                                })
                )
                .choice(
                        Choice.make("Strike Down the Guardian with Martial Might (-30 Health)", "ending_warrior")
                                .displayIf(s -> s.getStatValue("health") >= 31)
                                .onSelect(s -> {
                                    s.modifyStat("health", -30);
                                    s.modifyStat("gold", 50);
                                    s.setVar("ending_type", "warrior");
                                })
                )
                .choice(
                        Choice.make("Drink Crimson Elixir during Combat (+35 HP)", "clock_tower_apex")
                                .displayIfHasItem("health_potion")
                                .onSelect(s -> {
                                    s.removeItem("health_potion");
                                    s.modifyStat("health", 35);
                                })
                )
                .choice("Flee Down to the Courtyard", "courtyard");
    }
}
