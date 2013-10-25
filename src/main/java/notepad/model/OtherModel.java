package notepad.model;

import notepad.view.Mode;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class OtherModel {
    private static final Logger log = Logger.getLogger(OtherModel.class);
    private boolean isShowSelection = false;
    private boolean isEdited = false;
    private Mode mode = Mode.INSERT;
    private boolean isReverted = false;

    public void setReverted(boolean reverted) {
        isReverted = reverted;
    }

    public boolean isReverted() {
        return isReverted;
    }

    public void setShowSelection(boolean showSelection) {
        isShowSelection = showSelection;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean isShowSelection() {

        return isShowSelection;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public Mode getMode() {
        return mode;
    }

    public void swapMode(){
        mode = mode.swap();
    }
}
