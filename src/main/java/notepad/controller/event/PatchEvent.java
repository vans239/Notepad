package notepad.controller.event;

import notepad.controller.ControllerEvent;
import notepad.manager.Patch;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class PatchEvent implements ControllerEvent {
    private PatchType patchType;
    private Patch patch;

    public PatchEvent(PatchType patchType, Patch patch) {

        this.patchType = patchType;
        this.patch = patch;
    }

    public PatchType getPatchType() {
        return patchType;
    }

    public Patch getPatch() {
        return patch;
    }

    public enum PatchType {
        UNDO, REDO
    }

}
