package notepad.service;

import notepad.NotepadException;
import notepad.text.full.TextModel;
import notepad.text.full.model.AbstractTextModel;
import notepad.text.full.model.BufferedTextModel;
import notepad.text.full.model.Utf8FlushModel;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class FileService {
    private static final Logger log = Logger.getLogger(FileService.class);

    public AbstractTextModel open(final File file) throws NotepadException {
        final File temp;
        try {
            temp = File.createTempFile("notepad", "open");
            log.info(String.format("Created temp file [%s] for buffering changes [%s]", temp.getAbsolutePath(), file.getPath()));
            FileUtils.copyFile(file, temp);
            temp.deleteOnExit();
            return new BufferedTextModel(new Utf8FlushModel(temp));
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    public void save(final TextModel textModel, final File file) throws NotepadException {
        textModel.flush(file);
    }

    public AbstractTextModel empty() throws NotepadException {
        try {
            final File file = File.createTempFile("notepad", "init");
            return open(file);
        } catch (IOException e) {
            throw new NotepadException("Can't create new empty text model", e);
        }
    }
}
