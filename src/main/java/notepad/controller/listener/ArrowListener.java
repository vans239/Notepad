package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.adapter.KeyboardType;
import notepad.controller.event.CaretEvent;
import notepad.controller.event.KeyboardEvent;
import notepad.controller.event.ScrollEvent;
import notepad.text.TextModel;
import notepad.utils.SegmentL;
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

    private long hit1;

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof KeyboardEvent) {
            final KeyboardEvent ke = (KeyboardEvent) event;
            if (!ke.getType().equals(KeyboardType.PRESSED)) {
                return;
            }
            log.debug(String.format("ViewPos [%d] EditPos[%d] CaretPos [%d]", view.getViewPosition(), view.getEditPosition(), view.getCaretPosition()));
            if (ke.getKeyEvent().isShiftDown() && !view.isShowSelection()) {
                hit1 = view.getEditPosition();
            }
            initCaretInfo();
            switch (ke.getKeyEvent().getKeyCode()) {
                case VK_LEFT:
                    left(controller);
                    break;
                case VK_RIGHT:
                    right(controller);
                    break;
                case VK_DOWN:
                    down(controller);
                    break;
                case VK_UP:
                    up(controller);
                    break;
                default:
                    return;
            }

            if (!ke.getKeyEvent().isShiftDown()) {
                view.showSelectionSegment(false);
            } else if (ke.getKeyEvent().isShiftDown()) {
                long hit2 = view.getEditPosition();
                view.updateSelectionSegment(new SegmentL(Math.min(hit1, hit2), Math.max(hit1, hit2)));
                view.showSelectionSegment(true);
            }
        }
    }

    private int index;
    private TextLayoutInfo caretLayoutInfo;
    private ArrayList<TextLayoutInfo> layouts;

    private void left(NotepadController controller) {
        if (view.getCaretPosition() > 0) {
            controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.SHIFT, -1));
        } else {
            controller.fireControllerEvent(new ScrollEvent(ScrollEvent.Scroll.LEFT));
        }
    }

    private void right(NotepadController controller) {
        if (index != layouts.size() - 1 || (caretLayoutInfo.getPosition() + caretLayoutInfo.getLayout().getCharacterCount() - view.getCaretPosition() > 1)) {
            controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.SHIFT, 1));
        } else {
            controller.fireControllerEvent(new ScrollEvent(ScrollEvent.Scroll.RIGHT));
        }
    }

    private void down(NotepadController controller) {
        if (index != layouts.size() - 1) {
            TextLayoutInfo nextTextLayoutInfo = layouts.get(index + 1);
            int shift = Math.min(
                    nextTextLayoutInfo.getPosition() - view.getCaretPosition() + nextTextLayoutInfo.getLayout().getCharacterCount() - 1,
                    caretLayoutInfo.getLayout().getCharacterCount());
            controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.SHIFT, shift));
        } else {
            controller.fireControllerEvent(new ScrollEvent(ScrollEvent.Scroll.DOWN));
        }
    }

    private void up(NotepadController controller) {
        if (index > 0) {
            int shift = Math.min(-layouts.get(index - 1).getLayout().getCharacterCount(),
                    -(view.getCaretPosition() - caretLayoutInfo.getPosition() + 1));
            controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.SHIFT, shift));
        } else {
            controller.fireControllerEvent(new ScrollEvent(ScrollEvent.Scroll.UP));
        }
    }

    private void initCaretInfo() {
        layouts = view.getLayouts();
        for (int i = 0; i < layouts.size(); ++i) {
            TextLayoutInfo textLayoutInfo = layouts.get(i);
            if (view.caretInThisTextLayout(textLayoutInfo, i == layouts.size() - 1)) {
                caretLayoutInfo = textLayoutInfo;
                index = i;
                return;
            }
        }
        throw new IllegalArgumentException("Can't found caret on view");
    }

}
