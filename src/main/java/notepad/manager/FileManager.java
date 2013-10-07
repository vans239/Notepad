package notepad.manager;

import notepad.NotepadException;
import notepad.text.TextModel;
import notepad.text.model.FlushTextModel;
import notepad.text.model.InMemoryTextModel;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class FileManager {
    private static final Logger log = Logger.getLogger(FileManager.class);

    public TextModel open(final File file) throws NotepadException {
        return new InMemoryTextModel(file);
    }
    public void save(final TextModel textModel, final File file) throws NotepadException {
        textModel.flush(file);
    }
}
