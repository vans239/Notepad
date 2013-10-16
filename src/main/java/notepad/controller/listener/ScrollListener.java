package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.ScrollEvent;
import notepad.text.TextModel;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

import static notepad.controller.event.ScrollEvent.Scroll.*;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ScrollListener implements ControllerListener {
    private NotepadView notepadView;

    public ScrollListener(NotepadView notepadView) {
        this.notepadView = notepadView;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof ScrollEvent) {
            ScrollEvent se = (ScrollEvent) event;
            if (UP == se.getScroll()) {
                notepadView.scrollUp();
            } else if (DOWN == se.getScroll()) {
                notepadView.scrollDown();
            } else if (RIGHT == se.getScroll()) {
                notepadView.scrollRight();
            } else if (LEFT == se.getScroll()) {
                notepadView.scrollLeft();
            }else if(GOTO == se.getScroll()){
                notepadView.scrollGoto(se.getViewPosition());
            }
        }
    }
}
