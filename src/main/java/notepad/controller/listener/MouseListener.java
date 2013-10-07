package notepad.controller.listener;

import notepad.NotepadException;
import notepad.view.NotepadView;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.MouseEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

import static notepad.controller.adapter.Type.*;


/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MouseListener implements ControllerListener{
    private static final Logger log = Logger.getLogger(MouseListener.class);

    private final NotepadView view;
    public MouseListener(final NotepadView view){
        this.view = view;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event)
            throws NotepadException {
        if(event instanceof MouseEvent){
            final MouseEvent mouseEvent = (MouseEvent) event;
            if(mouseEvent.getType().equals(CLICKED)){
                log.info(mouseEvent.getEvent().getPoint());
                view.updateCaret(mouseEvent.getEvent().getPoint());
            }
        }
    }
}
