package notepad.controller.event;

import notepad.controller.ControllerEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class CaretEvent implements ControllerEvent {
    private Number value;
    private CaretEventType type;

    public CaretEvent(CaretEventType type, Number value) {
        this.type = type;
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    public CaretEventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "CaretEvent{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }

    public static enum CaretEventType {
        GOTO, SHIFT
    }
}
