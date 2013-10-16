package notepad.text.model;

import notepad.NotepadException;
import notepad.text.ChangeTextEvent;
import notepad.text.ChangeTextListener;
import notepad.text.TextModel;
import notepad.text.event.DeleteEvent;
import notepad.text.event.InsertEvent;
import notepad.text.event.ReplaceEvent;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */

public abstract class AbstractTextModel implements TextModel {
    private static final Logger log = Logger.getLogger(AbstractTextModel.class);

    /*
       Methods without logging
    */
    public abstract void doInsert(long pos, String s) throws NotepadException;

    public abstract void doReplace(long pos, String s) throws NotepadException;

    public abstract void doRemove(long pos, int length) throws NotepadException;

    @Override
    public void insert(long pos, String s) throws NotepadException {
        doInsert(pos, s);
        notifyChangeTextListeners(new InsertEvent(pos, s));
    }

    @Override
    public void replace(long pos, String s) throws NotepadException {
        final String replaced = get(pos, s.length());
        doReplace(pos, s);
        notifyChangeTextListeners(new ReplaceEvent(pos, replaced, s));
    }

    @Override
    public void remove(long pos, int length) throws NotepadException {
        final String removed = get(pos, length);
        doRemove(pos, length);
        notifyChangeTextListeners(new DeleteEvent(pos, removed));
    }

    private final List<ChangeTextListener> changeTextListeners = new ArrayList<ChangeTextListener>();

    @Override
    public void addChangeTextListener(ChangeTextListener listener) {
        changeTextListeners.add(listener);
    }

    @Override
    public void removeChangeTextListener(ChangeTextListener listener) {
        changeTextListeners.remove(listener);
    }

    protected void notifyChangeTextListeners(final ChangeTextEvent event) {
        for (final ChangeTextListener listener : changeTextListeners) {
            listener.actionPerformed(event);
        }
    }
}
