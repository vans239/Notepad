package notepad.text.full.model;

import notepad.NotepadException;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ReadTextModel extends Utf8FlushModel {
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
