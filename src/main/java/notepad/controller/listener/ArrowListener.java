package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.event.CaretEvent;
import notepad.view.NotepadView;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.adapter.Type;
import notepad.controller.event.KeyboardEvent;
import notepad.text.TextModel;
import notepad.view.TextLayoutInfo;
import org.apache.log4j.Logger;

import java.util.ArrayList;

import static java.awt.event.KeyEvent.*;

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
            if (!ke.getType().equals(Type.PRESSED)) {
                return;
            }
            int shift = 0;
            switch (ke.getKeyEvent().getKeyCode()) {
                case VK_LEFT:
                    shift = left();
                    break;
                case VK_RIGHT:
                    shift = right();
                    break;
                case VK_DOWN:
                    shift = down();
                    break;
                case VK_UP:
                    shift = up();
                    break;
            }
            if (shift != 0) {
                controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.SHIFT, shift));
            }
        }
    }

    int left() {
        return -1;
    }

    int right() {
        return 1;
    }

    int down() {
        ArrayList<TextLayoutInfo> layouts = view.getLayouts();
        for (int i = 0; i < layouts.size(); ++i) {
            TextLayoutInfo textLayoutInfo = layouts.get(i);
            if (view.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
//                if (i + 1 < layouts.size()) {
                    return textLayoutInfo.getLayout().getCharacterCount();
//                }
            }
        }
        return 0;
    }

    int up() {
        ArrayList<TextLayoutInfo> layouts = view.getLayouts();
        for (int i = 0; i < layouts.size(); ++i) {
            TextLayoutInfo textLayoutInfo = layouts.get(i);
            if (view.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
//                if (i >= 1) {
                return -textLayoutInfo.getLayout().getCharacterCount();
//                return -layouts.get(i - 1).getLayout().getCharacterCount();
//                }
            }
        }
        return 0;
    }
}
