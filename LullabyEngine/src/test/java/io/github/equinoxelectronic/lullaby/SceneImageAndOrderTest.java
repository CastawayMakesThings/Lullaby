package io.github.equinoxelectronic.lullaby;

import io.github.equinoxelectronic.lullaby.content.*;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.ui.UI;
import io.github.equinoxelectronic.lullaby.ui.util.ImageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SceneImageAndOrderTest {

    private GameState gameState;

    @BeforeEach
    public void setup() {
        gameState = new GameState();
    }

    @Test
    public void testImageUtilsImageLoading() {
        BufferedImage testImg = new BufferedImage(120, 80, BufferedImage.TYPE_INT_ARGB);
        ImageIcon icon = ImageUtils.loadImage(testImg);
        assertNotNull(icon);
        assertEquals(120, icon.getIconWidth());
        assertEquals(80, icon.getIconHeight());

        ImageIcon directIcon = new ImageIcon(testImg);
        ImageIcon loadedDirect = ImageUtils.loadImage(directIcon);
        assertSame(directIcon, loadedDirect);

        assertNull(ImageUtils.loadImage(null));
        assertNull(ImageUtils.loadImage(""));
        assertNull(ImageUtils.loadImage("non_existent_file_12345.png"));
    }

    @Test
    public void testSceneElementOrdering() {
        BufferedImage img1 = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        BufferedImage img2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        Scene scene = new Scene("orderScene");
        scene.title("Ordered Scene")
                .text("First Text")
                .image(img1)
                .choice("Choice 1", "scene2")
                .text("Second Text")
                .image(img2)
                .field("Input here", "scene3");

        List<SceneElement> elements = scene.elements;
        assertEquals(6, elements.size());
        assertTrue(elements.get(0) instanceof SceneTextElement);
        assertTrue(elements.get(1) instanceof SceneImageElement);
        assertTrue(elements.get(2) instanceof SceneChoiceElement);
        assertTrue(elements.get(3) instanceof SceneTextElement);
        assertTrue(elements.get(4) instanceof SceneImageElement);
        assertTrue(elements.get(5) instanceof SceneFieldElement);

        SceneTextElement t1 = (SceneTextElement) elements.get(0);
        assertEquals("First Text", t1.getText(gameState));

        SceneImageElement i1 = (SceneImageElement) elements.get(1);
        ImageIcon icon1 = i1.getImageIcon(gameState);
        assertEquals(50, icon1.getIconWidth());
        assertEquals(50, icon1.getIconHeight());

        SceneChoiceElement c1 = (SceneChoiceElement) elements.get(2);
        assertEquals("Choice 1", c1.getChoice().label);

        SceneTextElement t2 = (SceneTextElement) elements.get(3);
        assertEquals("Second Text", t2.getText(gameState));

        SceneImageElement i2 = (SceneImageElement) elements.get(4);
        ImageIcon icon2 = i2.getImageIcon(gameState);
        assertEquals(100, icon2.getIconWidth());
        assertEquals(100, icon2.getIconHeight());

        SceneFieldElement f1 = (SceneFieldElement) elements.get(5);
        assertEquals("Input here", f1.getField().placeholder);
    }

    @Test
    public void testConditionalImageRendering() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

        Scene scene = new Scene("condScene");
        scene.imageIfFlag("hasMap", img)
             .imageIfFlagFalse("hasMap", img)
             .imageIfVarEquals("chapter", 2, img)
             .imageIfVarNotEqual("chapter", 2, img)
             .imageIfHasItem("lantern", img)
             .imageIfDoesntHaveItem("lantern", img);

        assertEquals(6, scene.elements.size());

        // Initially hasMap is false, chapter is null, lantern is absent
        assertFalse(scene.elements.get(0).isVisible(gameState));
        assertTrue(scene.elements.get(1).isVisible(gameState));
        assertFalse(scene.elements.get(2).isVisible(gameState));
        assertTrue(scene.elements.get(3).isVisible(gameState));
        assertFalse(scene.elements.get(4).isVisible(gameState));
        assertTrue(scene.elements.get(5).isVisible(gameState));

        // Update state
        gameState.setFlag("hasMap", true);
        gameState.setVar("chapter", 2);
        gameState.giveItem("lantern", 1);

        assertTrue(scene.elements.get(0).isVisible(gameState));
        assertFalse(scene.elements.get(1).isVisible(gameState));
        assertTrue(scene.elements.get(2).isVisible(gameState));
        assertFalse(scene.elements.get(3).isVisible(gameState));
        assertTrue(scene.elements.get(4).isVisible(gameState));
        assertFalse(scene.elements.get(5).isVisible(gameState));
    }

    @Test
    public void testDynamicImageSource() {
        BufferedImage imgA = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imgB = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);

        Scene scene = new Scene("dynamicScene");
        scene.image(state -> state.getFlag("useBig") ? imgB : imgA);

        SceneImageElement elem = (SceneImageElement) scene.elements.get(0);
        assertEquals(30, elem.getImageIcon(gameState).getIconWidth());

        gameState.setFlag("useBig", true);
        assertEquals(60, elem.getImageIcon(gameState).getIconWidth());
    }

    @Test
    public void testUIRenderingWithImagesInOrder() {
        BufferedImage img = new BufferedImage(150, 100, BufferedImage.TYPE_INT_ARGB);

        LullabyGame engine = new LullabyGame();
        engine.game.story.scene("test")
                .title("Test Title")
                .text("Paragraph 1")
                .image(img)
                .choice("Choice A", "next")
                .text("Paragraph 2");

        UI ui = new UI(engine);
        ui.init();
        ui.goToScene("test");

        JPanel scenePanel = ui.getScenePanel();
        assertNotNull(scenePanel);

        // Verify components inside scene panel
        Component[] components = scenePanel.getComponents();
        // title (0), rigid (1), text1 (2), rigid (3), image (4), rigid (5), choice (6), rigid (7), text2 (8), rigid (9)
        assertTrue(components.length >= 8);

        // Find the image component (JLabel with ImageIcon)
        JLabel imgLabel = null;
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel l = (JLabel) comp;
                if (l.getIcon() instanceof ImageIcon) {
                    imgLabel = l;
                    break;
                }
            }
        }

        assertNotNull(imgLabel);
        ImageIcon icon = (ImageIcon) imgLabel.getIcon();
        assertEquals(150, icon.getIconWidth());
        assertEquals(100, icon.getIconHeight());
    }

    @Test
    public void testImageSqueezeFitWithinWindow() {
        // Very large image (e.g. 2000x1500)
        BufferedImage hugeImg = new BufferedImage(2000, 1500, BufferedImage.TYPE_INT_ARGB);
        ImageIcon hugeIcon = new ImageIcon(hugeImg);

        ImageIcon fitted = ImageUtils.fitImage(hugeIcon, 500, 400);
        assertNotNull(fitted);
        assertTrue(fitted.getIconWidth() <= 500, "Width should be <= 500 but was " + fitted.getIconWidth());
        assertTrue(fitted.getIconHeight() <= 400, "Height should be <= 400 but was " + fitted.getIconHeight());
        // Aspect ratio was 2000/1500 = 4/3 = 1.3333. For maxWidth=500, maxHeight=400:
        // 500/2000 = 0.25 (scaled height: 375 <= 400).
        assertEquals(500, fitted.getIconWidth());
        assertEquals(375, fitted.getIconHeight());

        // Test with UI rendering
        LullabyGame engine = new LullabyGame();
        engine.game.gameData.setWindowSizeX(800).setWindowSizeY(600);
        engine.game.story.scene("oversizedScene")
                .title("Big Image Scene")
                .image(hugeImg);

        UI ui = new UI(engine);
        ui.init();
        ui.goToScene("oversizedScene");

        JPanel scenePanel = ui.getScenePanel();
        assertNotNull(scenePanel);

        JLabel imgLabel = null;
        for (Component comp : scenePanel.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getIcon() instanceof ImageIcon) {
                imgLabel = (JLabel) comp;
                break;
            }
        }

        assertNotNull(imgLabel);
        ImageIcon renderedIcon = (ImageIcon) imgLabel.getIcon();
        int maxAllowedWidth = ui.calculateMaxImageWidth();
        int maxAllowedHeight = ui.calculateMaxImageHeight();
        assertTrue(renderedIcon.getIconWidth() <= maxAllowedWidth);
        assertTrue(renderedIcon.getIconHeight() <= maxAllowedHeight);
    }
}
