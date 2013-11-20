package notepad.utils.observe;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface Observer<T> {
    void update(final Observable<T> observable,final T t);
}
