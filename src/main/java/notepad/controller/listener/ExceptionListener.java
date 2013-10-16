package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.ExceptionEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ExceptionListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(ExceptionListener.class);

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event)
            throws NotepadException {
        if(event instanceof ExceptionEvent){
            ExceptionEvent e = (ExceptionEvent) event;
            throw new NotepadException(e.getCause(), e.getException());
        }
    }
}
