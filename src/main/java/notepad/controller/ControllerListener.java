package notepad.controller;

import notepad.NotepadException;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface ControllerListener {
    void actionPerformed(final NotepadController controller, final TextModel textModel, final ControllerEvent event) throws NotepadException;
}
