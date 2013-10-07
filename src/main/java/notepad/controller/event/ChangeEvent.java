package notepad.controller.event;

import notepad.controller.ControllerEvent;
import notepad.text.ChangeTextEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ChangeEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(ChangeEvent.class);
    private ChangeTextEvent changeTextEvent;

    public ChangeEvent(ChangeTextEvent changeTextEvent) {
        this.changeTextEvent = changeTextEvent;
    }

    public ChangeTextEvent getChangeTextEvent() {
        return changeTextEvent;
    }

    @Override
    public String toString() {
        return "ChangeEvent{" +
                "changeTextEvent=" + changeTextEvent +
                '}';
    }
}
