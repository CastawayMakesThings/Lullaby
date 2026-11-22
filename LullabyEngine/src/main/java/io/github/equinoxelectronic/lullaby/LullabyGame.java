package io.github.equinoxelectronic.lullaby;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.equinoxelectronic.lullaby.content.Game;
import io.github.equinoxelectronic.lullaby.content.Scene;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.Inventory;
import io.github.equinoxelectronic.lullaby.gamedata.Item;
import io.github.equinoxelectronic.lullaby.gamedata.ItemClass;
import io.github.equinoxelectronic.lullaby.gamedata.Stat;
import io.github.equinoxelectronic.lullaby.mechanics.Time;
import io.github.equinoxelectronic.lullaby.ui.UI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LullabyGame {
    //Program identifiers
    public static final String VERSION = "v0.1.0";
    public static final String NAME = "Lullaby";

    public JFrame window = null;
    public Game game = new Game();
    public GameState gameState = new GameState();
    public UI ui;
    private String startingSceneId;

    // --- Configuration Facades (Fluent Builder Style) ---

    public LullabyGame name(String name) {
        this.game.gameData.setName(name);
        return this;
    }

    public LullabyGame setName(String name) {
        return name(name);
    }

    public String getName() {
        return this.game.gameData.gameName;
    }

    public LullabyGame title(String title) {
        return name(title);
    }

    public LullabyGame setTitle(String title) {
        return name(title);
    }

    public String getTitle() {
        return getName();
    }

    public LullabyGame saveFileVersion(String version) {
        this.game.gameData.setSaveFileVersion(version);
        return this;
    }

    public LullabyGame setSaveFileVersion(String version) {
        return saveFileVersion(version);
    }

    public String getSaveFileVersion() {
        return this.game.gameData.gameSaveFileVersion;
    }

    public LullabyGame version(String version) {
        return saveFileVersion(version);
    }

    public LullabyGame setVersion(String version) {
        return saveFileVersion(version);
    }

    public String getVersion() {
        return getSaveFileVersion();
    }

    public LullabyGame author(String author) {
        this.game.gameData.setAuthor(author);
        return this;
    }

    public LullabyGame setAuthor(String author) {
        return author(author);
    }

    public String getAuthor() {
        return this.game.gameData.gameAuthor;
    }

    public LullabyGame startingScene(String sceneName) {
        this.game.gameData.setStartingSceneName(sceneName);
        this.startingSceneId = sceneName;
        return this;
    }

    public LullabyGame setStartingScene(String sceneName) {
        return startingScene(sceneName);
    }

    public LullabyGame setStartingSceneName(String sceneName) {
        return startingScene(sceneName);
    }

    public String getStartingScene() {
        return this.game.gameData.startingSceneName;
    }

    public String getStartingSceneName() {
        return getStartingScene();
    }

    public LullabyGame icon(String iconPath) {
        this.game.gameData.setIconPath(iconPath);
        return this;
    }

    public LullabyGame iconPath(String iconPath) {
        return icon(iconPath);
    }

    public LullabyGame setIconPath(String iconPath) {
        return icon(iconPath);
    }

    public String getIconPath() {
        return this.game.gameData.iconPath;
    }

    public LullabyGame displayTime(boolean display) {
        this.game.gameData.setShouldDisplayTime(display);
        return this;
    }

    public LullabyGame shouldDisplayTime(boolean display) {
        return displayTime(display);
    }

    public LullabyGame setShouldDisplayTime(boolean display) {
        return displayTime(display);
    }

    public boolean isDisplayTime() {
        return this.game.gameData.shouldDisplayTime;
    }

    public boolean shouldDisplayTime() {
        return isDisplayTime();
    }

    public LullabyGame windowSize(int width, int height) {
        this.game.gameData.setWindowSizeX(width);
        this.game.gameData.setWindowSizeY(height);
        return this;
    }

    public LullabyGame setWindowSize(int width, int height) {
        return windowSize(width, height);
    }

    public LullabyGame windowSizeX(int width) {
        this.game.gameData.setWindowSizeX(width);
        return this;
    }

    public LullabyGame setWindowSizeX(int width) {
        return windowSizeX(width);
    }

    public int getWindowSizeX() {
        return this.game.gameData.gameWindowSizeX;
    }

    public LullabyGame windowSizeY(int height) {
        this.game.gameData.setWindowSizeY(height);
        return this;
    }

    public LullabyGame setWindowSizeY(int height) {
        return windowSizeY(height);
    }

    public int getWindowSizeY() {
        return this.game.gameData.gameWindowSizeY;
    }

    public LullabyGame useScenePrefixByDefault(String prefix) {
        this.game.gameData.setUseScenePrefixByDefault(prefix);
        return this;
    }

    public LullabyGame setUseScenePrefixByDefault(String prefix) {
        return useScenePrefixByDefault(prefix);
    }

    public String getUseScenePrefixByDefault() {
        return this.game.gameData.useScenePrefixByDefault;
    }

    public LullabyGame excludeSceneFromRecords(String sceneId) {
        this.game.gameData.excludeSceneFromRecords(sceneId);
        return this;
    }

    public boolean isSceneExcludedFromRecords(String sceneId) {
        return this.game.gameData.isSceneExcludedFromRecords(sceneId);
    }

    // --- Scene Management Facades ---

    public Scene scene(String id) {
        return game.scene(id);
    }

    public Scene scene(Scene scene) {
        game.story.addScene(scene);
        return scene;
    }

    public LullabyGame addScene(Scene scene) {
        game.story.addScene(scene);
        return this;
    }

    public Scene getScene(String id) {
        return game.story.getScene(id);
    }

    public boolean hasScene(String id) {
        return game.story.getScene(id) != null;
    }

    public Map<String, Scene> getScenes() {
        return game.story.scenes;
    }

    public Scene getCurrentScene() {
        String currentId = getCurrentSceneId();
        return currentId != null ? getScene(currentId) : null;
    }

    public String getCurrentSceneId() {
        return gameState != null ? gameState.getCurrentSceneID() : startingSceneId;
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

    // --- Game State: Variables, Flags, Counters, Strings Facades ---

    public LullabyGame setVar(String key, Object value) {
        if (gameState != null) {
            gameState.setVar(key, value);
        }
        return this;
    }

    public Object getVar(String key) {
        return gameState != null ? gameState.getVar(key) : null;
    }

    public <T> T getVar(String key, Class<T> type) {
        return gameState != null ? gameState.getVar(key, type) : null;
    }

    public <T> T getVar(String key, T defaultValue) {
        return gameState != null ? gameState.getVar(key, defaultValue) : defaultValue;
    }

    public boolean isVarEqual(String key, Object value) {
        return gameState != null && gameState.isVarEqual(key, value);
    }

    public boolean hasVar(String key) {
        return gameState != null && gameState.getVars().containsKey(key);
    }

    public Object removeVar(String key) {
        return gameState != null ? gameState.getVars().remove(key) : null;
    }

    public LullabyGame setFlag(String key, boolean value) {
        if (gameState != null) {
            gameState.setFlag(key, value);
        }
        return this;
    }

    public boolean getFlag(String key) {
        return gameState != null && gameState.getFlag(key);
    }

    public boolean toggleFlag(String key) {
        boolean newVal = !getFlag(key);
        setFlag(key, newVal);
        return newVal;
    }

    public LullabyGame setCounter(String key, int value) {
        if (gameState != null) {
            gameState.setCounter(key, value);
        }
        return this;
    }

    public int getCounter(String key) {
        return gameState != null ? gameState.getCounter(key) : 0;
    }

    public int incrementCounter(String key) {
        return gameState != null ? gameState.incrementCounter(key) : 0;
    }

    public int incrementCounter(String key, int amount) {
        return gameState != null ? gameState.incrementCounter(key, amount) : 0;
    }

    public int decrementCounter(String key) {
        return gameState != null ? gameState.decrementCounter(key) : 0;
    }

    public int decrementCounter(String key, int amount) {
        return gameState != null ? gameState.decrementCounter(key, amount) : 0;
    }

    public LullabyGame setString(String key, String value) {
        if (gameState != null) {
            gameState.setString(key, value);
        }
        return this;
    }

    public String getString(String key) {
        return gameState != null ? gameState.getString(key) : null;
    }

    public LullabyGame setPlayerName(String name) {
        if (gameState != null) {
            gameState.setPlayerName(name);
        }
        return this;
    }

    public String getPlayerName() {
        return gameState != null ? gameState.getPlayerName() : null;
    }

    // --- Stats Facades ---

    public LullabyGame addStat(Stat stat) {
        if (gameState != null) {
            gameState.addStat(stat);
        }
        return this;
    }

    public LullabyGame addStat(String id, String displayName, int initialValue) {
        if (gameState != null) {
            gameState.addStat(id, displayName, initialValue);
        }
        return this;
    }

    public LullabyGame setStat(String id, int value) {
        if (gameState != null) {
            gameState.setStatValue(id, value);
        }
        return this;
    }

    public LullabyGame setStatValue(String id, int value) {
        return setStat(id, value);
    }

    public LullabyGame modifyStat(String id, int delta) {
        if (gameState != null) {
            gameState.modifyStat(id, delta);
        }
        return this;
    }

    public Stat getStat(String id) {
        return gameState != null ? gameState.getStat(id) : null;
    }

    public int getStatValue(String id) {
        return gameState != null ? gameState.getStatValue(id) : 0;
    }

    public boolean hasStat(String id) {
        return gameState != null && gameState.getStats().containsKey(id);
    }

    public Map<String, Stat> getStats() {
        return gameState != null ? gameState.getStats() : null;
    }

    // --- Item & Inventory Facades ---

    public LullabyGame registerItem(ItemClass itemClass) {
        if (gameState != null && gameState.getInventory() != null) {
            gameState.getInventory().registerItemClass(itemClass);
        }
        return this;
    }

    public LullabyGame registerItemClass(ItemClass itemClass) {
        return registerItem(itemClass);
    }

    public LullabyGame registerItem(String id, String displayName, String description) {
        return registerItem(new ItemClass(id, displayName, description));
    }

    public ItemClass getItemClass(String id) {
        if (gameState != null && gameState.getInventory() != null) {
            return gameState.getInventory().getGameItems().get(id);
        }
        return null;
    }

    public LullabyGame giveItem(Item item) {
        if (gameState != null) {
            gameState.giveItem(item);
        }
        return this;
    }

    public LullabyGame giveItem(String classPointer) {
        if (gameState != null) {
            gameState.giveItem(classPointer);
        }
        return this;
    }

    public LullabyGame giveItem(String classPointer, int quantity) {
        if (gameState != null) {
            gameState.giveItem(classPointer, quantity);
        }
        return this;
    }

    public LullabyGame addItem(String classPointer, int quantity) {
        return giveItem(classPointer, quantity);
    }

    public LullabyGame removeItem(Item item) {
        if (gameState != null) {
            gameState.removeItem(item);
        }
        return this;
    }

    public LullabyGame removeItem(String classPointer) {
        if (gameState != null) {
            gameState.removeItem(classPointer);
        }
        return this;
    }

    public LullabyGame removeItem(String classPointer, int quantity) {
        if (gameState != null) {
            gameState.removeItem(classPointer, quantity);
        }
        return this;
    }

    public LullabyGame setItem(String classPointer, int quantity) {
        if (gameState != null && gameState.getInventory() != null) {
            gameState.getInventory().setItem(classPointer, quantity);
            refreshUI();
        }
        return this;
    }

    public boolean hasItem(String classPointer) {
        return gameState != null && gameState.hasItem(classPointer);
    }

    public boolean hasItem(String classPointer, int quantity) {
        return gameState != null && gameState.hasItem(classPointer, quantity);
    }

    public int getItemQuantity(String classPointer) {
        return gameState != null ? gameState.getItemQuantity(classPointer) : 0;
    }

    public Inventory getInventory() {
        return gameState != null ? gameState.getInventory() : null;
    }

    // --- Time Facades ---

    public LullabyGame setTime(Time time) {
        if (gameState != null) {
            gameState.setGameTime(time);
        }
        return this;
    }

    public LullabyGame setTime(long seconds) {
        return setTime(new Time(seconds));
    }

    public LullabyGame advanceTime(long seconds) {
        if (gameState != null) {
            gameState.advanceTime(seconds);
        }
        return this;
    }

    public LullabyGame addTime(long seconds) {
        return advanceTime(seconds);
    }

    public Time getTime() {
        return gameState != null ? gameState.getGameTime() : null;
    }

    public Time getGameTime() {
        return getTime();
    }

    public String getFormattedTime() {
        Time time = getTime();
        return time != null ? time.getFormattedTime() : "";
    }

    // --- History Facades ---

    public int getSceneVisitCount(String sceneId) {
        return gameState != null ? gameState.getSceneVisitCount(sceneId) : 0;
    }

    public int getChoiceSelectedCount(String choiceLabel) {
        return gameState != null ? gameState.getChoiceSelectedCount(choiceLabel) : 0;
    }

    // --- UI and Lifecycle ---

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

    public void showInventoryDialog(Component parent) {
        io.github.equinoxelectronic.lullaby.ui.InventoryDialog.show(parent != null ? parent : window, gameState, game != null ? game.gameData : null);
    }

    public void showInventoryDialog() {
        showInventoryDialog(window);
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
