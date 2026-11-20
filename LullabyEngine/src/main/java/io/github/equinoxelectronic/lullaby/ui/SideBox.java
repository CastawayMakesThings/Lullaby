package io.github.equinoxelectronic.lullaby.ui;

import io.github.equinoxelectronic.lullaby.Lullaby;
import io.github.equinoxelectronic.lullaby.LullabyGame;
import io.github.equinoxelectronic.lullaby.gamedata.GameData;
import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.gamedata.Stat;
import io.github.equinoxelectronic.lullaby.ui.util.FontUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

//The entire Side panel that displays Game Name, stats, time, and inventory, save, load, and quit buttons.
public class SideBox {
    public static final Color SIDEBAR_BG = new Color(30, 31, 34);
    public static final Color SIDEBAR_BORDER = new Color(45, 47, 52);

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
        vbox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, SIDEBAR_BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));
        vbox.setBackground(SIDEBAR_BG);
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
        vbox.setBackground(SIDEBAR_BG);

        //Title label
        if (gameData != null && gameData.gameName != null) {
            JLabel title = new JLabel(gameData.gameName);
            title.setFont(new Font("Segoe UI", Font.BOLD, 18));
            title.setForeground(new Color(245, 245, 248));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            vbox.add(title);
            vbox.add(Box.createRigidArea(new Dimension(0, 12)));

            JSeparator sep = new JSeparator();
            sep.setForeground(new Color(50, 53, 58));
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            sep.setAlignmentX(Component.LEFT_ALIGNMENT);
            vbox.add(sep);
            vbox.add(Box.createRigidArea(new Dimension(0, 14)));
        }

        //Stats
        if (gameState != null && gameState.getStats() != null && !gameState.getStats().isEmpty()) {
            JLabel statsHeader = new JLabel("STATS");
            statsHeader.setFont(new Font("Segoe UI", Font.BOLD, 11));
            statsHeader.setForeground(new Color(135, 140, 150));
            statsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            vbox.add(statsHeader);
            vbox.add(Box.createRigidArea(new Dimension(0, 6)));

            for (Stat stat : gameState.getStats().values()) {
                JPanel statRow = new JPanel(new BorderLayout());
                statRow.setOpaque(false);
                statRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
                statRow.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel nameLabel = new JLabel(stat.getDisplayName());
                nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                nameLabel.setForeground(new Color(205, 208, 215));

                JLabel valueLabel = new JLabel(String.valueOf(stat.getValue()));
                valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
                valueLabel.setForeground(new Color(96, 165, 250));

                statRow.add(nameLabel, BorderLayout.WEST);
                statRow.add(valueLabel, BorderLayout.EAST);
                vbox.add(statRow);
                vbox.add(Box.createRigidArea(new Dimension(0, 4)));
            }
            vbox.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        //Display time if enabled
        if (gameData != null && gameData.shouldDisplayTime && gameState != null && gameState.getGameTime() != null) {
            JLabel t = new JLabel(gameState.getGameTime().getFormattedTime());
            t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            t.setForeground(new Color(158, 163, 172));
            t.setAlignmentX(Component.LEFT_ALIGNMENT);
            vbox.add(t);
            vbox.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        //Add the Buttons on the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(SIDEBAR_BG);
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Make the buttons stay at the bottom.
        vbox.add(Box.createVerticalGlue());

        //Inventory button
        bottomPanel.add(new SideBoxButton("Inventory", () -> {
            LullabyGame targetEngine = engine != null ? engine : findEngine(gameState, gameData);
            GameState targetState = targetEngine != null ? targetEngine.gameState : gameState;
            GameData targetData = targetEngine != null && targetEngine.game != null ? targetEngine.game.gameData : gameData;
            InventoryDialog.show(vbox, targetState, targetData);
        }));
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        //Save button
        bottomPanel.add(new SideBoxButton("Save", () -> {
            LullabyGame targetEngine = engine != null ? engine : findEngine(gameState, gameData);
            if (targetEngine != null) {
                targetEngine.saveWithDialog(vbox);
            }
        }));
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        //Load button
        bottomPanel.add(new SideBoxButton("Load", () -> {
            LullabyGame targetEngine = engine != null ? engine : findEngine(gameState, gameData);
            if (targetEngine != null) {
                targetEngine.loadWithDialog(vbox);
            }
        }));
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        //Quit button
        bottomPanel.add(new SideBoxButton("Quit", () -> System.exit(0)));

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
