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
import notepad.view.Mode;
import notepad.view.NotepadFrame;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;

import static notepad.controller.event.CaretEvent.CaretEventType.GOTO;
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
            Handler handler;
            if (view.isShowSelection()) {
                handler = new SelectionHandler(textModel, controller);
            } else {
                handler = new CaretHandler(textModel, controller);
            }

            if (ke.getType().equals(KeyboardType.TYPED)) {
                char keyChar = ke.getKeyEvent().getKeyChar();
                //todo check is char printable real
                if (keyChar != 8 && keyChar != '\u007f' && keyChar != '\u001A' && keyChar != '\u0019') {
                    handler.typed(keyChar);
                }
            }
            if (ke.getType().equals(KeyboardType.PRESSED) && ke.getKeyEvent().getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                handler.backSpace();
            }
            if (ke.getType().equals(KeyboardType.PRESSED) && ke.getKeyEvent().getKeyCode() == KeyEvent.VK_DELETE) {
                handler.delete();
            }
            if (ke.getType().equals(KeyboardType.RELEASED) && ke.getKeyEvent().getKeyCode() == KeyEvent.VK_INSERT) {
                notepad.swapMode();
            }
        }
    }

    interface Handler {
        void typed(char c) throws NotepadException;

        void delete() throws NotepadException;

        void backSpace() throws NotepadException;
    }

    class CaretHandler implements Handler {
        private TextModel textModel;
        private NotepadController controller;

        CaretHandler(TextModel textModel, NotepadController controller) {
            this.textModel = textModel;
            this.controller = controller;
        }

        @Override
        public void typed(char keyChar) throws NotepadException {
            if (notepad.getMode() == Mode.INSERT || view.getEditPosition() == controller.length()) {
                textModel.insert(view.getEditPosition(), Character.toString(keyChar));
            } else {
                textModel.replace(view.getEditPosition(), Character.toString(keyChar));
            }
            controller.fireControllerEvent(new CaretEvent(SHIFT, 1));
        }

        @Override
        public void backSpace() throws NotepadException {
            if (view.getEditPosition() > 0) {
                textModel.remove(view.getEditPosition() - 1, 1);
                controller.fireControllerEvent(new CaretEvent(SHIFT, -1));
            }

        }

        @Override
        public void delete() throws NotepadException {
            if (view.getEditPosition() < textModel.length()) {
                textModel.remove(view.getEditPosition(), 1);
            }
        }
    }

    class SelectionHandler implements Handler {
        private TextModel textModel;
        private NotepadController controller;

        SelectionHandler(TextModel textModel, NotepadController controller) {
            this.textModel = textModel;
            this.controller = controller;
        }

        public void end() {
            view.showSelectionSegment(false);
        }

        @Override
        public void typed(char c) throws NotepadException {
            SegmentL segment = view.getSelectionSegment();
            textModel.remove(segment.getStart(), (int) (segment.getEnd() - segment.getStart()));
            textModel.insert(segment.getStart(), Character.toString(c));
            scrollToPosition(segment.getStart());
            controller.fireControllerEvent(new CaretEvent(GOTO, (int) (segment.getStart() - view.getViewPosition() + 1)));
            end();
        }

        @Override
        public void delete() throws NotepadException {
            SegmentL segment = view.getSelectionSegment();
            textModel.remove(segment.getStart(), (int) (segment.getEnd() - segment.getStart()));
            scrollToPosition(segment.getStart());
            controller.fireControllerEvent(new CaretEvent(GOTO, segment.getStart()));
            end();
        }

        @Override
        public void backSpace() throws NotepadException {
            delete();
        }
        private void scrollToPosition(long pos){
            while(view.getViewPosition() > pos){
                controller.fireControllerEvent(new CaretEvent(GOTO, view.getViewPosition()));
                controller.fireControllerEvent(new ScrollEvent(ScrollEvent.Scroll.UP));
            }
        }
    }
}

