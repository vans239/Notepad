package notepad.controller.event;

import notepad.controller.ControllerEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class CaretEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(CaretEvent.class);
    private int value;
    private CaretEventType type;

    public CaretEvent(CaretEventType type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public CaretEventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CaretEvent{" +
                "value=" + value +
                '}';
    }

    public static enum CaretEventType {
        GOTO, SHIFT
    }
}
