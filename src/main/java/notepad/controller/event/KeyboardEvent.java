package notepad.controller.event;

import notepad.controller.ControllerEvent;
import notepad.controller.adapter.KeyboardType;
import notepad.controller.adapter.MouseType;

import java.awt.event.KeyEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class KeyboardEvent implements ControllerEvent {
    private KeyEvent keyEvent;
    private KeyboardType type;

    public KeyboardEvent(KeyboardType type, KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
        this.type = type;

    }

    public KeyboardType getType() {
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
