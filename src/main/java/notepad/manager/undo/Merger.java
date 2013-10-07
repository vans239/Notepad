package notepad.manager.undo;

import notepad.text.ChangeTextEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface Merger {
    boolean isMergeable(final ChangeTextEvent last, final ChangeTextEvent before);
    ChangeTextEvent merge(final ChangeTextEvent last, final ChangeTextEvent before);
}
