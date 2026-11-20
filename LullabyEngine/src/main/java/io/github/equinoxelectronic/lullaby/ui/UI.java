package io.github.equinoxelectronic.lullaby.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;
import io.github.equinoxelectronic.lullaby.content.Game;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.content.SceneChoiceElement;
import io.github.equinoxelectronic.lullaby.content.SceneElement;
import io.github.equinoxelectronic.lullaby.content.SceneFieldElement;
import io.github.equinoxelectronic.lullaby.content.SceneImageElement;
import io.github.equinoxelectronic.lullaby.content.SceneTextElement;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.ui.util.ImageUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class UI {
    public static final Color CONTENT_BG = new Color(24, 24, 27);
    public static final Color TEXT_PRIMARY = new Color(245, 245, 248);
    public static final Color TEXT_BODY = new Color(220, 220, 225);

    private final LullabyGame engine;
    private JFrame window;
    private JPanel leftPanel;
    private JPanel scenePanel;

    public UI(LullabyGame engine) {
        this.engine = engine;
    }

    public JFrame init() {
        if (UIManager.getLookAndFeel() == null || !UIManager.getLookAndFeel().getName().contains("FlatLaf")) {
            try {
                FlatDarkLaf.setup();
            } catch (Exception ignored) {
            }
        }

        window = new JFrame();
        int width = engine != null && engine.game != null && engine.game.gameData != null ? engine.game.gameData.gameWindowSizeX : 800;
        int height = engine != null && engine.game != null && engine.game.gameData != null ? engine.game.gameData.gameWindowSizeY : 600;
        String title = engine != null && engine.game != null && engine.game.gameData != null ? engine.game.gameData.gameName : "Lullaby";

        window.setSize(width, height);
        window.setTitle(title);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(CONTENT_BG);

        leftPanel = SideBox.getVBox(engine);
        rootPanel.add(leftPanel, BorderLayout.WEST);

        scenePanel = new JPanel();
        scenePanel.setLayout(new BoxLayout(scenePanel, BoxLayout.Y_AXIS));
        scenePanel.setBackground(CONTENT_BG);
        scenePanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JScrollPane scrollPane = new JScrollPane(scenePanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(CONTENT_BG);
        scrollPane.getViewport().setBackground(CONTENT_BG);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        window.add(rootPanel);
        return window;
    }

    public void updateSidebar() {
        if (leftPanel != null) {
            SideBox.populateVBox(leftPanel, engine);
        }
    }

    public void goToScene(String sceneId) {
        if (engine == null || engine.game == null || engine.game.story == null) return;
        Scene scene = engine.game.story.getScene(sceneId);
        if (scene != null) {
            displayScene(scene);
        }
    }

    public void displayScene(Scene scene) {
        if (scene == null || scenePanel == null) return;

        if (engine != null && engine.gameState != null) {
            engine.gameState.setCurrentSceneID(scene.id);
        }

        // Run all enter actions for this scene
        for (Consumer<GameState> action : scene.enterActions) {
            if (action != null && engine != null) {
                action.accept(engine.gameState);
            }
        }

        scenePanel.removeAll();

        // 1. Add Title
        if (scene.titleText != null && !scene.titleText.isEmpty()) {
            JLabel titleLabel = new JLabel(scene.titleText);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
            titleLabel.setForeground(TEXT_PRIMARY);
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            scenePanel.add(titleLabel);
            scenePanel.add(Box.createRigidArea(new Dimension(0, 16)));
        }

        GameState state = engine != null ? engine.gameState : null;

        // Render ordered elements if defined
        if (!scene.elements.isEmpty()) {
            for (SceneElement elem : scene.elements) {
                if (elem == null || !elem.isVisible(state)) {
                    continue;
                }

                if (elem instanceof SceneTextElement) {
                    String line = ((SceneTextElement) elem).getText(state);
                    if (line != null && !line.isEmpty()) {
                        JLabel textLabel = new JLabel("<html><body style='width: 100%;'>" + line.replace("\n", "<br>") + "</body></html>");
                        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                        textLabel.setForeground(TEXT_BODY);
                        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        scenePanel.add(textLabel);
                        scenePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                } else if (elem instanceof SceneImageElement) {
                    ImageIcon icon = ((SceneImageElement) elem).getImageIcon(state);
                    if (icon != null && icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                        int maxImgWidth = calculateMaxImageWidth();
                        int maxImgHeight = calculateMaxImageHeight();
                        ImageIcon fittedIcon = ImageUtils.fitImage(icon, maxImgWidth, maxImgHeight);
                        JLabel imgLabel = new JLabel(fittedIcon);
                        imgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                        imgLabel.setMaximumSize(new Dimension(fittedIcon.getIconWidth(), fittedIcon.getIconHeight()));
                        imgLabel.setPreferredSize(new Dimension(fittedIcon.getIconWidth(), fittedIcon.getIconHeight()));
                        scenePanel.add(imgLabel);
                        scenePanel.add(Box.createRigidArea(new Dimension(0, 12)));
                    }
                } else if (elem instanceof SceneChoiceElement) {
                    Choice choice = ((SceneChoiceElement) elem).getChoice();
                    if (choice != null && choice.isVisible(state)) {
                        JButton choiceBtn = createStyledChoiceButton(choice, state);
                        scenePanel.add(choiceBtn);
                        scenePanel.add(Box.createRigidArea(new Dimension(0, 8)));
                    }
                } else if (elem instanceof SceneFieldElement) {
                    Field field = ((SceneFieldElement) elem).getField();
                    if (field != null && field.isVisible(state)) {
                        JPanel fieldRow = createStyledFieldRow(field);
                        scenePanel.add(fieldRow);
                        scenePanel.add(Box.createRigidArea(new Dimension(0, 8)));
                    }
                }
            }
        } else {
            // Legacy fallback if elements was not populated
            for (Function<GameState, String> textGen : scene.textGenerators) {
                String line = textGen.apply(state);
                if (line != null && !line.isEmpty()) {
                    JLabel textLabel = new JLabel("<html><body style='width: 100%;'>" + line.replace("\n", "<br>") + "</body></html>");
                    textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                    textLabel.setForeground(TEXT_BODY);
                    textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    scenePanel.add(textLabel);
                    scenePanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            if (!scene.choices.isEmpty()) {
                scenePanel.add(Box.createRigidArea(new Dimension(0, 6)));
                for (Choice choice : scene.choices) {
                    if (choice == null || !choice.isVisible(state)) {
                        continue;
                    }
                    JButton choiceBtn = createStyledChoiceButton(choice, state);
                    scenePanel.add(choiceBtn);
                    scenePanel.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }

            if (!scene.fields.isEmpty()) {
                scenePanel.add(Box.createRigidArea(new Dimension(0, 6)));
                for (Field field : scene.fields) {
                    if (field == null || !field.isVisible(state)) {
                        continue;
                    }
                    JPanel fieldRow = createStyledFieldRow(field);
                    scenePanel.add(fieldRow);
                    scenePanel.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }
        }

        updateSidebar();
        scenePanel.revalidate();
        scenePanel.repaint();
    }

    private JButton createStyledChoiceButton(Choice choice, GameState state) {
        String label = choice.getLabel(state);
        JButton choiceBtn = new JButton(label);
        choiceBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        choiceBtn.setForeground(TEXT_PRIMARY);
        choiceBtn.setBackground(new Color(45, 48, 55));
        choiceBtn.setFocusPainted(false);
        choiceBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        choiceBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        choiceBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        choiceBtn.setPreferredSize(new Dimension(280, 38));
        choiceBtn.setHorizontalAlignment(SwingConstants.LEFT);
        choiceBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 64, 73), 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        choiceBtn.putClientProperty("JButton.buttonType", "roundRect");
        choiceBtn.addActionListener(e -> {
            if (engine != null && engine.gameState != null) {
                choice.execute(engine.gameState);
            }
            if (choice.targetSceneId != null) {
                goToScene(choice.targetSceneId);
            }
        });
        return choiceBtn;
    }

    private JPanel createStyledFieldRow(Field field) {
        JPanel fieldRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        fieldRow.setOpaque(false);
        fieldRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JTextField input = new JTextField(16);
        input.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        input.setPreferredSize(new Dimension(220, 36));
        input.setBackground(new Color(36, 38, 43));
        input.setForeground(TEXT_PRIMARY);
        input.setCaretColor(TEXT_PRIMARY);
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 64, 73), 1, true),
                new EmptyBorder(4, 10, 4, 10)
        ));
        if (field.placeholder != null) {
            input.setToolTipText(field.placeholder);
            input.putClientProperty("JTextField.placeholderText", field.placeholder);
        }

        JButton submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submitBtn.setForeground(TEXT_PRIMARY);
        submitBtn.setBackground(new Color(55, 65, 81));
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitBtn.setPreferredSize(new Dimension(90, 36));
        submitBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 85, 105), 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        submitBtn.putClientProperty("JButton.buttonType", "roundRect");

        Runnable submitAction = () -> {
            String text = input.getText();
            GameState state = engine != null ? engine.gameState : null;
            if (state != null) {
                field.submit(state, text);
            }
            String targetScene = field.resolveTargetSceneId(state, text);
            if (targetScene != null) {
                goToScene(targetScene);
            }
        };

        submitBtn.addActionListener(e -> submitAction.run());
        input.addActionListener(e -> submitAction.run());

        fieldRow.add(input);
        fieldRow.add(submitBtn);
        return fieldRow;
    }

    public JFrame getWindow() {
        return window;
    }

    public JPanel getScenePanel() {
        return scenePanel;
    }

    public JPanel getLeftPanel() {
        return leftPanel;
    }

    public int calculateMaxImageWidth() {
        int winWidth = (window != null && window.getWidth() > 0) ? window.getWidth()
                : (engine != null && engine.game != null && engine.game.gameData != null ? engine.game.gameData.gameWindowSizeX : 800);
        int sideWidth = (leftPanel != null && leftPanel.getWidth() > 0) ? leftPanel.getWidth()
                : (leftPanel != null && leftPanel.getPreferredSize().width > 0 ? leftPanel.getPreferredSize().width : 200);
        int paddingAndMargins = 70 + 30; // 35px left + 35px right padding + 30px buffer for scrollbars & borders
        int maxWidth = winWidth - sideWidth - paddingAndMargins;
        return Math.max(100, maxWidth);
    }

    public int calculateMaxImageHeight() {
        int winHeight = (window != null && window.getHeight() > 0) ? window.getHeight()
                : (engine != null && engine.game != null && engine.game.gameData != null ? engine.game.gameData.gameWindowSizeY : 600);
        int contentHeight = (window != null && window.getContentPane() != null && window.getContentPane().getHeight() > 0)
                ? window.getContentPane().getHeight()
                : winHeight;
        int maxHeight = contentHeight - 140;
        return Math.max(100, maxHeight);
    }

    public static JFrame getUI(Game game, GameState gameState) {
        LullabyGame dummyEngine = new LullabyGame();
        dummyEngine.game = game;
        dummyEngine.gameState = gameState;
        UI ui = new UI(dummyEngine);
        return ui.init();
    }
}
