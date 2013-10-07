package notepad.controller.event;

import notepad.controller.ControllerEvent;
import notepad.controller.adapter.Type;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MouseEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(MouseEvent.class);


    private Type type;
    private java.awt.event.MouseEvent event;

    public MouseEvent(Type type, java.awt.event.MouseEvent event) {
        this.type = type;
        this.event = event;
    }

    public java.awt.event.MouseEvent getEvent() {
        return event;
    }

    public Type getType() {

        return type;
    }
}
