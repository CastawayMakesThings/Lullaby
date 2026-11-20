package io.github.equinoxelectronic.lullaby;

import io.github.equinoxelectronic.lullaby.gamedata.GameData;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.ItemClass;
import io.github.equinoxelectronic.lullaby.ui.InventoryDialog;
import io.github.equinoxelectronic.lullaby.ui.SideBox;
import io.github.equinoxelectronic.lullaby.ui.SideBoxButton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryUITest {

    private GameState gameState;
    private GameData gameData;

    @BeforeEach
    public void setup() {
        gameState = new GameState();
        gameData = new GameData().setName("Test RPG");
    }

    @Test
    public void testSideBoxContainsInventoryButton() {
        JPanel vbox = SideBox.getVBox(gameState, gameData);
        assertNotNull(vbox);

        // Find the bottom panel
        JPanel bottomPanel = null;
        for (Component c : vbox.getComponents()) {
            if (c instanceof JPanel) {
                bottomPanel = (JPanel) c;
                break;
            }
        }
        assertNotNull(bottomPanel, "SideBox should contain a button panel");

        boolean hasInventoryBtn = false;
        boolean hasSaveBtn = false;
        boolean hasLoadBtn = false;
        boolean hasQuitBtn = false;

        for (Component c : bottomPanel.getComponents()) {
            if (c instanceof SideBoxButton) {
                SideBoxButton btn = (SideBoxButton) c;
                if ("Inventory".equals(btn.getText())) hasInventoryBtn = true;
                if ("Save".equals(btn.getText())) hasSaveBtn = true;
                if ("Load".equals(btn.getText())) hasLoadBtn = true;
                if ("Quit".equals(btn.getText())) hasQuitBtn = true;
            }
        }

        assertTrue(hasInventoryBtn, "SideBox must contain an Inventory button");
        assertTrue(hasSaveBtn, "SideBox must contain a Save button");
        assertTrue(hasLoadBtn, "SideBox must contain a Load button");
        assertTrue(hasQuitBtn, "SideBox must contain a Quit button");
    }

    @Test
    public void testInventoryDialogEmptyAndWithItems() {
        // Register item classes
        ItemClass potion = new ItemClass();
        potion.id = "healing_potion";
        potion.displayName = "Healing Potion";
        potion.description = "Restores 50 HP.";
        gameState.getInventory().registerItemClass(potion);

        ItemClass key = new ItemClass();
        key.id = "rusty_key";
        key.displayName = "Rusty Key";
        key.description = "Opens an old iron door.";
        gameState.getInventory().registerItemClass(key);

        gameState.giveItem("healing_potion", 3);
        gameState.giveItem("rusty_key", 1);

        // Create a headless-safe test or swing test dialog
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = InventoryDialog.show(null, gameState, gameData);
            assertNotNull(dialog);
            assertEquals("Inventory", dialog.getTitle());
            dialog.dispose();
        });
    }
}
