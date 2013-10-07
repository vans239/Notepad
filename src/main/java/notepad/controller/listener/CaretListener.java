package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.CaretEvent;
import notepad.text.TextModel;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class CaretListener implements ControllerListener{
    private static final Logger log = Logger.getLogger(CaretListener.class);
    private NotepadView notepadView;

    public CaretListener(NotepadView notepadView) {
        this.notepadView = notepadView;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if(event instanceof CaretEvent){
            CaretEvent ce = (CaretEvent) event;
            if(ce.getType() == CaretEvent.CaretEventType.SHIFT){
                notepadView.updateCaretShift(ce.getValue());
            }   else {
                notepadView.updateCaretGoTo(0);
            }
        }
    }
}
