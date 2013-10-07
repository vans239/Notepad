package notepad.controller.listener;

import notepad.NotepadException;
import notepad.view.NotepadView;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class UpdateListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(UpdateListener.class);
    private NotepadView notepadView;

    public UpdateListener(NotepadView notepadView) {
        this.notepadView = notepadView;
    }

    @Override
    public void actionPerformed(final NotepadController controller, final TextModel textModel, final ControllerEvent event) throws NotepadException {
        notepadView.update();
    }
}
