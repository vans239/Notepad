package notepad.manager.undo;

import notepad.text.ChangeTextEvent;
import notepad.text.event.InsertEvent;
import org.apache.log4j.Logger;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class InsertMerger implements Merger {
    private static final Logger log = Logger.getLogger(InsertMerger.class);

    @Override
    public boolean isMergeable(ChangeTextEvent last, ChangeTextEvent before) {
        if (!(last instanceof InsertEvent) || !(before instanceof InsertEvent)) {
            return false;
        }
        InsertEvent lastIE = (InsertEvent) last;
        InsertEvent beforeIE = (InsertEvent) before;
        if (isBlank(lastIE.getInserted()) || isBlank(beforeIE.getInserted())) {
            return false;
        }

        return lastIE.getPos() == beforeIE.getPos() + beforeIE.getInserted().length();
    }

    @Override
    public ChangeTextEvent merge(ChangeTextEvent last, ChangeTextEvent before) {
        if (!(last instanceof InsertEvent) || !(before instanceof InsertEvent)) {
            throw new IllegalArgumentException();
        }
        InsertEvent lastIE = (InsertEvent) last;
        InsertEvent beforeIE = (InsertEvent) before;
        if (lastIE.getPos() == beforeIE.getPos() + beforeIE.getInserted().length()) {
            return new InsertEvent(beforeIE.getPos(), beforeIE.getInserted() + lastIE.getInserted());
        }
        throw new IllegalArgumentException();
    }
}
