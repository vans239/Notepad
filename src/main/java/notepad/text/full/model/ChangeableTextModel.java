package notepad.text.full.model;

import notepad.NotepadException;
import notepad.text.full.ChangeTextListener;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ChangeableTextModel extends AbstractTextModel {
    private AbstractTextModel textModel;

    public ChangeableTextModel(AbstractTextModel textModel) {
        this.textModel = textModel;
    }

    public AbstractTextModel getTextModel(){
        return textModel;
    }
    public void changeTextModel(final AbstractTextModel textModel){
        this.textModel = textModel;
    }

    @Override
    public void doInsert(long pos, String s) throws NotepadException {
        textModel.doInsert(pos, s);
    }

    @Override
    public void doReplace(long pos, String s) throws NotepadException {
        textModel.doReplace(pos, s);
    }

    @Override
    public void doRemove(long pos, int length) throws NotepadException {
        textModel.doRemove(pos, length);
    }

    @Override
    public long length() throws NotepadException {
        return textModel.length();
    }

    @Override
    public String get(long pos, int length) throws NotepadException {
        return textModel.get(pos, length);
    }

    @Override
    public void flush(File file) throws NotepadException {
        textModel.flush(file);
    }

    @Override
    public void close() throws NotepadException {
        textModel.close();
    }
}
