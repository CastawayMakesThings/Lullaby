package io.github.equinoxelectronic.lullaby;

import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class StructureTest {

    @Test
    public void testStoryAndSceneBuilder() {
        LullabyGame engine = new LullabyGame();

        engine.scene("intro")
                .title("Welcome")
                .text("Hello traveler")
                .text(s -> "Player: " + s.getPlayerName())
                .choice(new Choice("Go to Forest", "forest"))
                .field(Field.make("Enter name...", "intro"));

        Scene intro = engine.game.story.getScene("intro");
        assertNotNull(intro);
        assertEquals("Welcome", intro.titleText);
        assertEquals(2, intro.textGenerators.size());
        assertEquals(1, intro.choices.size());
        assertEquals(1, intro.fields.size());
        assertEquals("Go to Forest", intro.choices.get(0).label);
        assertEquals("forest", intro.choices.get(0).targetSceneId);

        engine.gameState.setPlayerName("Hero");
        assertEquals("Hello traveler", intro.textGenerators.get(0).apply(engine.gameState));
        assertEquals("Player: Hero", intro.textGenerators.get(1).apply(engine.gameState));
    }

    @Test
    public void testGameStateChangeListener() {
        GameState state = new GameState();
        AtomicBoolean updated = new AtomicBoolean(false);

        state.addChangeListener(() -> updated.set(true));

        state.addStat("health", "HP", 100);
        assertTrue(updated.get());

        updated.set(false);
        state.setStatValue("health", 80);
        assertTrue(updated.get());
        assertEquals(80, state.getStat("health").getValue());
    }

    @Test
    public void testGoToScenePreStart() {
        LullabyGame engine = new LullabyGame();
        engine.scene("start").text("Start scene");
        engine.scene("next").text("Next scene");

        engine.goToScene("start");
        assertEquals("start", engine.gameState.getCurrentSceneID());
        assertEquals("start", engine.game.story.scenes.keySet().iterator().next());
    }
}
