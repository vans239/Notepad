package notepad.manager;

import notepad.NotepadException;
import notepad.text.TextModel;
import notepad.text.model.BufferedFlushTextModel;
import notepad.text.model.FlushTextModel;
import notepad.text.model.InMemoryTextModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class FileManager {
    private static final Logger log = Logger.getLogger(FileManager.class);

    public TextModel open(final File file) throws NotepadException {
        final File temp;
        try {
            temp = File.createTempFile("notepad", "open");
            FileUtils.copyFile(file, temp);
            return new BufferedFlushTextModel(temp);
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }
    public void save(final TextModel textModel, final File file) throws NotepadException {
        textModel.flush(file);
    }
}
