package notepad.manager;

import notepad.manager.undo.Merger;
import notepad.utils.BoundedStack;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class UndoManager<T> {
    private static final Logger log = Logger.getLogger(UndoManager.class);
    private int MAX_CAPACITY = 1024;
    private final BoundedStack<T> undos = new BoundedStack<T>(MAX_CAPACITY);
    private final BoundedStack<T> redos = new BoundedStack<T>(MAX_CAPACITY);
    private List<Merger<T>> mergers = new ArrayList<Merger<T>>();

    public void add(T t) {
        redos.clear();
        undos.push(t);
        merge();
    }

    public T undo() {
        if (undos.isEmpty()) {
            return null;
        }
        final T t = undos.pop();
        redos.push(t);
        return t;
    }

    public void addMerger(final Merger<T> merger) {
        mergers.add(merger);
    }

    public T redo() {
        if (redos.isEmpty()) {
            return null;
        }
        final T t = redos.pop();
        undos.push(t);
        return t;
    }


    private void merge() {
        boolean f = true;
        while (f) {
            f = false;
            for (Merger<T> merger : mergers) {
                if (undos.size() >= 2) {
                    T last = undos.pop();
                    T before = undos.pop();
                    if (merger.isMergeable(last, before)) {
                        f = true;
                        T merged = merger.merge(last, before);
                        undos.push(merged);
                    } else {
                        undos.push(before);
                        undos.push(last);
                    }
                }
            }
        }
    }
}
