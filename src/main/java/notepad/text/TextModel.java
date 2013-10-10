package notepad.text;

import notepad.NotepadException;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface TextModel {
    long length() throws NotepadException;

    void insert(long pos, String s) throws NotepadException;

    void replace(long pos, String s) throws NotepadException;

    void remove(long pos, int length) throws NotepadException;

    String get(long pos, int length) throws NotepadException;

    void addChangeTextListener(ChangeTextListener listener);

    void removeChangeTextListener(ChangeTextListener listener);

    void flush(File file) throws NotepadException;

    void close();
}
