package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.adapter.KeyboardType;
import notepad.controller.event.CaretEvent;
import notepad.controller.event.KeyboardEvent;
import notepad.text.TextModel;
import notepad.utils.Segment;
import notepad.view.NotepadView;
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

    private int hit1;
    private int hit2;
    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof KeyboardEvent) {
            final KeyboardEvent ke = (KeyboardEvent) event;
            if(!ke.getType().equals(KeyboardType.PRESSED)){
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
            if (ke.getKeyEvent().isShiftDown() && !view.isShowSelection()){
                hit1 = view.getCaretPosition();
            }
            if (shift != 0) {
                if (!ke.getKeyEvent().isShiftDown()) {
                    view.showSelectionSegment(false);
                    controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.SHIFT, shift));
                } else if(ke.getKeyEvent().isShiftDown()){
                    hit2 = view.getCaretPosition() + shift;
                    view.updateSelectionSegment(new Segment(Math.min(hit1, hit2), Math.max(hit1, hit2)));
                    view.showSelectionSegment(true);
                    controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.GOTO, hit2));
                }
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
                return textLayoutInfo.getLayout().getCharacterCount();
            }
        }
        return 0;
    }

    int up() {
        ArrayList<TextLayoutInfo> layouts = view.getLayouts();
        for (int i = 0; i < layouts.size(); ++i) {
            TextLayoutInfo textLayoutInfo = layouts.get(i);
            if (view.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
                if(i > 0){
                    return Math.min(-layouts.get(i - 1).getLayout().getCharacterCount(),
                            -(view.getCaretPosition() - textLayoutInfo.getPosition()));
                }
                return -textLayoutInfo.getLayout().getCharacterCount();
            }
        }
        return 0;
    }
}
