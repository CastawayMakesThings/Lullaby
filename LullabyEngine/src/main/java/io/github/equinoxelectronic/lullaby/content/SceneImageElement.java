package io.github.equinoxelectronic.lullaby.content;

import io.github.equinoxelectronic.lullaby.gamedata.GameState;
import io.github.equinoxelectronic.lullaby.ui.util.ImageUtils;

import javax.swing.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SceneImageElement implements SceneElement {
    private final Object imageSource;
    private final Function<GameState, Object> dynamicImageSource;
    private final Predicate<GameState> condition;

    public SceneImageElement(Object imageSource) {
        this(null, imageSource);
    }

    public SceneImageElement(Predicate<GameState> condition, Object imageSource) {
        this.condition = condition;
        this.imageSource = imageSource;
        this.dynamicImageSource = null;
    }

    @SuppressWarnings("unchecked")
    public SceneImageElement(Function<GameState, Object> dynamicImageSource) {
        this(null, dynamicImageSource);
    }

    public SceneImageElement(Predicate<GameState> condition, Function<GameState, Object> dynamicImageSource) {
        this.condition = condition;
        this.imageSource = null;
        this.dynamicImageSource = dynamicImageSource;
    }

    @Override
    public boolean isVisible(GameState state) {
        if (condition != null && !condition.test(state)) {
            return false;
        }
        return true;
    }

    public Object resolveSource(GameState state) {
        if (dynamicImageSource != null) {
            return dynamicImageSource.apply(state);
        }
        return imageSource;
    }

    public ImageIcon getImageIcon(GameState state) {
        Object src = resolveSource(state);
        return ImageUtils.loadImage(src);
    }
}
