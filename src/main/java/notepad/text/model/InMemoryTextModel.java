package notepad.text.model;

import notepad.NotepadException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class InMemoryTextModel extends AbstractTextModel {
    private static final Logger log = Logger.getLogger(InMemoryTextModel.class);
    private StringBuilder sb;

    public InMemoryTextModel(File file) throws NotepadException {
        try {
            final RandomAccessFile raf = new RandomAccessFile(file, "r");
            byte bytes[] = new byte[(int) raf.length()];
            raf.readFully(bytes);
            this.sb = new StringBuilder(new String(bytes));
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    @Override
    public long length() throws NotepadException {
        return sb.length();
    }

    @Override
    public String get(long pos, int length) throws NotepadException {
        return sb.substring((int) pos, (int) pos + length);
    }

    @Override
    public void flush(final File file) throws NotepadException {
        try {
            PrintWriter pw = new PrintWriter(file);
            IOUtils.write(sb.toString(), pw);
            pw.close();
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    @Override
    public void _insert(long pos, String s) throws NotepadException {
        sb.insert((int) pos, s);
    }

    @Override
    public void _replace(long pos, String s) throws NotepadException {
        sb.replace((int) pos, (int) pos + s.length(), s);
    }

    @Override
    public void _remove(long pos, int length) throws NotepadException {
        sb.delete((int) pos, (int) pos + length);
    }
}
