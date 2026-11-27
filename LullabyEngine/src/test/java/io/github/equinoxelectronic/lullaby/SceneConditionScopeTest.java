package io.github.equinoxelectronic.lullaby;

import io.github.equinoxelectronic.lullaby.content.*;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.ItemClass;
import io.github.equinoxelectronic.lullaby.gamedata.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class SceneConditionScopeTest {

    private GameState gameState;

    @BeforeEach
    public void setup() {
        gameState = new GameState();
        gameState.addStat(Stat.make("health", "Health", 100));
    }

    @Test
    public void testScopedConditionWhenTrueAndFalse() {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Scene scene = Scene.make("test_scene")
                .text("Always visible")
                .whenFlag("unlocked", scope -> {
                    scope.text("Secret text")
                            .image(img)
                            .choice("Secret Choice", "secret_target")
                            .field("Secret Field", "field_target");
                });

        assertEquals(5, scene.elements.size());
        assertTrue(scene.elements.get(0).isVisible(gameState)); // Always visible

        // Flag is false initially
        assertFalse(scene.elements.get(1).isVisible(gameState)); // text
        assertFalse(scene.elements.get(2).isVisible(gameState)); // image
        assertFalse(scene.elements.get(3).isVisible(gameState)); // choice
        assertFalse(scene.elements.get(4).isVisible(gameState)); // field

        SceneChoiceElement choiceElem = (SceneChoiceElement) scene.elements.get(3);
        assertFalse(choiceElem.getChoice().isVisible(gameState));

        SceneFieldElement fieldElem = (SceneFieldElement) scene.elements.get(4);
        assertFalse(fieldElem.getField().isVisible(gameState));

        // Now set flag to true
        gameState.setFlag("unlocked", true);
        assertTrue(scene.elements.get(1).isVisible(gameState));
        assertTrue(scene.elements.get(2).isVisible(gameState));
        assertTrue(scene.elements.get(3).isVisible(gameState));
        assertTrue(scene.elements.get(4).isVisible(gameState));
        assertTrue(choiceElem.getChoice().isVisible(gameState));
        assertTrue(fieldElem.getField().isVisible(gameState));
    }

    @Test
    public void testScopedFlagFalse() {
        Scene scene = Scene.make("test_flag_false")
                .whenFlagFalse("hasEscaped", scope -> {
                    scope.text("Still trapped in the room.")
                         .choice("Look for exit", "search");
                });

        assertEquals(2, scene.elements.size());
        assertTrue(scene.elements.get(0).isVisible(gameState));
        assertTrue(scene.elements.get(1).isVisible(gameState));

        gameState.setFlag("hasEscaped", true);
        assertFalse(scene.elements.get(0).isVisible(gameState));
        assertFalse(scene.elements.get(1).isVisible(gameState));
    }

    @Test
    public void testScopedVarEquals() {
        Scene scene = Scene.make("test_var")
                .whenVarEquals("chapter", 3, scope -> {
                    scope.text("Chapter 3 Intro")
                         .choice("Continue Chapter 3", "ch3_main");
                })
                .whenVarNotEqual("chapter", 3, scope -> {
                    scope.text("Not Chapter 3");
                });

        assertEquals(3, scene.elements.size());

        // Initial chapter is null
        assertFalse(scene.elements.get(0).isVisible(gameState));
        assertFalse(scene.elements.get(1).isVisible(gameState));
        assertTrue(scene.elements.get(2).isVisible(gameState));

        gameState.setVar("chapter", 3);
        assertTrue(scene.elements.get(0).isVisible(gameState));
        assertTrue(scene.elements.get(1).isVisible(gameState));
        assertFalse(scene.elements.get(2).isVisible(gameState));
    }

    @Test
    public void testScopedItemHelpers() {
        Scene scene = Scene.make("test_items")
                .whenHasItem("key", scope -> {
                    scope.text("You hold the key in your hands.")
                         .choice("Unlock door", "door_open");
                })
                .whenDoesntHaveItem("key", scope -> {
                    scope.text("The door is locked tight.");
                });

        assertEquals(3, scene.elements.size());

        assertFalse(scene.elements.get(0).isVisible(gameState));
        assertFalse(scene.elements.get(1).isVisible(gameState));
        assertTrue(scene.elements.get(2).isVisible(gameState));

        gameState.giveItem("key", 1);

        assertTrue(scene.elements.get(0).isVisible(gameState));
        assertTrue(scene.elements.get(1).isVisible(gameState));
        assertFalse(scene.elements.get(2).isVisible(gameState));
    }

    @Test
    public void testScopedStatHelpers() {
        Scene scene = Scene.make("test_stats")
                .whenStatGreaterThan("health", 50, scope -> {
                    scope.text("You are healthy.");
                })
                .whenStatLessThan("health", 20, scope -> {
                    scope.text("You are in critical condition!");
                })
                .whenStatEquals("health", 0, scope -> {
                    scope.text("You have perished.");
                });

        assertEquals(3, scene.elements.size());

        // Initial health = 100
        assertTrue(scene.elements.get(0).isVisible(gameState));
        assertFalse(scene.elements.get(1).isVisible(gameState));
        assertFalse(scene.elements.get(2).isVisible(gameState));

        gameState.setStatValue("health", 15);
        assertFalse(scene.elements.get(0).isVisible(gameState));
        assertTrue(scene.elements.get(1).isVisible(gameState));
        assertFalse(scene.elements.get(2).isVisible(gameState));

        gameState.setStatValue("health", 0);
        assertFalse(scene.elements.get(0).isVisible(gameState));
        assertTrue(scene.elements.get(1).isVisible(gameState)); // 0 < 20 is true
        assertTrue(scene.elements.get(2).isVisible(gameState)); // 0 == 0 is true
    }

    @Test
    public void testNestedWhenScope() {
        Scene scene = Scene.make("nested_scene")
                .whenFlag("questStarted", outer -> {
                    outer.text("Quest is underway.")
                         .whenFlag("questComplete", inner -> {
                             inner.text("Quest complete! Claim reward.")
                                  .choice("Claim 100 Gold", "reward_scene");
                         });
                });

        assertEquals(3, scene.elements.size());

        // Neither flag set
        assertFalse(scene.elements.get(0).isVisible(gameState));
        assertFalse(scene.elements.get(1).isVisible(gameState));
        assertFalse(scene.elements.get(2).isVisible(gameState));

        // Start quest
        gameState.setFlag("questStarted", true);
        assertTrue(scene.elements.get(0).isVisible(gameState));
        assertFalse(scene.elements.get(1).isVisible(gameState));
        assertFalse(scene.elements.get(2).isVisible(gameState));

        // Complete quest
        gameState.setFlag("questComplete", true);
        assertTrue(scene.elements.get(0).isVisible(gameState));
        assertTrue(scene.elements.get(1).isVisible(gameState));
        assertTrue(scene.elements.get(2).isVisible(gameState));
    }

    @Test
    public void testChoiceActionInScope() {
        final boolean[] clicked = {false};
        Scene scene = Scene.make("choice_action_scene")
                .when(state -> true, scope -> {
                    scope.choice("Action Choice", "target", s -> clicked[0] = true);
                });

        SceneChoiceElement elem = (SceneChoiceElement) scene.elements.get(0);
        elem.getChoice().execute(gameState);
        assertTrue(clicked[0]);
    }
}
