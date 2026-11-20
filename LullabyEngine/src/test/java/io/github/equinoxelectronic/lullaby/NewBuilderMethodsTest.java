package io.github.equinoxelectronic.lullaby;

import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NewBuilderMethodsTest {

    private GameState gameState;

    @BeforeEach
    public void setup() {
        gameState = new GameState();
    }

    @Test
    public void testSceneTextIfFlag() {
        Scene scene = new Scene("testScene");
        scene.textIfFlag("foundKey", "You have found the key.")
             .textIfFlag("foundKey", s -> "Dynamic: Key found!")
             .textIfFlagFalse("foundKey", "You do not have the key.")
             .textIfFlagFalse("foundKey", s -> "Dynamic: Key missing!");

        // Initially flag is false
        assertEquals("", scene.textGenerators.get(0).apply(gameState));
        assertEquals("", scene.textGenerators.get(1).apply(gameState));
        assertEquals("You do not have the key.", scene.textGenerators.get(2).apply(gameState));
        assertEquals("Dynamic: Key missing!", scene.textGenerators.get(3).apply(gameState));

        // Set flag to true
        gameState.setFlag("foundKey", true);
        assertEquals("You have found the key.", scene.textGenerators.get(0).apply(gameState));
        assertEquals("Dynamic: Key found!", scene.textGenerators.get(1).apply(gameState));
        assertEquals("", scene.textGenerators.get(2).apply(gameState));
        assertEquals("", scene.textGenerators.get(3).apply(gameState));
    }

    @Test
    public void testSceneTextIfVarEqualsAndNotEqual() {
        Scene scene = new Scene("varScene");
        scene.textIfVarEquals("playerName", "Alice", "Hello Alice")
             .textIfVarNotEqual("playerName", "Alice", "You are not Alice")
             .textIfVarEquals("score", 100, "Perfect score!")
             .textIfVarEquals("health", 99.5, "Health is 99.5")
             .textIfVarEquals("isAlive", true, "You are alive")
             .textIfVarEquals("gold", 500L, "Gold is 500");

        gameState.setVar("playerName", "Alice");
        gameState.setVar("score", 100);
        gameState.setVar("health", 99.5);
        gameState.setVar("isAlive", true);
        gameState.setVar("gold", 500L);

        assertEquals("Hello Alice", scene.textGenerators.get(0).apply(gameState));
        assertEquals("", scene.textGenerators.get(1).apply(gameState));
        assertEquals("Perfect score!", scene.textGenerators.get(2).apply(gameState));
        assertEquals("Health is 99.5", scene.textGenerators.get(3).apply(gameState));
        assertEquals("You are alive", scene.textGenerators.get(4).apply(gameState));
        assertEquals("Gold is 500", scene.textGenerators.get(5).apply(gameState));

        gameState.setVar("playerName", "Bob");
        assertEquals("", scene.textGenerators.get(0).apply(gameState));
        assertEquals("You are not Alice", scene.textGenerators.get(1).apply(gameState));
    }

    @Test
    public void testSceneTextIfFirstAndIteration() {
        Scene scene = new Scene("repeatScene");
        scene.textIfFirst("Welcome for the first time!")
             .textIfIteration(2, "Welcome back for the second time!")
             .textIfIteration(it -> it >= 3, "You have visited many times!");

        gameState.setCurrentSceneID("repeatScene"); // visit count = 1
        assertEquals("Welcome for the first time!", scene.textGenerators.get(0).apply(gameState));
        assertEquals("", scene.textGenerators.get(1).apply(gameState));
        assertEquals("", scene.textGenerators.get(2).apply(gameState));

        gameState.setCurrentSceneID("repeatScene"); // visit count = 2
        assertEquals("", scene.textGenerators.get(0).apply(gameState));
        assertEquals("Welcome back for the second time!", scene.textGenerators.get(1).apply(gameState));
        assertEquals("", scene.textGenerators.get(2).apply(gameState));

        gameState.setCurrentSceneID("repeatScene"); // visit count = 3
        assertEquals("", scene.textGenerators.get(0).apply(gameState));
        assertEquals("", scene.textGenerators.get(1).apply(gameState));
        assertEquals("You have visited many times!", scene.textGenerators.get(2).apply(gameState));
    }

    @Test
    public void testSceneTextIfHasItemAndDoesntHaveItem() {
        Scene scene = new Scene("itemScene");
        scene.textIfHasItem("potion", "You have a potion.")
             .textIfHasItem("coin", 5, "You have 5 or more coins.")
             .textIfDoesntHaveItem("sword", "You need a sword.")
             .textIfDoesntHaveItem("coin", 10, "You don't have 10 coins.");

        assertEquals("", scene.textGenerators.get(0).apply(gameState));
        assertEquals("", scene.textGenerators.get(1).apply(gameState));
        assertEquals("You need a sword.", scene.textGenerators.get(2).apply(gameState));
        assertEquals("You don't have 10 coins.", scene.textGenerators.get(3).apply(gameState));

        gameState.giveItem("potion", 1);
        gameState.giveItem("coin", 6);
        gameState.giveItem("sword", 1);

        assertEquals("You have a potion.", scene.textGenerators.get(0).apply(gameState));
        assertEquals("You have 5 or more coins.", scene.textGenerators.get(1).apply(gameState));
        assertEquals("", scene.textGenerators.get(2).apply(gameState));
        assertEquals("You don't have 10 coins.", scene.textGenerators.get(3).apply(gameState));
    }

    @Test
    public void testChoiceHiddenIfAndDisplayIf() {
        Choice choice = new Choice("Open Gate", "gateScene")
                .displayIfFlag("hasKey")
                .hiddenIfFlag("guardAwake");

        // hasKey false, guardAwake false -> displayIf fails -> invisible
        assertFalse(choice.isVisible(gameState));

        // hasKey true, guardAwake false -> displayIf passes, hiddenIf false -> visible
        gameState.setFlag("hasKey", true);
        assertTrue(choice.isVisible(gameState));

        // hasKey true, guardAwake true -> hiddenIf overrides displayIf -> invisible
        gameState.setFlag("guardAwake", true);
        assertFalse(choice.isVisible(gameState));
    }

    @Test
    public void testChoiceSetVarAndDynamicText() {
        Choice choice = new Choice("Static", "next")
                .setDynamicText(s -> "Player: " + s.getString("name") + " (Score: " + s.getCounter("score") + ")")
                .setString("name", "Hero")
                .setCounter("score", 50)
                .setFlag("questAccepted", true);

        assertEquals("Player: null (Score: 0)", choice.getLabel(gameState));

        choice.execute(gameState);

        assertEquals("Hero", gameState.getString("name"));
        assertEquals(50, gameState.getCounter("score"));
        assertTrue(gameState.getFlag("questAccepted"));
        assertEquals("Player: Hero (Score: 50)", choice.getLabel(gameState));
    }

    @Test
    public void testChoiceGiveRequiresAndConsumesItem() {
        Choice buyChoice = new Choice("Buy Sword", "shop")
                .requiresItem("coin", 10)
                .consumesItem("coin", 10)
                .giveItem("sword", 1);

        assertFalse(buyChoice.isVisible(gameState));

        gameState.giveItem("coin", 15);
        assertTrue(buyChoice.isVisible(gameState));

        buyChoice.execute(gameState);
        assertEquals(5, gameState.getInventory().getItemQuantity("coin"));
        assertEquals(1, gameState.getInventory().getItemQuantity("sword"));
        assertTrue(gameState.hasItem("sword"));
    }

    @Test
    public void testFieldPutAsVarAndSceneOverride() {
        Field field = new Field("Enter password...", "wrongScene")
                .putAsVar("password")
                .putAsInt("pin")
                .sceneOverideIfInputEquals("secret123", "secretRoom")
                .sceneOverrideIf(state -> state.getFlag("isVip"), "vipLounge");

        // Normal wrong input
        field.submit(gameState, "test");
        assertEquals("test", gameState.getVar("password"));
        assertEquals("wrongScene", field.resolveTargetSceneId(gameState, "test"));

        // Match input override
        assertEquals("secretRoom", field.resolveTargetSceneId(gameState, "secret123"));

        // Match flag override
        gameState.setFlag("isVip", true);
        assertEquals("vipLounge", field.resolveTargetSceneId(gameState, "anyInput"));

        // Integer pin parsing
        field.submit(gameState, "4321");
        assertEquals(4321, gameState.getCounter("pin"));
    }

    @Test
    public void testSceneConvenienceMethods() {
        Scene scene = new Scene("endScene")
                .choice("Restart", "start")
                .choice("Quit", "quit", s -> s.setFlag("quit", true))
                .field("Your feedback...", "thanks")
                .ending();

        assertTrue(scene.isEnding);
        assertEquals(2, scene.choices.size());
        assertEquals("Restart", scene.choices.get(0).label);
        assertEquals("Quit", scene.choices.get(1).label);
        assertEquals(1, scene.fields.size());

        scene.choices.get(1).execute(gameState);
        assertTrue(gameState.getFlag("quit"));
    }
}
