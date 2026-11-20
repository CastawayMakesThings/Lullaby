package io.github.equinoxelectronic.lullaby.ui;

import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.content.Choice;
import io.github.equinoxelectronic.lullaby.content.Field;
import io.github.equinoxelectronic.lullaby.content.Game;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class UI {
    private final LullabyGame engine;
    private JFrame window;
    private JPanel leftPanel;
    private JPanel scenePanel;

    public UI(LullabyGame engine) {
        this.engine = engine;
    }

    public JFrame init() {
        window = new JFrame();
        window.setSize(engine.game.gameData.gameWindowSizeX, engine.game.gameData.gameWindowSizeY);
        window.setTitle(engine.game.gameData.gameName);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel(new BorderLayout());

        leftPanel = SideBox.getVBox(engine);
        rootPanel.add(leftPanel, BorderLayout.WEST);

        scenePanel = new JPanel();
        scenePanel.setLayout(new BoxLayout(scenePanel, BoxLayout.Y_AXIS));
        scenePanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JScrollPane scrollPane = new JScrollPane(scenePanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
        Scene scene = engine.game.story.getScene(sceneId);
        if (scene != null) {
            displayScene(scene);
        }
    }

    public void displayScene(Scene scene) {
        if (scene == null || scenePanel == null) return;

        engine.gameState.setCurrentSceneID(scene.id);

        // Run all enter actions for this scene
        for (Consumer<GameState> action : scene.enterActions) {
            if (action != null) {
                action.accept(engine.gameState);
            }
        }

        scenePanel.removeAll();

        // 1. Add Title
        if (scene.titleText != null && !scene.titleText.isEmpty()) {
            JLabel titleLabel = new JLabel(scene.titleText);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            scenePanel.add(titleLabel);
            scenePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        // 2. Add Dynamic and Static Text
        for (Function<GameState, String> textGen : scene.textGenerators) {
            String line = textGen.apply(engine.gameState);
            if (line != null && !line.isEmpty()) {
                JLabel textLabel = new JLabel("<html>" + line.replace("\n", "<br>") + "</html>");
                textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                scenePanel.add(textLabel);
                scenePanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        // 3. Add Choices
        if (!scene.choices.isEmpty()) {
            scenePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            for (Choice choice : scene.choices) {
                if (!choice.isVisible(engine.gameState)) {
                    continue;
                }
                String label = choice.getLabel(engine.gameState);
                JButton choiceBtn = new JButton(label);
                choiceBtn.addActionListener(e -> {
                    choice.execute(engine.gameState);
                    if (choice.targetSceneId != null) {
                        goToScene(choice.targetSceneId);
                    }
                });
                scenePanel.add(choiceBtn);
                scenePanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        // 4. Add Input Fields
        if (!scene.fields.isEmpty()) {
            scenePanel.add(Box.createRigidArea(new Dimension(0, 10)));
            for (Field field : scene.fields) {
                if (!field.isVisible(engine.gameState)) {
                    continue;
                }
                JPanel fieldRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                JTextField input = new JTextField(15);
                if (field.placeholder != null) {
                    input.setToolTipText(field.placeholder);
                }
                JButton submitBtn = new JButton("Submit");
                submitBtn.addActionListener(e -> {
                    String text = input.getText();
                    field.submit(engine.gameState, text);
                    String targetScene = field.resolveTargetSceneId(engine.gameState, text);
                    if (targetScene != null) {
                        goToScene(targetScene);
                    }
                });
                fieldRow.add(input);
                fieldRow.add(submitBtn);
                scenePanel.add(fieldRow);
                scenePanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        updateSidebar();
        scenePanel.revalidate();
        scenePanel.repaint();
    }

    public JFrame getWindow() {
        return window;
    }

    public static JFrame getUI(Game game, GameState gameState) {
        LullabyGame dummyEngine = new LullabyGame();
        dummyEngine.game = game;
        dummyEngine.gameState = gameState;
        UI ui = new UI(dummyEngine);
        return ui.init();
    }
}
