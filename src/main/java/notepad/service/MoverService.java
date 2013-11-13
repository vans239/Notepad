package notepad.service;

import notepad.NotepadException;
import notepad.model.CaretModel;
import notepad.text.window.TextWindowModel;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MoverService {
    private static final Logger log = Logger.getLogger(MoverService.class);
    private final TextWindowModel textWindowModel;
    private final CaretModel caretModel;

    public MoverService(TextWindowModel textWindowModel, CaretModel caretModel) {
        this.textWindowModel = textWindowModel;
        this.caretModel = caretModel;
    }

    public void up() throws NotepadException {
        if(!caretModel.goUp()){
            textWindowModel.up();
            caretModel.goUp();
        }
    }

    public void down() throws NotepadException {
        if(!caretModel.goDown()){
            textWindowModel.down();
            caretModel.goDown();
        }
    }

    public void left() throws NotepadException {
        if(!caretModel.goLeft()){
            textWindowModel.up();
            caretModel.goLeft();
        }
    }

    public void right() throws NotepadException {
        if(!caretModel.goRight()){
            textWindowModel.down();
            caretModel.goRight();
        }
    }
}
