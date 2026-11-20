package io.github.equinoxelectronic.lullaby.ui.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class ImageUtils {

    public static ImageIcon loadImage(Object source) {
        if (source == null) return null;
        if (source instanceof ImageIcon) {
            return (ImageIcon) source;
        }
        if (source instanceof Image) {
            return new ImageIcon((Image) source);
        }
        if (source instanceof File) {
            File f = (File) source;
            if (f.exists() && f.isFile()) {
                try {
                    BufferedImage img = ImageIO.read(f);
                    if (img != null) {
                        return new ImageIcon(img);
                    }
                } catch (Exception ignored) {
                }
                return new ImageIcon(f.getAbsolutePath());
            }
            return null;
        }
        if (source instanceof URL) {
            try {
                return new ImageIcon((URL) source);
            } catch (Exception ignored) {
                return null;
            }
        }
        if (source instanceof String) {
            String path = ((String) source).trim();
            if (path.isEmpty()) return null;

            // 1. Check if it's an existing file on the filesystem
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                try {
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        return new ImageIcon(img);
                    }
                } catch (Exception ignored) {
                }
                return new ImageIcon(file.getAbsolutePath());
            }

            // 2. Try loading as classpath resource
            String resPath = path.startsWith("/") ? path : "/" + path;
            URL url = ImageUtils.class.getResource(resPath);
            if (url == null) {
                String alt = path.startsWith("/") ? path.substring(1) : path;
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if (cl != null) {
                    url = cl.getResource(alt);
                }
                if (url == null) {
                    url = ImageUtils.class.getClassLoader().getResource(alt);
                }
            }
            if (url != null) {
                try {
                    return new ImageIcon(url);
                } catch (Exception ignored) {
                }
            }

            // 3. Try reading as resource stream
            try (InputStream is = ImageUtils.class.getResourceAsStream(resPath)) {
                if (is != null) {
                    BufferedImage img = ImageIO.read(is);
                    if (img != null) {
                        return new ImageIcon(img);
                    }
                }
            } catch (Exception ignored) {
            }

            // 4. Try loading as URI / URL
            try {
                URI uri = URI.create(path);
                if (uri.isAbsolute()) {
                    return new ImageIcon(uri.toURL());
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static ImageIcon fitImage(ImageIcon original, int maxWidth, int maxHeight) {
        if (original == null) return null;
        int origW = original.getIconWidth();
        int origH = original.getIconHeight();
        if (origW <= 0 || origH <= 0) return original;
        if (maxWidth <= 0 || maxHeight <= 0) return original;

        if (origW <= maxWidth && origH <= maxHeight) {
            return original;
        }

        double scaleW = (double) maxWidth / origW;
        double scaleH = (double) maxHeight / origH;
        double scale = Math.min(scaleW, scaleH);

        int targetW = Math.max(1, (int) Math.round(origW * scale));
        int targetH = Math.max(1, (int) Math.round(origH * scale));

        BufferedImage scaled = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(original.getImage(), 0, 0, targetW, targetH, null);
        g2.dispose();

        return new ImageIcon(scaled);
    }
}
