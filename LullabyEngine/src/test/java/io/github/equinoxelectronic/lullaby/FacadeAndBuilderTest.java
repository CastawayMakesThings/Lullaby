package io.github.equinoxelectronic.lullaby;

import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.gamedata.Item;
import io.github.equinoxelectronic.lullaby.gamedata.ItemClass;
import io.github.equinoxelectronic.lullaby.gamedata.Stat;
import io.github.equinoxelectronic.lullaby.mechanics.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FacadeAndBuilderTest {

    @Test
    public void testLullabyStaticFacadeAndGameConfig() {
        LullabyGame game = Lullaby.create("My Adventure")
                .author("Author Name")
                .saveFileVersion("1.0.0")
                .startingScene("town")
                .icon("assets/icon.png")
                .displayTime(true)
                .windowSize(1024, 768)
                .useScenePrefixByDefault("zone1_")
                .excludeSceneFromRecords("secret_room");

        assertSame(game, Lullaby.get("My Adventure"));
        assertEquals("My Adventure", game.getName());
        assertEquals("Author Name", game.getAuthor());
        assertEquals("1.0.0", game.getSaveFileVersion());
        assertEquals("town", game.getStartingScene());
        assertEquals("assets/icon.png", game.getIconPath());
        assertTrue(game.shouldDisplayTime());
        assertEquals(1024, game.getWindowSizeX());
        assertEquals(768, game.getWindowSizeY());
        assertEquals("zone1_", game.getUseScenePrefixByDefault());
        assertTrue(game.isSceneExcludedFromRecords("secret_room"));
    }

    @Test
    public void testLullabyGameSceneManagementFacades() {
        LullabyGame game = new LullabyGame();

        Scene forest = Scene.create("forest")
                .title("The Deep Forest")
                .text("You stand among ancient trees.");
        game.addScene(forest);

        game.scene("cave")
                .title("Dark Cave")
                .text("It is pitch black.");

        assertTrue(game.hasScene("forest"));
        assertTrue(game.hasScene("cave"));
        assertFalse(game.hasScene("nonexistent"));
        assertEquals("The Deep Forest", game.getScene("forest").titleText);
        assertEquals(2, game.getScenes().size());
    }

    @Test
    public void testGameStateFacades() {
        LullabyGame game = new LullabyGame();

        game.setPlayerName("Adventurer");
        assertEquals("Adventurer", game.getPlayerName());

        game.setFlag("has_key", true);
        assertTrue(game.getFlag("has_key"));
        assertFalse(game.toggleFlag("has_key"));
        assertFalse(game.getFlag("has_key"));

        game.setCounter("gold", 50);
        assertEquals(50, game.getCounter("gold"));
        assertEquals(60, game.incrementCounter("gold", 10));
        assertEquals(61, game.incrementCounter("gold"));
        assertEquals(56, game.decrementCounter("gold", 5));
        assertEquals(55, game.decrementCounter("gold"));

        game.setString("quest", "Find the relic");
        assertEquals("Find the relic", game.getString("quest"));

        game.setVar("custom_key", 123);
        assertTrue(game.hasVar("custom_key"));
        assertTrue(game.isVarEqual("custom_key", 123));
        assertTrue(game.isVarEqual("custom_key", "123"));
        assertEquals(123, (int) game.getVar("custom_key", Integer.class));
        assertEquals(123, (int) game.getVar("custom_key", 0));
        game.removeVar("custom_key");
        assertFalse(game.hasVar("custom_key"));
    }

    @Test
    public void testStatsFacades() {
        LullabyGame game = new LullabyGame();

        game.addStat("hp", "Health Points", 100);
        game.addStat(Stat.make("mana", "Mana Points", 50));

        assertTrue(game.hasStat("hp"));
        assertTrue(game.hasStat("mana"));
        assertEquals(100, game.getStatValue("hp"));
        assertEquals("Health Points", game.getStat("hp").getDisplayName());

        game.modifyStat("hp", -20);
        assertEquals(80, game.getStatValue("hp"));

        game.setStat("hp", 95);
        assertEquals(95, game.getStatValue("hp"));
    }

    @Test
    public void testItemAndInventoryFacades() {
        LullabyGame game = new LullabyGame();

        ItemClass potion = ItemClass.make("potion", "Health Potion", "Restores 50 HP");
        game.registerItem(potion);
        game.registerItem("sword", "Iron Sword", "A sharp iron blade");

        assertNotNull(game.getItemClass("potion"));
        assertNotNull(game.getItemClass("sword"));
        assertEquals("Health Potion", game.getItemClass("potion").getDisplayName());

        game.giveItem("potion", 3);
        game.giveItem(new Item("sword", 1));

        assertTrue(game.hasItem("potion", 3));
        assertTrue(game.hasItem("sword"));
        assertEquals(3, game.getItemQuantity("potion"));

        game.removeItem("potion", 1);
        assertEquals(2, game.getItemQuantity("potion"));

        game.removeItem(new Item("sword", 1));
        assertFalse(game.hasItem("sword"));
    }

    @Test
    public void testTimeFacades() {
        LullabyGame game = new LullabyGame();

        game.setTime(Time.construct().hours(2).minutes(30).construct());
        assertNotNull(game.getTime());
        assertEquals(9000, game.getTime().seconds());

        game.advanceTime(60);
        assertEquals(9060, game.getTime().seconds());
        assertFalse(game.getFormattedTime().isEmpty());
    }

    @Test
    public void testChoiceAndFieldBuilders() {
        Choice choice = Choice.of("Open Chest", "chest_opened")
                .label("Unlock and Open Chest")
                .target("chest_opened_2")
                .requiresItem("key", 1)
                .consumesItem("key", 1)
                .givesItem("gold_coins", 50)
                .modifyStat("xp", 100);

        assertEquals("Unlock and Open Chest", choice.label);
        assertEquals("chest_opened_2", choice.targetSceneId);

        Field field = Field.of("Enter secret code...", "vault_opened")
                .placeholder("Enter code")
                .target("vault_opened_2")
                .bindTo("entered_code");

        assertEquals("Enter code", field.placeholder);
        assertEquals("vault_opened_2", field.targetSceneId);
        assertEquals("entered_code", field.boundVariableKey);
    }

    @Test
    public void testSceneBuilderFactoriesAndMethods() {
        Scene scene = Scene.make("tavern")
                .title("The Prancing Pony")
                .text("A warm hearth welcomes you.")
                .textIfFlag("knows_innkeeper", "The innkeeper waves at you.")
                .textIfVarEquals("gold", 100, "You feel wealthy.")
                .choice(Choice.make("Drink Ale", "bar").setStat("hp", 100))
                .field(Field.make("Say something...", "chat"))
                .isEnding(false);

        assertEquals("tavern", scene.id);
        assertEquals("The Prancing Pony", scene.titleText);
        assertEquals(3, scene.textGenerators.size());
        assertEquals(1, scene.choices.size());
        assertEquals(1, scene.fields.size());
        assertFalse(scene.isEnding);
    }

    @Test
    public void testItemClassAndStatFluentBuilders() {
        ItemClass item = new ItemClass()
                .id("gem")
                .displayName("Ruby Gem")
                .description("A shiny red ruby.");

        assertEquals("gem", item.getId());
        assertEquals("Ruby Gem", item.getDisplayName());
        assertEquals("A shiny red ruby.", item.getDescription());

        Stat stat = new Stat("mana", "MP", 100)
                .displayName("Mana")
                .value(80)
                .withValue(90);

        assertEquals("mana", stat.getIdentifier());
        assertEquals("Mana", stat.getDisplayName());
        assertEquals(90, stat.getValue());
    }
}
