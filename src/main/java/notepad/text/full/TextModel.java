package notepad.text.full;

import notepad.NotepadException;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface TextModel {
    long length() throws NotepadException;

    /*
        Methods with logging of changes
     */
    void insert(long pos, String s) throws NotepadException;

    void replace(long pos, String s) throws NotepadException;

    void remove(long pos, int length) throws NotepadException;


    String get(long pos, int length) throws NotepadException;

    void addChangeTextListener(ChangeTextListener listener);

    void removeChangeTextListener(ChangeTextListener listener);

    void flush(File file) throws NotepadException;

    void close() throws NotepadException;
}
