package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.adapter.KeyboardType;
import notepad.controller.event.ChangeTextEvent;
import notepad.controller.event.KeyboardEvent;
import notepad.controller.event.PatchEvent;
import notepad.manager.Context;
import notepad.manager.Patch;
import notepad.manager.UndoManager;
import notepad.text.TextModel;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class UndoListener implements ControllerListener {
    private UndoManager<Patch> undoManager;
    private boolean isReverted = false;
    private NotepadView view;

    public UndoListener(UndoManager<Patch> undoManager, NotepadView view) {
        this.undoManager = undoManager;
        this.view = view;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event)
            throws NotepadException {
        if (event instanceof ChangeTextEvent) {
            ChangeTextEvent ce = (ChangeTextEvent) event;
            if (!isReverted) {
                undoManager.add(new Patch(new Context(view.getViewPosition(), view.getCaretPosition()), ce.getChangeTextEvent()));
            }
            isReverted = false;
        }
        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            KeyEvent ke = keyboardEvent.getKeyEvent();
            if (keyboardEvent.getType() != KeyboardType.PRESSED) {
                return;
            }
            Patch patch = null;
            PatchEvent.PatchType patchType = null;
            if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_Z) {
                patch = undoManager.undo();
                patchType = PatchEvent.PatchType.UNDO;
            } else if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_Y) {
                patch = undoManager.redo();
                patchType = PatchEvent.PatchType.REDO;
            }
            if (patch != null) {
                isReverted = true;
                controller.fireControllerEvent(new PatchEvent(patchType, patch));
            }
        }
    }
}
