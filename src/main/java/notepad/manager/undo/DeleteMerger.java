package notepad.manager.undo;

import notepad.manager.Patch;
import notepad.text.full.ChangeTextEvent;
import notepad.text.full.event.DeleteEvent;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class DeleteMerger implements Merger<Patch> {
    @Override
    public boolean isMergeable(final Patch last,final Patch before) {
        final ChangeTextEvent lastEvent = last.getCte();
        final ChangeTextEvent beforeEvent = before.getCte();

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
    public Patch merge(final Patch last,final  Patch before) {
        final ChangeTextEvent lastEvent = last.getCte();
        final ChangeTextEvent beforeEvent = before.getCte();

        if (!(lastEvent instanceof DeleteEvent) || !(beforeEvent instanceof DeleteEvent)) {
            throw new IllegalArgumentException();
        }
        DeleteEvent lastDE = (DeleteEvent) lastEvent;
        DeleteEvent beforeDE = (DeleteEvent) beforeEvent;
        if (lastDE.getPos() == beforeDE.getPos()) {
            return new Patch(before.getContextBefore(), last.getContextAfter(), new DeleteEvent(lastDE.getPos(), beforeDE.getDeleted() + lastDE.getDeleted()));
        }
        if (lastDE.getPos() + lastDE.getDeleted().length() == beforeDE.getPos()) {
            return new Patch(before.getContextBefore(), last.getContextAfter(), new DeleteEvent(lastDE.getPos(), lastDE.getDeleted() + beforeDE.getDeleted()));
        }
        throw new IllegalArgumentException();
    }
}
