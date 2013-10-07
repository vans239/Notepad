package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.CaretEvent;
import notepad.controller.event.FileEvent;
import notepad.manager.FileManager;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

import java.io.File;

import static notepad.controller.event.FileEvent.FileStatus.*;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class FileListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(FileListener.class);
    private FileManager fileManager;

    public FileListener(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof FileEvent) {
            FileEvent ov = (FileEvent) event;
            final File file = new File(ov.getFilePath());
            if (ov.getFileStatus() == OPEN) {
                final TextModel newTextModel = fileManager.open(file);
                controller.setTextModel(newTextModel);
                controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.GOTO, 0));
            } else {
                textModel.flush(file);
            }
        }
    }

}
