package notepad.view;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.TextLayout;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class TextLayoutInfo {
    private final Point origin;
    private final TextLayout layout;
    private final int position;

    public TextLayoutInfo(TextLayout layout, Point origin, int position) {
        this.layout = layout;
        this.origin = origin;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public Point getOrigin() {
        return origin;
    }

    public TextLayout getLayout() {
        return layout;
    }
}
