package notepad.manager.undo;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface Merger<T> {
    boolean isMergeable(final T last, final T before);

    T merge(final T last, final T before);
}
