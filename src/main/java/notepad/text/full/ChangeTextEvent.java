package notepad.text.full;

import notepad.NotepadException;
import notepad.text.full.TextModel;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface ChangeTextEvent {
    void apply(final TextModel textModel) throws NotepadException;

    void revert(final TextModel textModel) throws NotepadException;
}
