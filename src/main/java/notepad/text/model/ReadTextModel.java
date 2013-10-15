package notepad.text.model;

import notepad.NotepadException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ReadTextModel extends Utf8FlushModelText {
    private static final Logger log = Logger.getLogger(InMemoryTextModel.class);
    private RandomAccessFile raf;
    private File file;

    public ReadTextModel(final File file) throws NotepadException {
        super(file);
    }

    @Override
    public void doInsert(long pos, String s) throws NotepadException {
        throw new UnsupportedOperationException("This is read text model");
    }


    @Override
    public void doReplace(long pos, String s) throws NotepadException {
        throw new UnsupportedOperationException("This is read text model");
    }

    @Override
    public void doRemove(long pos, int length) throws NotepadException {
        throw new UnsupportedOperationException("This is read text model");
    }
}
