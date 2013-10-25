package notepad.controller;

import notepad.NotepadException;
import notepad.model.OtherModel;
import notepad.service.FileService;
import notepad.model.CaretModel;
import notepad.text.full.model.AbstractTextModel;
import notepad.text.full.model.ChangeableTextModel;
import notepad.text.window.TextWindowModel;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class OtherController {
    private static final Logger log = Logger.getLogger(OtherController.class);
    private final ChangeableTextModel textModel;
    private final FileService fileService;
    private final CaretModel caretModel;
    private final TextWindowModel windowModel;
    private final OtherModel otherModel;
    private NotepadView view; //todo ??
    public OtherController(ChangeableTextModel textModel, FileService fileService,
                           CaretModel caretModel, TextWindowModel windowModel, OtherModel otherModel) {
        this.textModel = textModel;
        this.fileService = fileService;
        this.caretModel = caretModel;
        this.windowModel = windowModel;
        this.otherModel = otherModel;
    }

    private void init() throws NotepadException {
        textModel.changeTextModel(fileService.empty());
        windowModel.init(view.getSize(), view.getMetrics(), view.getFontRenderContext());
    }

    public void open(File file) throws NotepadException {
        final AbstractTextModel newTextModel = fileService.open(file);
        textModel.changeTextModel(newTextModel);
        windowModel.goTo(0);
        caretModel.goTo(0);
        otherModel.setEdited(false);
    }

    public void save(File file) throws NotepadException {
        fileService.save(textModel, file);
        otherModel.setEdited(false);
    }

    public void catchException(final Exception e){

    }
}
