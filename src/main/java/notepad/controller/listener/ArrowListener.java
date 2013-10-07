package notepad.controller.listener;

import notepad.NotepadException;
import notepad.view.NotepadView;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.adapter.Type;
import notepad.controller.event.KeyboardEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

import static java.awt.event.KeyEvent.*;
import static notepad.view.ArrowType.*;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ArrowListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(ArrowListener.class);
    private NotepadView view;

    public ArrowListener(final NotepadView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof KeyboardEvent) {
            final KeyboardEvent ke = (KeyboardEvent) event;
            if(!ke.getType().equals(Type.PRESSED)){
                return;
            }
            switch (ke.getKeyEvent().getKeyCode()) {
                case VK_LEFT:
                    view.updateCaret(LEFT);
                    break;
                case VK_RIGHT:
                    view.updateCaret(RIGHT);
                    break;
                case VK_DOWN:
                    view.updateCaret(DOWN);
                    break;
                case VK_UP:
                    view.updateCaret(UP);
                    break;
                default:
            }
        }
    }
}
