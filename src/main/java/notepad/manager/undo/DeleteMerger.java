package notepad.manager.undo;

import notepad.text.ChangeTextEvent;
import notepad.text.event.DeleteEvent;
import org.apache.log4j.Logger;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class DeleteMerger implements Merger {
    private static final Logger log = Logger.getLogger(DeleteMerger.class);

    @Override
    public boolean isMergeable(ChangeTextEvent last, ChangeTextEvent before) {
        if (!(last instanceof DeleteEvent) || !(before instanceof DeleteEvent)) {
            return false;
        }
        DeleteEvent lastDE = (DeleteEvent) last;
        DeleteEvent beforeDE = (DeleteEvent) before;
        if (isBlank(lastDE.getDeleted()) || isBlank(beforeDE.getDeleted())) {
            return false;
        }

        return lastDE.getPos() == beforeDE.getPos() || lastDE.getPos() + beforeDE.getDeleted().length() == beforeDE.getPos();
    }

    @Override
    public ChangeTextEvent merge(ChangeTextEvent last, ChangeTextEvent before) {
        if (!(last instanceof DeleteEvent) || !(before instanceof DeleteEvent)) {
            throw new IllegalArgumentException();
        }
        DeleteEvent lastDE = (DeleteEvent) last;
        DeleteEvent beforeDE = (DeleteEvent) before;
        if (lastDE.getPos() == beforeDE.getPos()) {
            return new DeleteEvent(lastDE.getPos(), beforeDE.getDeleted() + lastDE.getDeleted());
        }
        if (lastDE.getPos() + beforeDE.getDeleted().length() == beforeDE.getPos()) {
            return new DeleteEvent(lastDE.getPos(), lastDE.getDeleted() + beforeDE.getDeleted());
        }
        throw new IllegalArgumentException();
    }
}
