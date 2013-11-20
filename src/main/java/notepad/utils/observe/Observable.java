package notepad.utils.observe;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface Observable<T> {
    void addObserver(@NotNull final Observer<T> observer );
    void remove(@NotNull final Observer observer);
    void notifyObservers(@Nullable final T t);
    void notifyObservers();
}
