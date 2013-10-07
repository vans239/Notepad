package notepad.text.event;

import notepad.text.ChangeTextEvent;
import notepad.NotepadException;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class InsertEvent implements ChangeTextEvent {
    private static final Logger log = Logger.getLogger(InsertEvent.class);

    private String inserted;
    private long pos;

    public InsertEvent(long pos, String inserted) {
        this.inserted = inserted;
        this.pos = pos;
    }

    @Override
    public void apply(TextModel textModel) {
        try {
            textModel.insert(pos, inserted);
        } catch (NotepadException e) {
            log.error("", e);
        }
    }

    @Override
    public void revert(TextModel textModel) {
        try {
            textModel.remove(pos, inserted.length());
        } catch (NotepadException e) {
            log.error("", e);
        }
    }

    @Override
    public String toString() {
        return "InsertEvent{" +
                "inserted='" + inserted + '\'' +
                ", pos=" + pos +
                '}';
    }

    public String getInserted() {
        return inserted;
    }

    public long getPos() {
        return pos;
    }
}
