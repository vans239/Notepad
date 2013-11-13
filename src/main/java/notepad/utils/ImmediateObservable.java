package notepad.utils;


import java.util.Observable;

public class ImmediateObservable extends Observable {
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
