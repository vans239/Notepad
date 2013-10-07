package notepad.controller.listener;

import notepad.controller.event.CaretEvent;
import notepad.view.NotepadFrame;
import notepad.NotepadException;
import notepad.view.Mode;
import notepad.view.NotepadView;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.adapter.Type;
import notepad.controller.event.KeyboardEvent;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;

import static notepad.controller.event.CaretEvent.CaretEventType.SHIFT;


/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class TypingListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(TypingListener.class);
    private NotepadView view;
    private NotepadFrame notepad;

    public TypingListener(final NotepadView view, final NotepadFrame notepad) {
        this.view = view;
        this.notepad = notepad;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof KeyboardEvent) {
            final KeyboardEvent ke = (KeyboardEvent) event;
            if (ke.getType().equals(Type.TYPED)) {
                char keyChar = ke.getKeyEvent().getKeyChar();
                if (keyChar != 8 && keyChar != '\u007f' && keyChar != '\u001A' && keyChar != '\u0019') {
                    if(notepad.getMode() == Mode.INSERT){
                        log.info("Start");
                        textModel.insert(view.getEditPosition(), Character.toString(keyChar));
                        log.info("End");
                    } else {
                        textModel.replace(view.getEditPosition(), Character.toString(keyChar));
                    }
                    controller.fireControllerEvent(new CaretEvent(SHIFT, 1));
                }
            }
            if (ke.getType().equals(Type.PRESSED)) {
                if (ke.getKeyEvent().getKeyCode() == KeyEvent.VK_BACK_SPACE && view.getEditPosition() > 0) {
                    textModel.remove(view.getEditPosition() - 1, 1);
                    controller.fireControllerEvent(new CaretEvent(SHIFT, -1));
                }
            }
            if (ke.getType().equals(Type.PRESSED)) {
                if (ke.getKeyEvent().getKeyCode() == KeyEvent.VK_DELETE && view.getEditPosition() < textModel.length()) {
                    textModel.remove(view.getEditPosition(), 1);
                }
            }
            if (ke.getType().equals(Type.RELEASED)) {
                if (ke.getKeyEvent().getKeyCode() == KeyEvent.VK_F1) {
                    notepad.swapMode();
                }
            }
        }
    }
}

//java.awt.event.KeyEvent[KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='d',keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0,extendedKeyCode=0x0] on frame0, type=TYPED}
//java.awt.event.KeyEvent[KEY_TYPED,keyCode=0,keyText=Unknown keyCode: 0x0,keyChar='',modifiers=Ctrl,extModifiers=Ctrl,keyLocation=KEY_LOCATION_UNKNOWN,rawCode=0,primaryLevelUnicode=0,scancode=0,extendedKeyCode=0x0] on frame0