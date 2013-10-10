package notepad.text;

import notepad.NotepadException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface ChangeTextEvent {
    void apply(final TextModel textModel) throws NotepadException;

    void revert(final TextModel textModel) throws NotepadException;
}
