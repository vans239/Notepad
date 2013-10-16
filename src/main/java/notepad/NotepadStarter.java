package notepad;

import notepad.controller.NotepadController;
import notepad.controller.adapter.KeyboardAdapter;
import notepad.controller.adapter.MouseAdapter;
import notepad.controller.event.InitEvent;
import notepad.controller.listener.*;
import notepad.manager.FileManager;
import notepad.manager.Patch;
import notepad.manager.UndoManager;
import notepad.manager.undo.DeleteMerger;
import notepad.manager.undo.InsertMerger;
import notepad.view.NotepadFrame;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */

/**
 *
 * Testing:
 * check selection more than one screen
 * check resize with caret at the end of text
 * check russian symbols (open, save, open)
 */
public class NotepadStarter {
    public static void main(String args[]) throws IOException, NotepadException {
        final FileManager fileManager = new FileManager();
        final NotepadController controller = new NotepadController();

        final UndoManager<Patch> undoManager = new UndoManager<Patch>();
        undoManager.addMerger(new InsertMerger());
        undoManager.addMerger(new DeleteMerger());

        controller.addChangeTextListener(new InitListener(fileManager));
        controller.fireControllerEvent(new InitEvent());

        final NotepadView view = new NotepadView(controller);
        final NotepadFrame gui = new NotepadFrame(controller, view);

        KeyboardAdapter.addKeyboardListener(gui, controller);
        MouseAdapter.addMouseListener(view, controller);

        controller.addChangeTextListener(new FileListener(fileManager));
        controller.addChangeTextListener(new MouseListener(view));
        controller.addChangeTextListener(new ArrowListener(view));
        controller.addChangeTextListener(new TypingListener(view, gui));
        controller.addChangeTextListener(new CaretListener(view));
        controller.addChangeTextListener(new PatchListener(view));
        controller.addChangeTextListener(new UndoListener(undoManager, view));
        controller.addChangeTextListener(new ScrollListener(view));
        controller.addChangeTextListener(new ExceptionListener());
        controller.addChangeTextListener(new UpdateListener(view, gui));

        gui.launchFrame();
    }
}
