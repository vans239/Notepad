package notepad.text.model;

import notepad.NotepadException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ReadTextModel extends FlushTextModel {
    private static final Logger log = Logger.getLogger(InMemoryTextModel.class);
    private RandomAccessFile raf;
    private File file;

    public ReadTextModel(final File file) throws NotepadException {
        super(file);
    }

    @Override
    public void _insert(long pos, String s) throws NotepadException {
        throw new UnsupportedOperationException("This is read text model");
    }


    @Override
    public void _replace(long pos, String s) throws NotepadException {
        throw new UnsupportedOperationException("This is read text model");
    }

    @Override
    public void _remove(long pos, int length) throws NotepadException {
        throw new UnsupportedOperationException("This is read text model");
    }
}
