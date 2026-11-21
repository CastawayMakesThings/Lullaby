package io.github.equinoxelectronic.lullaby.gamedata;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public String gameName = "Lullaby Game";
    public String gameVersion = "v0.0.1";
    public String gameSaveFileVersion = "v0.0.1";
    public String gameAuthor = "John Smith";
    public String startingSceneName = "EMPTY";
    public String iconPath = "icon.png";
    public boolean shouldDisplayTime = true;
    public int gameWindowSizeX = 800;
    public int gameWindowSizeY = 600;

    //Automatically adds a prefix to scene names when creating or accessing one.
    public String useScenePrefixByDefault = "false";

    public ArrayList<String> excludedScenesFromRecords = new ArrayList<>();

    public GameData setName(String name) {
        this.gameName = name;
        return this;
    }

    public GameData name(String name) {
        return setName(name);
    }

    public GameData setVersion(String version) {
        this.gameVersion = version;
        return this;
    }

    public GameData version(String version) {
        return setVersion(version);
    }

    public GameData setSaveFileVersion(String saveFileVersion) {
        this.gameSaveFileVersion = saveFileVersion;
        return this;
    }

    public GameData saveFileVersion(String saveFileVersion) {
        return setSaveFileVersion(saveFileVersion);
    }

    public GameData setAuthor(String author) {
        this.gameAuthor = author;
        return this;
    }

    public GameData author(String author) {
        return setAuthor(author);
    }

    public GameData setStartingSceneName(String sceneName) {
        this.startingSceneName = sceneName;
        return this;
    }

    public GameData startingSceneName(String sceneName) {
        return setStartingSceneName(sceneName);
    }

    public GameData setIconPath(String iconPath) {
        this.iconPath = iconPath;
        return this;
    }

    public GameData iconPath(String iconPath) {
        return setIconPath(iconPath);
    }

    public GameData setShouldDisplayTime(boolean shouldDisplayTime) {
        this.shouldDisplayTime = shouldDisplayTime;
        return this;
    }

    public GameData shouldDisplayTime(boolean shouldDisplayTime) {
        return setShouldDisplayTime(shouldDisplayTime);
    }

    public GameData displayTime(boolean shouldDisplayTime) {
        return setShouldDisplayTime(shouldDisplayTime);
    }

    public GameData setWindowSizeX(int windowSizeX) {
        this.gameWindowSizeX = windowSizeX;
        return this;
    }

    public GameData windowSizeX(int windowSizeX) {
        return setWindowSizeX(windowSizeX);
    }

    public GameData setWindowSizeY(int windowSizeY) {
        this.gameWindowSizeY = windowSizeY;
        return this;
    }

    public GameData windowSizeY(int windowSizeY) {
        return setWindowSizeY(windowSizeY);
    }

    public GameData windowSize(int width, int height) {
        setWindowSizeX(width);
        setWindowSizeY(height);
        return this;
    }

    public GameData setWindowSize(int width, int height) {
        return windowSize(width, height);
    }

    public GameData setUseScenePrefixByDefault(String prefix) {
        this.useScenePrefixByDefault = prefix;
        return this;
    }

    public GameData useScenePrefixByDefault(String prefix) {
        return setUseScenePrefixByDefault(prefix);
    }

    public GameData excludeSceneFromRecords(String sceneId) {
        if (sceneId != null && !excludedScenesFromRecords.contains(sceneId)) {
            excludedScenesFromRecords.add(sceneId);
        }
        return this;
    }

    public boolean isSceneExcludedFromRecords(String sceneId) {
        return sceneId != null && excludedScenesFromRecords.contains(sceneId);
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public String getGameSaveFileVersion() {
        return gameSaveFileVersion;
    }

    public String getGameAuthor() {
        return gameAuthor;
    }

    public String getStartingSceneName() {
        return startingSceneName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public boolean isShouldDisplayTime() {
        return shouldDisplayTime;
    }

    public int getGameWindowSizeX() {
        return gameWindowSizeX;
    }

    public int getGameWindowSizeY() {
        return gameWindowSizeY;
    }

    public String getUseScenePrefixByDefault() {
        return useScenePrefixByDefault;
    }

    public List<String> getExcludedScenesFromRecords() {
        return excludedScenesFromRecords;
    }
}
