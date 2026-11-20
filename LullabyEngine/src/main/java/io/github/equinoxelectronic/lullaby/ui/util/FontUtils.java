package io.github.equinoxelectronic.lullaby.ui.util;

import javax.swing.*;
import java.awt.*;

public class FontUtils {
    public static final String DEFAULT_FONT_NAME = "Segoe UI";

    public static JLabel applyDefaultFont(JLabel label) {
        return applyDefaultFont(label, 14);
    }

    public static JLabel applyDefaultFont(JLabel label, int size) {
        label.setFont(new Font(DEFAULT_FONT_NAME, Font.PLAIN, size));
        return label;
    }

    public static JLabel applyBoldFont(JLabel label, int size) {
        label.setFont(new Font(DEFAULT_FONT_NAME, Font.BOLD, size));
        return label;
    }

    public static Font getFont(int style, int size) {
        return new Font(DEFAULT_FONT_NAME, style, size);
    }
}
