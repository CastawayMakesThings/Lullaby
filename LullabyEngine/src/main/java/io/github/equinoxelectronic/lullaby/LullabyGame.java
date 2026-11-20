package io.github.equinoxelectronic.lullaby;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.equinoxelectronic.lullaby.content.Game;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.ui.UI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.File;
import java.io.IOException;

public class LullabyGame {
    //Program identifiers
    public static final String VERSION = "v0.0.2-ALPHA";
    public static final String NAME = "Lullaby";

    public JFrame window = null;
    public Game game = new Game();
    public GameState gameState = new GameState();
    public UI ui;
    private String startingSceneId;

    public Scene scene(String id) {
        return game.scene(id);
    }

    public void goToScene(String sceneId) {
        this.startingSceneId = sceneId;
        if (gameState != null) {
            gameState.setCurrentSceneID(sceneId);
        }
        if (ui != null) {
            ui.goToScene(sceneId);
        }
    }

    public void refreshUI() {
        if (ui != null) {
            ui.updateSidebar();
        }
    }

    public void setGameState(GameState newGameState) {
        if (newGameState == null) return;
        this.gameState = newGameState;
        if (this.game != null && this.game.gameData != null) {
            this.gameState.init(this.game.gameData.gameName);
        }
        this.gameState.addChangeListener(this::refreshUI);
        if (ui != null) {
            if (gameState.getCurrentSceneID() != null && game.story.getScene(gameState.getCurrentSceneID()) != null) {
                ui.goToScene(gameState.getCurrentSceneID());
            } else {
                refreshUI();
            }
        }
    }

    public void save(File file) throws IOException {
        if (gameState != null) {
            gameState.save(file);
        }
    }

    public void save(String filePath) throws IOException {
        save(new File(filePath));
    }

    public void save() throws IOException {
        String safeName = (game != null && game.gameData != null && game.gameData.gameName != null)
                ? game.gameData.gameName.replaceAll("[^a-zA-Z0-9.-]", "_")
                : "save";
        save(new File("saves", safeName + ".sav"));
    }

    public void saveToFile(File file) throws IOException {
        save(file);
    }

    public void saveToFile(String filePath) throws IOException {
        save(filePath);
    }

    public void load(File file) throws IOException, ClassNotFoundException {
        GameState loaded = GameState.load(file);
        setGameState(loaded);
    }

    public void load(String filePath) throws IOException, ClassNotFoundException {
        load(new File(filePath));
    }

    public void load() throws IOException, ClassNotFoundException {
        String safeName = (game != null && game.gameData != null && game.gameData.gameName != null)
                ? game.gameData.gameName.replaceAll("[^a-zA-Z0-9.-]", "_")
                : "save";
        load(new File("saves", safeName + ".sav"));
    }

    public void loadFromFile(File file) throws IOException, ClassNotFoundException {
        load(file);
    }

    public void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
        load(filePath);
    }

    public void saveWithDialog(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        String safeName = (game != null && game.gameData != null && game.gameData.gameName != null)
                ? game.gameData.gameName.replaceAll("[^a-zA-Z0-9.-]", "_")
                : "save";
        fileChooser.setSelectedFile(new File(safeName + ".sav"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Save Files (*.sav)", "sav"));
        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            if (!selected.getName().contains(".")) {
                selected = new File(selected.getAbsolutePath() + ".sav");
            }
            try {
                save(selected);
                JOptionPane.showMessageDialog(parent, "Game saved successfully!", "Save Game", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Failed to save game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadWithDialog(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Save Files (*.sav)", "sav"));
        int userSelection = fileChooser.showOpenDialog(parent);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            try {
                load(selected);
                JOptionPane.showMessageDialog(parent, "Game loaded successfully!", "Load Game", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Failed to load game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void start() {
        FlatDarkLaf.setup();

        gameState.init(game.gameData.gameName);

        Lullaby.engines.put(game.gameData.gameName, this);

        // Automatically update the UI whenever gameState changes
        gameState.addChangeListener(this::refreshUI);

        SwingUtilities.invokeLater(() -> {
            ui = new UI(this);
            window = ui.init();
            window.setVisible(true);

            // Determine which scene to show first
            String sceneToDisplay = startingSceneId;
            if (sceneToDisplay == null || game.story.getScene(sceneToDisplay) == null) {
                String configuredStart = game.gameData.startingSceneName;
                if (configuredStart != null && game.story.getScene(configuredStart) != null) {
                    sceneToDisplay = configuredStart;
                } else if (gameState.getCurrentSceneID() != null && game.story.getScene(gameState.getCurrentSceneID()) != null) {
                    sceneToDisplay = gameState.getCurrentSceneID();
                } else if (!game.story.scenes.isEmpty()) {
                    sceneToDisplay = game.story.scenes.keySet().iterator().next();
                }
            }

            if (sceneToDisplay != null) {
                ui.goToScene(sceneToDisplay);
            }
        });
    }
}
