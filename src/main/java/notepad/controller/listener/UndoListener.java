package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.adapter.KeyboardType;
import notepad.controller.adapter.MouseType;
import notepad.controller.event.ChangeTextEvent;
import notepad.controller.event.KeyboardEvent;
import notepad.controller.event.PatchEvent;
import notepad.manager.UndoManager;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

import java.awt.event.KeyEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class UndoListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(UndoListener.class);
    private UndoManager undoManager;
    private boolean isReverted = false;

    public UndoListener(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event)
            throws NotepadException {
        if (event instanceof ChangeTextEvent) {
            ChangeTextEvent ce = (ChangeTextEvent) event;
            if (!isReverted) {
                undoManager.add(ce.getChangeTextEvent());
            }
            isReverted = false;
        }
        if (event instanceof KeyboardEvent) {
            KeyboardEvent keyboardEvent = (KeyboardEvent) event;
            KeyEvent ke = keyboardEvent.getKeyEvent();
            if (keyboardEvent.getType() != KeyboardType.PRESSED) {
                return;
            }
            notepad.text.ChangeTextEvent cte = null;
            PatchEvent.PatchType patchType = null;
            if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_Z) {
                cte = undoManager.undo();
                patchType = PatchEvent.PatchType.UNDO;
            } else if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_Y) {
                cte = undoManager.redo();
                patchType = PatchEvent.PatchType.REDO;
            }
            if (cte != null) {
                isReverted = true;
                controller.fireControllerEvent(new PatchEvent(patchType, cte));
            }
        }
    }
}
