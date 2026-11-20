package io.github.equinoxelectronic.lullaby.ui;

import javax.swing.*;
import java.awt.*;

public class SideBoxButton extends JButton {
    public SideBoxButton(String text, Runnable action) {
        super(text);
        this.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        this.setFocusPainted(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        this.setPreferredSize(new Dimension(130, 34));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.putClientProperty("JButton.buttonType", "roundRect");
        this.addActionListener(e -> {
            if (action != null) {
                action.run();
            }
        });
    }
}
