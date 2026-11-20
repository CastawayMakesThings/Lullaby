package io.github.equinoxelectronic.lullaby;

import io.github.equinoxelectronic.lullaby.gamedata.GameData;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.ui.SideBox;
import io.github.equinoxelectronic.lullaby.ui.UI;
import io.github.equinoxelectronic.lullaby.ui.util.FontUtils;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class UIAestheticTest {

    @Test
    public void testFontUtils() {
        JLabel label = new JLabel("Test");
        FontUtils.applyDefaultFont(label);
        assertEquals("Segoe UI", label.getFont().getFamily());
        assertEquals(14, label.getFont().getSize());
        assertEquals(Font.PLAIN, label.getFont().getStyle());

        FontUtils.applyBoldFont(label, 18);
        assertEquals(18, label.getFont().getSize());
        assertEquals(Font.BOLD, label.getFont().getStyle());

        Font f = FontUtils.getFont(Font.ITALIC, 16);
        assertEquals(16, f.getSize());
        assertEquals(Font.ITALIC, f.getStyle());
    }

    @Test
    public void testSideBoxAesthetics() {
        GameState state = new GameState();
        state.addStat("hp", "Health", 100);
        state.addStat("mana", "Mana", 50);

        GameData data = new GameData().setName("Aesthetic Game").setShouldDisplayTime(true);

        JPanel sideBox = SideBox.getVBox(state, data);
        assertEquals(SideBox.SIDEBAR_BG, sideBox.getBackground());
        assertNotNull(sideBox.getBorder());
    }

    @Test
    public void testUIInitAndColors() {
        LullabyGame engine = new LullabyGame();
        engine.game.gameData.setName("Dark Adventure");

        UI ui = new UI(engine);
        JFrame frame = ui.init();

        assertNotNull(frame);
        assertEquals("Dark Adventure", frame.getTitle());
        assertNotNull(ui.getLeftPanel());
        assertNotNull(ui.getScenePanel());
        assertEquals(UI.CONTENT_BG, ui.getScenePanel().getBackground());
        frame.dispose();
    }
}
