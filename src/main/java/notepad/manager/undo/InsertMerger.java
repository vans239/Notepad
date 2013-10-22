package notepad.manager.undo;

import notepad.manager.Patch;
import notepad.text.ChangeTextEvent;
import notepad.text.event.InsertEvent;
import org.apache.log4j.Logger;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class InsertMerger implements Merger<Patch> {
    @Override
    public boolean isMergeable(Patch last, Patch before) {
        ChangeTextEvent lastEvent = last.getCte();
        ChangeTextEvent beforeEvent = before.getCte();

        if (!(lastEvent instanceof InsertEvent) || !(beforeEvent instanceof InsertEvent)) {
            return false;
        }
        InsertEvent lastIE = (InsertEvent) lastEvent;
        InsertEvent beforeIE = (InsertEvent) beforeEvent;
        if (isBlank(lastIE.getInserted()) || isBlank(beforeIE.getInserted())) {
            return false;
        }
        return lastIE.getPos() == beforeIE.getPos() + beforeIE.getInserted().length();
    }

    @Override
    public Patch merge(Patch last, Patch before) {
        ChangeTextEvent lastEvent = last.getCte();
        ChangeTextEvent beforeEvent = before.getCte();

        if (!(lastEvent instanceof InsertEvent) || !(beforeEvent instanceof InsertEvent)) {
            throw new IllegalArgumentException();
        }
        InsertEvent lastIE = (InsertEvent) lastEvent;
        InsertEvent beforeIE = (InsertEvent) beforeEvent;
        if (lastIE.getPos() == beforeIE.getPos() + beforeIE.getInserted().length()) {
            ChangeTextEvent patchEvent = new InsertEvent(beforeIE.getPos(), beforeIE.getInserted() + lastIE.getInserted());
            return new Patch(before.getContext(), patchEvent);
        }
        throw new IllegalArgumentException();
    }
}
