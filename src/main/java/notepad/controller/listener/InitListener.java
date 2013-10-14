package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.InitEvent;
import notepad.manager.FileManager;
import notepad.text.TextModel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */

public class InitListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(InitListener.class);
    private FileManager fileManager;

    public InitListener(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event) throws NotepadException {
        if (event instanceof InitEvent) {
            try {
                final File file = File.createTempFile("notepad", "init");
                final TextModel newTextModel = fileManager.open(file);
                controller.setTextModel(newTextModel);
                //newTextModel.insert(0, " ");    //dirty hack for initialize multiline text
            } catch (IOException e) {
                throw new NotepadException("", e);
            }
        }

    }
}
