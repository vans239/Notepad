package notepad.manager.undo;

import notepad.manager.Patch;
import notepad.text.ChangeTextEvent;
import notepad.text.event.DeleteEvent;
import org.apache.log4j.Logger;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class DeleteMerger implements Merger<Patch> {
    private static final Logger log = Logger.getLogger(DeleteMerger.class);

    @Override
    public boolean isMergeable(Patch last, Patch before) {
        ChangeTextEvent lastEvent = last.getCte();
        ChangeTextEvent beforeEvent = before.getCte();

        if (!(lastEvent instanceof DeleteEvent) || !(beforeEvent instanceof DeleteEvent)) {
            return false;
        }
        DeleteEvent lastDE = (DeleteEvent) lastEvent;
        DeleteEvent beforeDE = (DeleteEvent) beforeEvent;
        if (isBlank(lastDE.getDeleted()) || isBlank(beforeDE.getDeleted())) {
            return false;
        }

        return lastDE.getPos() == beforeDE.getPos() || lastDE.getPos() + lastDE.getDeleted().length() == beforeDE.getPos();
    }

    @Override
    public Patch merge(Patch last, Patch before) {
        ChangeTextEvent lastEvent = last.getCte();
        ChangeTextEvent beforeEvent = before.getCte();

        if (!(lastEvent instanceof DeleteEvent) || !(beforeEvent instanceof DeleteEvent)) {
            throw new IllegalArgumentException();
        }
        DeleteEvent lastDE = (DeleteEvent) lastEvent;
        DeleteEvent beforeDE = (DeleteEvent) beforeEvent;
        if (lastDE.getPos() == beforeDE.getPos()) {
            return new Patch(before.getContext(), new DeleteEvent(lastDE.getPos(), beforeDE.getDeleted() + lastDE.getDeleted()));
        }
        if (lastDE.getPos() + lastDE.getDeleted().length() == beforeDE.getPos()) {
            return new Patch(before.getContext(), new DeleteEvent(lastDE.getPos(), lastDE.getDeleted() + beforeDE.getDeleted()));
        }
        throw new IllegalArgumentException();
    }
}
