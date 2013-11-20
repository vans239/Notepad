package notepad.utils.observe;


import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ObservableImpl<T> implements Observable<T>{
    private List<Observer<T>> observers = new ArrayList<Observer<T>>();

    public void addObserver(@NotNull final Observer<T> observer ){
        observers.add(observer);
    }

    public void remove(@NotNull final Observer observer){
        observers.remove(observer);
    }

    public void notifyObservers(@Nullable final T t) {
        for(final Observer<T> observer : observers){
            observer.update(this, t);
        }
    }

    public void notifyObservers() {
        for(final Observer<T> observer : observers){
            observer.update(this, null);
        }
    }
}
