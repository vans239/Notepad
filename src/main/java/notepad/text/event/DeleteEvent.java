package notepad.text.event;

import notepad.NotepadException;
import notepad.text.ChangeTextEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class DeleteEvent implements ChangeTextEvent {
    private static final Logger log = Logger.getLogger(DeleteEvent.class);

    private String deleted;
    private long pos;

    public DeleteEvent(long pos, String deleted) {
        this.deleted = deleted;
        this.pos = pos;
    }

    @Override
    public void apply(TextModel textModel) throws NotepadException {
        textModel.remove(pos, deleted.length());
    }

    @Override
    public String toString() {
        return "DeleteEvent{" +
                "deleted='" + deleted + '\'' +
                ", pos=" + pos +
                '}';
    }

    @Override
    public void revert(TextModel textModel) throws NotepadException {
        textModel.insert(pos, deleted);
    }

    public String getDeleted() {
        return deleted;
    }

    public long getPos() {
        return pos;
    }
}
