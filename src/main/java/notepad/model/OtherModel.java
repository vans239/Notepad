package notepad.model;

import notepad.text.full.ChangeTextEvent;
import notepad.text.full.ChangeTextListener;
import notepad.utils.ImmediateObservable;
import notepad.view.Mode;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;

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
    private int indent = 5;

    public final Observable swapObservable = new ImmediateObservable();
    public final Observable isEditObservable = new ImmediateObservable();

    public void setReverted(boolean reverted) {
        isReverted = reverted;
    }

    public int getIndent() {
        return indent;
    }

    public boolean isReverted() {
        return isReverted;
    }

    public void setShowSelection(boolean showSelection) {
        isShowSelection = showSelection;
    }

    public void setEdited(boolean edited) {
        if(edited != isEdited){
            isEdited = edited;
            isEditObservable.notifyObservers();
        }
    }

    public void setMode(Mode mode) {
        if(mode != this.mode){
            this.mode = mode;
            swapObservable.notifyObservers();
        }
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
        setMode(mode.swap());
    }

    public final ChangeTextListener editChangeTextListener = new ChangeTextListener() {
        @Override
        public void actionPerformed(ChangeTextEvent event) {
            setEdited(true);
        }
    };
}
