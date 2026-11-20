package io.github.equinoxelectronic.lullaby.gamedata;

import io.github.equinoxelectronic.lullaby.LullabyGame;

import java.util.ArrayList;

public class GameData {
    public  String gameName = "Lullaby Game";
    public  String gameVersion = LullabyGame.VERSION;
    public  String gameSaveFileVersion = "0.0.1";
    public  String gameAuthor = "John Smith";
    public  String menuSceneName = "#menu";
    public  String startingSceneName = "EMPTY";
    public  String iconPath = "icon.png";
    public  String defaultErrorSceneName = "#error";
    public boolean shouldDisplayTime = true;
    public int gameWindowSizeX = 800;
    public int gameWindowSizeY = 600;

    //Automatically adds a prefix to scene names when creating or accessing one.
    public  String useScenePrefixByDefault = "false";

    public  ArrayList<String> excludedScenesFromRecords = new ArrayList<>();
}
