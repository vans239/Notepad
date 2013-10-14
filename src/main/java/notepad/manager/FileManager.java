package notepad.manager;

import notepad.NotepadException;
import notepad.text.TextModel;
import notepad.text.model.BufferedTextModel;
import notepad.text.model.Utf8FlushModelText;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
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
            log.info(String.format("Created temp file [%s] for buffering changes [%s]", temp.getAbsolutePath(), file.getPath()));
            FileUtils.copyFile(file, temp);
            temp.deleteOnExit();
            return new BufferedTextModel(new Utf8FlushModelText(temp));
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    public void save(final TextModel textModel, final File file) throws NotepadException {
        textModel.flush(file);
        textModel.close();
    }
}
