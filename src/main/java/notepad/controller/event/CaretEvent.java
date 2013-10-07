package notepad.controller.event;

import notepad.controller.ControllerEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class CaretEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(CaretEvent.class);
    private int shiftCaret;

    public CaretEvent(int shiftCaret) {
        this.shiftCaret = shiftCaret;
    }

    public int getShiftCaret() {
        return shiftCaret;
    }

    @Override
    public String toString() {
        return "CaretEvent{" +
                "shiftCaret=" + shiftCaret +
                '}';
    }
}
