package com.github.castawaymakesthings.lullabyexamplegame;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.content.SceneChoiceElement;
import io.github.equinoxelectronic.lullaby.content.SceneElement;
import io.github.equinoxelectronic.lullaby.content.SceneFieldElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleGameTest {

    @Test
    public void testExampleGameInitialization() {
        LullabyGame game = Main.createGame();

        assertNotNull(game);
        assertEquals("Whispers of the Clockwork Manor", game.getName());
        assertEquals("intro", game.game.gameData.getStartingSceneName());
        assertTrue(game.game.gameData.isShouldDisplayTime());

        // Verify registered stats
        assertTrue(game.hasStat("health"));
        assertTrue(game.hasStat("mana"));
        assertTrue(game.hasStat("gold"));
        assertTrue(game.hasStat("reputation"));

        // Verify registered item classes
        assertNotNull(game.getInventory().getGameItems().get("rusty_key"));
        assertNotNull(game.getInventory().getGameItems().get("health_potion"));
        assertNotNull(game.getInventory().getGameItems().get("mana_crystal"));
        assertNotNull(game.getInventory().getGameItems().get("ancient_tome"));
        assertNotNull(game.getInventory().getGameItems().get("clockwork_gear"));

        // Verify key scenes exist
        String[] expectedScenes = {
                "intro", "gate_inspection", "char_creation", "welcome_named",
                "courtyard", "courtyard_rest", "courtyard_search", "courtyard_drink_potion", "courtyard_use_crystal",
                "library", "library_search", "library_lore", "library_cipher", "library_cipher_eval", "library_cipher_success", "library_cipher_fail",
                "alchemy_lab", "alchemy_shop", "alchemy_shop_buy_potion", "alchemy_shop_buy_crystal", "alchemy_shop_buy_gear", "alchemy_cauldron", "alchemy_transmute", "alchemy_synthesize",
                "clock_tower_gates", "clock_tower_forced", "clock_tower_ascent", "clock_tower_apex",
                "ending_scholar", "ending_mage", "ending_warrior", "epilogue"
        };

        for (String sceneId : expectedScenes) {
            assertTrue(game.hasScene(sceneId), "Expected scene to exist: " + sceneId);
            Scene scene = game.getScene(sceneId);
            assertNotNull(scene, "Scene object should not be null: " + sceneId);
            assertNotNull(scene.titleText, "Scene should have title: " + sceneId);
        }
    }

    @Test
    public void testNoDanglingSceneTargets() {
        LullabyGame game = Main.createGame();

        // Check all choices and fields in all scenes to ensure every target scene actually exists
        for (Scene scene : game.game.story.scenes.values()) {
            for (SceneElement element : scene.elements) {
                if (element instanceof SceneChoiceElement) {
                    Choice choice = ((SceneChoiceElement) element).getChoice();
                    if (choice != null && choice.targetSceneId != null) {
                        assertTrue(game.hasScene(choice.targetSceneId),
                                "Scene '" + scene.id + "' has choice targeting non-existent scene: '" + choice.targetSceneId + "'");
                    }
                } else if (element instanceof SceneFieldElement) {
                    Field field = ((SceneFieldElement) element).getField();
                    if (field != null && field.targetSceneId != null) {
                        assertTrue(game.hasScene(field.targetSceneId),
                                "Scene '" + scene.id + "' has field targeting non-existent scene: '" + field.targetSceneId + "'");
                    }
                }
            }
        }
    }

    @Test
    public void testFieldInputAndClassSelectionCallbacks() {
        LullabyGame game = Main.createGame();

        // Simulate name input
        game.setVar("player_name", "Valeria");
        game.setPlayerName("Valeria");
        assertEquals("Valeria", game.getVar("player_name", ""));

        // Simulate choosing Arcane Scholar archetype
        game.modifyStat("mana", 25);
        game.giveItem("ancient_tome", 1);
        game.giveItem("mana_crystal", 1);
        game.setFlag("has_scholar_training", true);

        assertEquals(75, game.getStatValue("mana"));
        assertTrue(game.hasItem("ancient_tome"));
        assertTrue(game.hasItem("mana_crystal"));
        assertTrue(game.getFlag("has_scholar_training"));
    }

    @Test
    public void testPuzzleAndMerchantMechanics() {
        LullabyGame game = Main.createGame();

        // Test merchant purchase
        int startingGold = game.getStatValue("gold");
        assertTrue(startingGold >= 10);
        game.modifyStat("gold", -10);
        game.giveItem("health_potion", 1);

        assertEquals(startingGold - 10, game.getStatValue("gold"));
        assertTrue(game.hasItem("health_potion"));

        // Test potion consumption
        game.modifyStat("health", -40);
        assertEquals(60, game.getStatValue("health"));

        game.removeItem("health_potion");
        game.modifyStat("health", 35);
        assertEquals(95, game.getStatValue("health"));
        assertFalse(game.hasItem("health_potion"));
    }
}
