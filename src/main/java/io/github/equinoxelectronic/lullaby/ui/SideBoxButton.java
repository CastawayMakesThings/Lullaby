package io.github.equinoxelectronic.lullaby.ui;

import javax.swing.*;

public class SideBoxButton extends JButton {
    public SideBoxButton(String text, Runnable action) {
        super(text);
        this.addActionListener(e -> action.run());
    }
}
