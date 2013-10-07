package notepad.controller.event;

import notepad.controller.ControllerEvent;
import notepad.controller.adapter.Type;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class KeyboardEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(KeyboardEvent.class);
    private KeyEvent keyEvent;
    private Type type;

    public KeyboardEvent(Type type, KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
        this.type = type;

    }

    public Type getType() {
        return type;
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    @Override
    public String toString() {
        return "KeyboardEvent{" +
                "keyEvent=" + keyEvent +
                ", type=" + type +
                '}';
    }
}
