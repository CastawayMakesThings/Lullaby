package io.github.equinoxelectronic.lullaby.ui;

import io.github.equinoxelectronic.lullaby.Lullaby;
import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.gamedata.GameData;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.Stat;
import io.github.equinoxelectronic.lullaby.ui.util.FontUtils;

import javax.swing.*;
import java.awt.*;

//The entire Side panel that displays Game Name, stats, time, and save, load, and quit buttons.
public class SideBox {
    public static JPanel getVBox(LullabyGame engine) {
        return getVBox(engine != null ? engine.gameState : null, engine != null && engine.game != null ? engine.game.gameData : null, engine);
    }

    public static JPanel getVBox(GameState gameState, GameData gameData) {
        return getVBox(gameState, gameData, null);
    }

    public static JPanel getVBox(GameState gameState, GameData gameData, LullabyGame engine) {
        //Create and configure the panel
        JPanel vbox = new JPanel();
        vbox.setLayout(new BoxLayout(vbox, BoxLayout.Y_AXIS));
        vbox.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        vbox.setBackground(new Color(67, 71, 67));
        populateVBox(vbox, gameState, gameData, engine);
        return vbox;
    }

    public static void populateVBox(JPanel vbox, LullabyGame engine) {
        populateVBox(vbox, engine != null ? engine.gameState : null, engine != null && engine.game != null ? engine.game.gameData : null, engine);
    }

    public static void populateVBox(JPanel vbox, GameState gameState, GameData gameData) {
        populateVBox(vbox, gameState, gameData, null);
    }

    public static void populateVBox(JPanel vbox, GameState gameState, GameData gameData, LullabyGame engine) {
        vbox.removeAll();

        //Title label
        if (gameData != null && gameData.gameName != null) {
            JLabel title = new JLabel(gameData.gameName);
            title = FontUtils.applyDefaultFont(title, 18);
            vbox.add(title);
            vbox.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        //Stats
        if (gameState != null && gameState.getStats() != null) {
            for (Stat stat : gameState.getStats().values()) {
                JLabel statLabel = new JLabel(stat.getDisplayName() + ": " + stat.getValue());
                statLabel = FontUtils.applyDefaultFont(statLabel);
                vbox.add(statLabel);
            }
        }

        vbox.add(Box.createRigidArea(new Dimension(0, 10)));

        //Display time if enabled
        if (gameData != null && gameData.shouldDisplayTime && gameState != null && gameState.getGameTime() != null) {
            JLabel t = new JLabel(gameState.getGameTime().getFormattedTime());
            t = FontUtils.applyDefaultFont(t, 10);
            vbox.add(t);
        }

        //Add the Buttons on the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        //Make the buttons go from bottom to top.
        bottomPanel.add(Box.createVerticalGlue());

        //Save button
        bottomPanel.add(new SideBoxButton("Save", () -> {
            LullabyGame targetEngine = engine != null ? engine : findEngine(gameState, gameData);
            if (targetEngine != null) {
                targetEngine.saveWithDialog(vbox);
            }
        }));
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        //Load button
        bottomPanel.add(new SideBoxButton("Load", () -> {
            LullabyGame targetEngine = engine != null ? engine : findEngine(gameState, gameData);
            if (targetEngine != null) {
                targetEngine.loadWithDialog(vbox);
            }
        }));
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        //Quit button
        bottomPanel.add(new SideBoxButton("Quit", () -> System.exit(0)));

        //Configure panel
        bottomPanel.setBackground(new Color(67, 71, 67));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        vbox.add(bottomPanel);

        vbox.revalidate();
        vbox.repaint();
    }

    private static LullabyGame findEngine(GameState gameState, GameData gameData) {
        if (gameData != null && gameData.gameName != null && Lullaby.engines.containsKey(gameData.gameName)) {
            return Lullaby.engines.get(gameData.gameName);
        }
        if (gameState != null && gameState.getOwner() != null && Lullaby.engines.containsKey(gameState.getOwner())) {
            return Lullaby.engines.get(gameState.getOwner());
        }
        if (!Lullaby.engines.isEmpty()) {
            return Lullaby.engines.values().iterator().next();
        }
        return null;
    }
}
