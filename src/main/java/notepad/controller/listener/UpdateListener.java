package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.CaretEvent;
import notepad.controller.event.ChangeTextEvent;
import notepad.controller.event.ScrollEvent;
import notepad.text.TextModel;
import notepad.view.NotepadFrame;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class UpdateListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(UpdateListener.class);
    private NotepadView notepadView;
    private NotepadFrame notepadFrame;

    public UpdateListener(NotepadView notepadView, NotepadFrame notepadFrame) {
        this.notepadView = notepadView;
        this.notepadFrame = notepadFrame;
    }

    @Override
    public void actionPerformed(final NotepadController controller, final TextModel textModel, final ControllerEvent event) throws NotepadException {
        if (event instanceof CaretEvent || event instanceof ChangeTextEvent || event instanceof ScrollEvent){
            notepadView.update();
        }
        if(event instanceof ChangeTextEvent){
            notepadFrame.edited();
        }
    }
}
