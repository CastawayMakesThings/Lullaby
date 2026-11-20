package io.github.equinoxelectronic.lullaby.gamedata;

import io.github.equinoxelectronic.lullaby.LullabyGame;

import java.util.ArrayList;

public class GameData {
    public  String gameName = "Lullaby Game";
    public  String gameVersion = "v0.0.1";
    public  String gameSaveFileVersion = "v0.0.1";
    public  String gameAuthor = "John Smith";
    public  String startingSceneName = "EMPTY";
    public  String iconPath = "icon.png";
    public boolean shouldDisplayTime = true;
    public int gameWindowSizeX = 800;
    public int gameWindowSizeY = 600;

    //Automatically adds a prefix to scene names when creating or accessing one.
    public  String useScenePrefixByDefault = "false";

    public  ArrayList<String> excludedScenesFromRecords = new ArrayList<>();public GameData setName(String name) {
        this.gameName = name;
        return this;
    }

    public GameData setVersion(String version) {
        this.gameVersion = version;
        return this;
    }

    public GameData setSaveFileVersion(String saveFileVersion) {
        this.gameSaveFileVersion = saveFileVersion;
        return this;
    }

    public GameData setAuthor(String author) {
        this.gameAuthor = author;
        return this;
    }

    public GameData setStartingSceneName(String sceneName) {
        this.startingSceneName = sceneName;
        return this;
    }

    public GameData setIconPath(String iconPath) {
        this.iconPath = iconPath;
        return this;
    }

    public GameData setShouldDisplayTime(boolean shouldDisplayTime) {
        this.shouldDisplayTime = shouldDisplayTime;
        return this;
    }

    public GameData setWindowSizeX(int windowSizeX) {
        this.gameWindowSizeX = windowSizeX;
        return this;
    }

    public GameData setWindowSizeY(int windowSizeY) {
        this.gameWindowSizeY = windowSizeY;
        return this;
    }
}
