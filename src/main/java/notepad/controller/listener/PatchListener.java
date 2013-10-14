package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.PatchEvent;
import notepad.manager.Context;
import notepad.text.ChangeTextEvent;
import notepad.text.TextModel;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class PatchListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(PatchListener.class);
    private NotepadView view;

    public PatchListener(NotepadView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof PatchEvent) {
            PatchEvent pe = (PatchEvent) event;
            final Context context = pe.getPatch().getContext();
            view.updateCaretGoTo(context.getCaretPosition());

            if (pe.getPatchType() == PatchEvent.PatchType.REDO) {
                pe.getPatch().getCte().apply(textModel);
            } else {
                pe.getPatch().getCte().revert(textModel);
            }
        }
    }
}
