package notepad.controller.event;

import notepad.controller.ControllerEvent;
import notepad.controller.adapter.MouseType;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MouseEvent implements ControllerEvent {
    private MouseType type;
    private java.awt.event.MouseEvent event;

    public MouseEvent(MouseType type, java.awt.event.MouseEvent event) {
        this.type = type;
        this.event = event;
    }

    public java.awt.event.MouseEvent getEvent() {
        return event;
    }

    public MouseType getType() {
        return type;
    }
}
