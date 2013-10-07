package notepad.text.model;

import notepad.NotepadException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ReadTextModel extends AbstractTextModel {
    private static final Logger log = Logger.getLogger(InMemoryTextModel.class);
    private RandomAccessFile raf;
    private File file;

    public ReadTextModel(final File file) throws FileNotFoundException {
        this.raf = new RandomAccessFile(file, "r");
        this.file = file;
    }

    @Override
    public long length() throws NotepadException {
        try {
            return raf.length();
        } catch (IOException e) {
            throw new NotepadException("can't read length", e);
        }
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


    @Override
    public String get(long pos, int length) throws NotepadException {
        int err;
        byte[] bytes = new byte[length];
        try {
            raf.seek(pos);
            err = raf.read(bytes, 0, length);
        } catch (IOException e) {
            throw new NotepadException("There are not enough data");
        }
        if (err == -1) {
            throw new NotepadException("There are not enough data");
        }
        return new String(bytes);
    }

    @Override
    public void flush(final File file) throws NotepadException {
        try {
            raf.getChannel().transferTo(0, raf.length(), new RandomAccessFile(file, "rw").getChannel());
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }
}
