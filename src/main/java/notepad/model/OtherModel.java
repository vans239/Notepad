package notepad.model;

import notepad.text.full.ChangeTextEvent;
import notepad.text.full.ChangeTextListener;
import notepad.utils.observe.ObservableImpl;
import notepad.view.Mode;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Observable;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class OtherModel {
    private static final Logger log = Logger.getLogger(OtherModel.class);
    private boolean isEdited = false;
    private boolean isReverted = false;
    private Mode mode = Mode.INSERT;
    private final int textIndent = 5;
    private File file;

    public final ObservableImpl<Void> swapObservable = new ObservableImpl<Void>();
    public final ObservableImpl<Void> isEditObservable = new ObservableImpl<Void>();
    public final ObservableImpl<Void> fileObservable = new ObservableImpl<Void>();

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        fileObservable.notifyObservers();
    }

    public void setReverted(boolean reverted) {
        isReverted = reverted;
    }

    public int getTextIndent() {
        return textIndent;
    }

    public boolean isReverted() {
        return isReverted;
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
