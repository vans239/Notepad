package notepad.controller;

import notepad.NotepadException;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class NotepadController {
    private static final Logger log = Logger.getLogger(NotepadController.class);

    private List<ControllerListener> listeners = new ArrayList<ControllerListener>();
    private TextModel textModel;
    private ControllerTextListener controllerTextListener = new ControllerTextListener(this);

    public long length() throws NotepadException {
        return textModel.length();
    }

    public String get(long pos, int length) throws NotepadException {
        return textModel.get(pos, length);
    }

    public void addChangeTextListener(ControllerListener listener) {
        listeners.add(listener);
    }

    public void removeChangeTextListener(ControllerListener listener) {
        listeners.remove(listener);
    }

    public void fireControllerEvent(final ControllerEvent event) {
        log.debug(event.toString());
        final TextModel currTextModel = textModel;
        for (final ControllerListener listener : listeners) {
            try {
                listener.actionPerformed(this, currTextModel, event);
            } catch (NotepadException e) {
                log.error("Unexpected behaviour. Program will be closed now", e);
                System.exit(0);
            }
        }
    }

    public void setTextModel(TextModel textModel) {
        if (this.textModel != null) {
            this.textModel.removeChangeTextListener(controllerTextListener);
        }
        this.textModel = textModel;
        this.textModel.addChangeTextListener(controllerTextListener);
    }
}
