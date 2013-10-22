package notepad.text.event;

import notepad.NotepadException;
import notepad.text.ChangeTextEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class InsertEvent implements ChangeTextEvent {
    private String inserted;
    private long pos;

    public InsertEvent(long pos, String inserted) {
        this.inserted = inserted;
        this.pos = pos;
    }

    @Override
    public void apply(TextModel textModel) throws NotepadException {
        textModel.insert(pos, inserted);
    }

    @Override
    public void revert(TextModel textModel) throws NotepadException {
        textModel.remove(pos, inserted.length());
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
