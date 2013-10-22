package notepad.controller;

import notepad.controller.event.ChangeTextEvent;
import notepad.text.ChangeTextListener;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ControllerTextListener implements ChangeTextListener {
    private NotepadController controller;

    public ControllerTextListener(NotepadController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(notepad.text.ChangeTextEvent event) {
        controller.fireControllerEvent(new ChangeTextEvent(event));
    }
}
