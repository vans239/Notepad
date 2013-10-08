package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.PatchEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class PatchListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(PatchListener.class);

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof PatchEvent) {
            PatchEvent pe = (PatchEvent) event;
            if (pe.getPatchType() == PatchEvent.PatchType.REDO) {
                pe.getEvent().apply(textModel);
            } else {
                pe.getEvent().revert(textModel);
            }
        }
    }
}
