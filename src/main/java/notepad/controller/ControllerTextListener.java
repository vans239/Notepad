package notepad.controller;

import notepad.controller.event.ChangeEvent;
import notepad.text.ChangeTextEvent;
import notepad.text.ChangeTextListener;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ControllerTextListener implements ChangeTextListener {
    private static final Logger log = Logger.getLogger(ControllerTextListener.class);
    private NotepadController controller;

    public ControllerTextListener(NotepadController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ChangeTextEvent event) {
        controller.fireControllerEvent(new ChangeEvent(event));
    }
}
