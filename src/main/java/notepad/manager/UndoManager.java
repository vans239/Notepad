package notepad.manager;

import notepad.manager.undo.Merger;
import notepad.text.ChangeTextEvent;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class UndoManager {
    private static final Logger log = Logger.getLogger(UndoManager.class);
    private final Stack<ChangeTextEvent> undos = new Stack<ChangeTextEvent>();   //todo maxCapacity
    private final Stack<ChangeTextEvent> redos = new Stack<ChangeTextEvent>();
    private List<Merger> mergers = new ArrayList<Merger>();

    public void add(ChangeTextEvent event) {
        redos.clear();
        undos.push(event);
        merge();
    }

    public ChangeTextEvent undo() {
        if (undos.isEmpty()) {
            return null;
        }
        final ChangeTextEvent event = undos.pop();
        redos.push(event);
        return event;
    }

    public void addMerger(final Merger merger) {
        mergers.add(merger);
    }

    public ChangeTextEvent redo() {
        if (redos.isEmpty()) {
            return null;
        }
        final ChangeTextEvent event = redos.pop();
        undos.push(event);
        return event;
    }


    private void merge() {
        boolean f = true;
        while (f) {
            f = false;
            for (Merger merger : mergers) {
                if (undos.size() >= 2) {
                    ChangeTextEvent last = undos.pop();
                    ChangeTextEvent before = undos.pop();
                    if (merger.isMergeable(last, before)) {
                        f = true;
                        ChangeTextEvent merged = merger.merge(last, before);
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
