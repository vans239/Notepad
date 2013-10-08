package notepad.controller;

import notepad.NotepadException;
import notepad.text.TextModel;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface ControllerListener {
    void actionPerformed(final NotepadController controller, final TextModel textModel, final ControllerEvent event) throws NotepadException;
}
