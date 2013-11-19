package notepad.controller;

import notepad.NotepadException;
import notepad.manager.Context;
import notepad.manager.Patch;
import notepad.manager.UndoManager;
import notepad.model.CaretModel;
import notepad.model.OtherModel;
import notepad.model.SelectionModel;
import notepad.service.MoverService;
import notepad.text.full.ChangeTextEvent;
import notepad.text.full.ChangeTextListener;
import notepad.text.full.TextModel;
import notepad.text.window.TextWindowModel;
import notepad.utils.SegmentL;
import notepad.view.Mode;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.event.KeyEvent.*;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class KeyboardController implements KeyListener {
    private static final Logger log = Logger.getLogger(KeyboardController.class);

    private final CaretModel caretModel;
    private final SelectionModel selectionModel;
    private final OtherModel otherModel;
    private final TextWindowModel textWindowModel;
    private final MoverService moverService;
    private final TextModel textModel;
    private final OtherController otherController;
    private final UndoManager<Patch> undoManager;

    public KeyboardController(CaretModel caretModel, SelectionModel selectionModel,
                              OtherModel otherModel, TextWindowModel textWindowModel, MoverService moverService,
                              TextModel textModel, OtherController otherController, UndoManager<Patch> undoManager) {
        this.caretModel = caretModel;
        this.selectionModel = selectionModel;
        this.otherModel = otherModel;
        this.textWindowModel = textWindowModel;
        this.moverService = moverService;
        this.textModel = textModel;
        this.otherController = otherController;
        this.undoManager = undoManager;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        try {
            handler(e);
        } catch (NotepadException e1) {
            otherController.catchException(e1);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            arrows(e);
            handler(e);
            undoHandler(e);
        } catch (NotepadException e1) {
            otherController.catchException(e1);
        }
    }



    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == VK_F1) {
            otherModel.swapMode();
        }
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S){
            try {
                otherController.save();
            } catch (NotepadException e1) {
                log.error("Can't save file", e1);
            }
        }
    }

    public enum PatchType {
        UNDO, REDO
    }

    private void undoHandler(KeyEvent e) throws NotepadException {
        Patch patch = null;
        PatchType patchType = null;
        if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            patch = undoManager.redo();
            patchType = PatchType.REDO;
        } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            patch = undoManager.undo();
            patchType = PatchType.UNDO;
        }

        if (patch != null) {
            otherModel.setReverted(true);
            caretModel.goTo(0);
            final Context context;
            if (patchType == PatchType.REDO) {
                context = patch.getContextAfter();
                patch.getCte().apply(textModel);
            } else {
                context = patch.getContextBefore();
                patch.getCte().revert(textModel);
            }
            textWindowModel.goTo(context.getWindowPosition());
            caretModel.goTo(context.getCaretPosition());
        }
    }

    private void handler(KeyEvent e) throws NotepadException {
        Handler handler;
        if (selectionModel.isShowSelection()) {
            handler = new SelectionHandler();
        } else {
            handler = new CaretHandler();
        }

        if(e.getID() == KEY_TYPED){
            char keyChar = e.getKeyChar();
            //todo check is char printable real
            // \b
            if (keyChar != 8 && keyChar != '\u007f' && keyChar != '\u0019'
                    && (e.getModifiers() | java.awt.event.InputEvent.SHIFT_MASK) == java.awt.event.InputEvent.SHIFT_MASK) {
                handler.typed(keyChar);
            }
        }

        if (e.getID() == KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            handler.backSpace();
        }
        if (e.getID() == KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_DELETE) {
            handler.delete();
        }

    }

    private void arrows(KeyEvent e) throws NotepadException {
        if (e.isShiftDown() && !selectionModel.isShowSelection()) {
            selectionModel.setStart(caretModel.getCaretPositionAbs());
        }

        switch (e.getKeyCode()) {
            case VK_LEFT:
                moverService.left();
                break;
            case VK_RIGHT:
                moverService.right();
                break;
            case VK_DOWN:
                moverService.down();
                break;
            case VK_UP:
                moverService.up();
                break;
            default:
                return;
        }

        if(!e.isShiftDown() && selectionModel.isShowSelection()){
            selectionModel.unshowSelection();
        }

        if (e.isShiftDown()) {
            long hit2 = caretModel.getCaretPositionAbs();
            selectionModel.setEnd(hit2);
        }
    }

    private Patch patch;

    public void textModelChanged(ChangeTextEvent event) {
        if(!otherModel.isReverted()){
            Context contextBefore = getContext();
            patch = new Patch(
                    contextBefore, null, event);
        }
        otherModel.setReverted(false);
    }

    private Context getContext() {
        return new Context(textWindowModel.getWindowPosition(), caretModel.getCaretPosition());
    }

    interface Handler {
        void typed(char c) throws NotepadException;

        void delete() throws NotepadException;

        void backSpace() throws NotepadException;
    }

    class CaretHandler implements Handler {
        @Override
        public void typed(char keyChar) throws NotepadException {
            if (otherModel.getMode() == Mode.INSERT || caretModel.getCaretPositionAbs() == textModel.length()) {
                textModel.insert(caretModel.getCaretPositionAbs(), Character.toString(keyChar));
            } else {
                textModel.replace(caretModel.getCaretPositionAbs(), Character.toString(keyChar));
            }
            moverService.right();
            end();
        }

        @Override
        public void backSpace() throws NotepadException {
            if (caretModel.getCaretPositionAbs() > 0) {
                if(caretModel.getCaretPosition() == 0){
                    textWindowModel.up();
                }
                textModel.remove(caretModel.getCaretPositionAbs() - 1, 1);
                moverService.left();
            }
            end();
        }

        @Override
        public void delete() throws NotepadException {
            if (caretModel.getCaretPositionAbs() < textModel.length()) {
                textModel.remove(caretModel.getCaretPositionAbs(), 1);
            }
            end();
        }

        public void end() {
            selectionModel.unshowSelection();
            undoManager.add(new Patch(patch.getContextBefore(), getContext(), patch.getCte()));
        }
    }

    class SelectionHandler implements Handler {
        public void end() {
            selectionModel.unshowSelection();
            undoManager.add(new Patch(patch.getContextBefore(), getContext(), patch.getCte()));
        }

        @Override
        public void typed(char c) throws NotepadException {
            SegmentL segment = selectionModel.getSelection();
            textModel.remove(segment.getStart(), (int) (segment.getEnd() - segment.getStart()));
            undoManager.add(new Patch(patch.getContextBefore(), getContext(), patch.getCte()));
            textModel.insert(segment.getStart(), Character.toString(c));
            scrollToPosition(segment.getStart());
            caretModel.goTo((int) (segment.getStart() - textWindowModel.getWindowPosition() + 1));
            end();
        }

        @Override
        public void delete() throws NotepadException {
            SegmentL segment = selectionModel.getSelection();
            textModel.remove(segment.getStart(), (int) (segment.getEnd() - segment.getStart()));
            scrollToPosition(segment.getStart());
            caretModel.goTo((int) (segment.getStart() - textWindowModel.getWindowPosition()));
            end();
        }

        @Override
        public void backSpace() throws NotepadException {
            delete();
        }

        private void scrollToPosition(long pos) throws NotepadException {
            caretModel.goTo(0);
            while(textWindowModel.getWindowPosition() > pos){
                textWindowModel.up();
            }
        }
    }
}
