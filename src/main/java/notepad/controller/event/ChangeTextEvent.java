package notepad.controller.event;

import notepad.controller.ControllerEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ChangeTextEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(ChangeTextEvent.class);
    private notepad.text.ChangeTextEvent changeTextEvent;

    public ChangeTextEvent(notepad.text.ChangeTextEvent changeTextEvent) {
        this.changeTextEvent = changeTextEvent;
    }

    public notepad.text.ChangeTextEvent getChangeTextEvent() {
        return changeTextEvent;
    }

    @Override
    public String toString() {
        return "ChangeTextEvent{" +
                "changeTextEvent=" + changeTextEvent +
                '}';
    }
}
