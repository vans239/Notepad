package notepad.manager.undo;

import notepad.text.ChangeTextEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface Merger<T> {
    boolean isMergeable(final T last, final T before);

    T merge(final T last, final T before);
}
