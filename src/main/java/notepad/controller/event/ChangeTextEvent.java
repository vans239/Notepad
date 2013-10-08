package notepad.controller.event;

import notepad.controller.ControllerEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ChangeTextEvent implements ControllerEvent {
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
