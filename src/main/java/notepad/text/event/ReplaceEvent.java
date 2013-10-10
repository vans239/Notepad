package notepad.text.event;

import notepad.NotepadException;
import notepad.text.ChangeTextEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ReplaceEvent implements ChangeTextEvent {
    private static final Logger log = Logger.getLogger(ReplaceEvent.class);

    private String beforeStr;
    private String afterString;
    private long pos;

    public ReplaceEvent(long pos, String beforeStr, String afterString) {
        this.beforeStr = beforeStr;
        this.afterString = afterString;
        if (beforeStr.length() != afterString.length()) {
            throw new IllegalArgumentException("Lengths of strings should be equal for replace event.May it's problem with encoding(only 1-byte)");
        }
        this.pos = pos;
    }

    @Override
    public void apply(TextModel textModel) throws NotepadException {
        textModel.replace(pos, afterString);
    }

    @Override
    public void revert(TextModel textModel) throws NotepadException {
        textModel.replace(pos, beforeStr);
    }

    @Override
    public String toString() {
        return "ReplaceEvent{" +
                "beforeStr='" + beforeStr + '\'' +
                ", afterString='" + afterString + '\'' +
                ", pos=" + pos +
                '}';
    }
}
