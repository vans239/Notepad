package notepad.text.model;

import notepad.text.ChangeTextEvent;
import notepad.text.ChangeTextListener;
import notepad.NotepadException;
import notepad.text.TextModel;
import notepad.text.event.DeleteEvent;
import notepad.text.event.InsertEvent;
import notepad.text.event.ReplaceEvent;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public abstract class AbstractTextModel implements TextModel {
    private static final Logger log = Logger.getLogger(AbstractTextModel.class);

    protected abstract void _insert(long pos, String s) throws NotepadException;
    protected abstract void _replace(long pos, String s) throws NotepadException;
    protected abstract void _remove(long pos, int length) throws NotepadException;

    @Override
    public void insert(long pos, String s) throws NotepadException {
        _insert(pos, s);
        notifyChangeTextListeners(new InsertEvent(pos, s));
    }

    @Override
    public void replace(long pos, String s) throws NotepadException {
        final String replaced = get(pos, s.length());
        _replace(pos, s);
        notifyChangeTextListeners(new ReplaceEvent(pos, replaced, s));
    }

    @Override
    public void remove(long pos, int length) throws NotepadException {
        final String removed = get(pos, length);
        _remove(pos, length);
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

    protected void notifyChangeTextListeners(final ChangeTextEvent event){
        for(final ChangeTextListener listener : changeTextListeners){
            listener.actionPerformed(event);
        }
    }
}