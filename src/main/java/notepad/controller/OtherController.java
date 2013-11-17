package notepad.controller;

import notepad.NotepadException;
import notepad.model.OtherModel;
import notepad.service.FileService;
import notepad.model.CaretModel;
import notepad.text.full.model.AbstractTextModel;
import notepad.text.full.model.ChangeableTextModel;
import notepad.text.window.TextWindowModel;
import notepad.view.NotepadFrame;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

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
    private NotepadFrame frame;

    public OtherController(ChangeableTextModel textModel, FileService fileService,
                           CaretModel caretModel, TextWindowModel windowModel, OtherModel otherModel, NotepadView view, NotepadFrame frame) {
        this.textModel = textModel;
        this.fileService = fileService;
        this.caretModel = caretModel;
        this.windowModel = windowModel;
        this.otherModel = otherModel;
        this.view = view;
        this.frame = frame;
    }

    public void init() throws NotepadException {
        textModel.changeTextModel(fileService.empty());
        windowModel.init(view.getSize(), view.getMetrics(), view.getFontRenderContext());
//        view.addComponentListener(resizedAdapter);
    }

    /**
     * asks user for file if need
     */
    public void save() throws NotepadException {
        if(otherModel.getFile() == null){
            frame.sFile();
        } else {
            save(otherModel.getFile());
        }
    }

    private void open(File file) throws NotepadException {
        final AbstractTextModel newTextModel = fileService.open(file);
        textModel.changeTextModel(newTextModel);
        windowModel.goTo(0);
        caretModel.goTo(0);
        otherModel.setEdited(false);
    }

    private void save(File file) throws NotepadException {
        fileService.save(textModel, file);
        otherModel.setEdited(false);
    }

    public void catchException(final Exception e){
        log.error("", e);
    }

    public final Observer saveObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            try {
                if(arg != null){
                    otherModel.setFile((File) arg);
                }
                save(otherModel.getFile());
            } catch (NotepadException e) {
                log.error("", e);
            }
        }
    };

    public final Observer openObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            try {
                otherModel.setFile((File) arg);
                open(otherModel.getFile());
            } catch (NotepadException e) {
                log.error("", e);
            }
        }
    };

    public void setSize(Dimension size) throws NotepadException {
        windowModel.setSize(size);
        caretModel.goTo(0);
    }

    public final ComponentAdapter resizedAdapter = new ComponentAdapter() {
        public void componentResized(ComponentEvent e) {
            try {
                Dimension size = e.getComponent().getSize();
                Dimension sizeForDraw = new Dimension(size.width - otherModel.getTextIndent(), size.height);
                setSize(sizeForDraw);
            } catch (NotepadException exc) {
                log.error("", exc);
            }
        }
    };
}
