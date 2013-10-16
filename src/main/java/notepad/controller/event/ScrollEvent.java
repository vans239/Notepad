package notepad.controller.event;

import notepad.controller.ControllerEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ScrollEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(ScrollEvent.class);
    private Scroll scroll;
    private long viewPosition;

    public ScrollEvent(Scroll scroll, long viewPosition) {

        this.scroll = scroll;
        this.viewPosition = viewPosition;
    }

    public ScrollEvent(Scroll scroll) {
        this.scroll = scroll;
    }

    public long getViewPosition() {
        return viewPosition;
    }

    public Scroll getScroll() {
        return scroll;
    }

    @Override
    public String toString() {
        return "ScrollEvent{" +
                "scroll=" + scroll +
                '}';
    }

    public static enum Scroll {
        UP, DOWN, LEFT, RIGHT, GOTO
    }
}
