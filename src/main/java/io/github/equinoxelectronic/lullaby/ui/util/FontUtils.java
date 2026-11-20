package io.github.equinoxelectronic.lullaby.ui.util;

import javax.swing.*;
import java.awt.*;

public class FontUtils {
    public static JLabel applyDefaultFont(JLabel label) {
        label.setFont(new Font("Serif", Font.PLAIN, 14));
        return label;
    }

    public static JLabel applyDefaultFont(JLabel label, int size) {
        label.setFont(new Font("Serif", Font.PLAIN, size));
        return label;
    }
}
