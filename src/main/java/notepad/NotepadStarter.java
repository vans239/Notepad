package notepad;

import notepad.controller.KeyboardController;
import notepad.controller.MouseController;
import notepad.controller.OtherController;
import notepad.model.CaretModel;
import notepad.model.OtherModel;
import notepad.model.SelectionModel;
import notepad.service.FileService;
import notepad.manager.Patch;
import notepad.manager.UndoManager;
import notepad.manager.undo.DeleteMerger;
import notepad.manager.undo.InsertMerger;
import notepad.service.MoverService;
import notepad.text.full.ChangeTextEvent;
import notepad.text.full.ChangeTextListener;
import notepad.text.full.model.ChangeableTextModel;
import notepad.text.window.TextWindowModel;
import notepad.view.NotepadFrame;
import notepad.view.NotepadView;

import java.io.IOException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */

/**
 * Testing:
 + check selection more than one screen
 + check resize with caret at the end of text
 + check russian symbols (open, save, open)
 + 1) наберите строчку, зажмите и не отпускайте backspace - удалятся не все символы
 + 4) после нажатия enter в конце текста каретка не переходит на следующую строчку
 + 5) после второго нажатия она пропадает
 + 6) usability-проблема: если набрать 2 строчки:
 abcdef
 abc
 поставить курсор после f, нажать стрелку вниз, потом стрелку вверх - каретка получится не в той же позиции, где была. В приличных редакторах позиция после таких действий не поменяется. Поправьте, пожалуйста.
 + 8) cosmetics: желательно, чтобы текст начинался не прямо от края окна, а имел небольшой отступ
 + 9) cosmetics: не пишите в консоль так много информации - очень сложно из-за огромного ее количества отлавливать взглядом исключения
 ? 10) после большого количества действий зажал "ctrl-z" и подержал - упало исключение:
 ? 11) попробуйте набрать несколько слов, разделенных переводами строк, но без пробелов и понажимать ctrl-z - работает явно не так, как хотелось бы
 ? 12) то же самое - с ctrl-y
 + 13) usability: ctrl-y - плохое сочетание. Понимаю, что написано в readme, но, если честно, когда Вы последний раз читали readme к notepad'y? ;) Стандартное сочетание клавиш для redo - ctrl-shift-z
 14) todo usability: наберите слово, нажмите ctrl-z, ctrl-y - каретка не на том же месте, что и в начале

 + 1) Segment и SegmentL - явное дублирование. Его как-то нужно убрать
 2) Если у Вас UndoManager всегда параметризован классом Patch - зачем вообще нужна эта параметризация? Тот же вопрос - про класс Merger
 + 3) Плохая работа при добавлении в конец пустой строки!!
 + 4) буфферезирование файла в конце
 */

public class NotepadStarter {
    public static void main(String args[]) throws IOException, NotepadException {
        final FileService fileService = new FileService();

        final ChangeableTextModel textModel = new ChangeableTextModel();
        final OtherModel otherModel = new OtherModel();
        final TextWindowModel windowModel = new TextWindowModel(textModel);
        final CaretModel caretModel = new CaretModel(windowModel);
        final SelectionModel selectionModel = new SelectionModel();

        final UndoManager<Patch> undoManager = new UndoManager<Patch>();
        undoManager.addMerger(new InsertMerger());
        undoManager.addMerger(new DeleteMerger());
        final MoverService moverService = new MoverService(windowModel, caretModel);

        final NotepadView view = new NotepadView(windowModel, caretModel, otherModel, selectionModel);
        final NotepadFrame gui = new NotepadFrame(view, otherModel);

        final OtherController otherController = new OtherController(textModel, fileService,caretModel,windowModel,otherModel, view);
        final MouseController mouseController =
                new MouseController(caretModel,selectionModel,otherModel);
        final KeyboardController keyboardController =
                new KeyboardController(caretModel, selectionModel,otherModel,windowModel,moverService, textModel,otherController, undoManager);

        otherController.init();
        view.addMouseListener(mouseController);
        view.addMouseMotionListener(mouseController);
        view.addComponentListener(otherController.resizedAdapter);
        gui.addKeyListener(keyboardController);
        gui.openObservable.addObserver(otherController.openObserver);
        gui.saveObservable.addObserver(otherController.saveObserver);

        textModel.addChangeTextListener(otherModel.editChangeTextListener);

        textModel.addChangeTextListener(new ChangeTextListener() {
            @Override
            public void actionPerformed(ChangeTextEvent event) {
                keyboardController.textModelChanged(event);    //todo to model
            }
        });
        gui.launchFrame();
    }
}
