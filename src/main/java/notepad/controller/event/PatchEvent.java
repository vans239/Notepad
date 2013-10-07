package notepad.controller.event;

import notepad.controller.ControllerEvent;
import notepad.text.ChangeTextEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class PatchEvent implements ControllerEvent {
    private static final Logger log = Logger.getLogger(PatchEvent.class);
    private PatchType patchType;
    private ChangeTextEvent event;

    public PatchEvent(PatchType patchType, ChangeTextEvent event) {

        this.patchType = patchType;
        this.event = event;
    }

    public PatchType getPatchType() {
        return patchType;
    }

    public ChangeTextEvent getEvent() {
        return event;
    }

    public enum PatchType {
        UNDO, REDO
    }

}
