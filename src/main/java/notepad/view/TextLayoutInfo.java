package notepad.view;

import notepad.view.textlayout.SmartTextLayout;

import java.awt.*;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class TextLayoutInfo implements Cloneable {
    private final Point origin;
    private final SmartTextLayout layout;
    private final int position;

    public TextLayoutInfo(SmartTextLayout layout, Point origin, int position) {
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

    public SmartTextLayout getLayout() {
        return layout;
    }

    public TextLayoutInfo clone(){
        return new TextLayoutInfo(layout.clone(), origin, position);
    }
}
