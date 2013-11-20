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
import org.apache.log4j.Logger;

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
 + 6) usability-проблема: если набрать 2 строчки: abcdef abc поставить курсор после f, нажать стрелку вниз, потом стрелку вверх - каретка получится не в той же позиции, где была.
 + 9) cosmetics: не пишите в консоль так много информации - очень сложно из-за огромного ее количества отлавливать взглядом исключения
 ? 11) попробуйте набрать несколько слов, разделенных переводами строк, но без пробелов и понажимать ctrl-z - работает явно не так, как хотелось бы
 ? 12)  то же самое - с ctrl-y
 + 14)usability: наберите слово, нажмите ctrl-z, ctrl-y - каретка не на том же месте, что и в начале
 ? 5) todo после набора 5 строк текста зажал ctrl-z и долго держал:
 Exception in thread "AWT-EventQueue-0" java.lang.StringIndexOutOfBoundsException: String index out of range: 20
 at java.lang.AbstractStringBuilder.substring(AbstractStringBuilder.java:879)
 at java.lang.StringBuilder.substring(StringBuilder.java:55)
 at notepad.text.full.model.BufferedTextModel.get(BufferedTextModel.java:37)
 at notepad.text.full.model.ChangeableTextModel.get(ChangeableTextModel.java:44)
 at notepad.text.full.model.AbstractTextModel.remove(AbstractTextModel.java:48)
 at notepad.text.full.event.InsertEvent.revert(InsertEvent.java:27)
 at notepad.controller.KeyboardController.undoHandler(KeyboardController.java:106)
 at notepad.controller.KeyboardController.keyPressed(KeyboardController.java:67)

 + 7) набираю “abc”, выделяю “b", нажимаю “d” (замена), потом e. Потом 2 раза жму ctrl-z:
    Exception in thread "AWT-EventQueue-0" java.lang.StringIndexOutOfBoundsException: String index out of range: 3
 + 11) при выходе из приложения (крестиком или quit в меню) изменения в файле просто теряются
 + 14) в режиме replace невозможно добавить строчек в конец файла
 + 15) нет скроллинга в случае, когда кsa
 ол-во строк не помещается на экране
 + 16) Большие траблы с ревертом (нужна 2 патча!)
 +2) Replace. Для юзера нет символа “\n”, а есть конец строки. Интуитивно конец строки нельзя заменить на символ. Поэтому текущее поведение контринтуитивно. Посмотрите как работает в абсолютно любом редакторе. Ни в одном переводы строк заменяться не будут. Вам нужно сделать что-то похожее.
 ? 1) при изменении размеров окна каретка меняет место в тексте
 ? 2) скроллинга пока нет
 + 3) (usability) при выделении нескольких пустых строк подряд выделенного фрагмента не видно
 + 4) набираю “a b”, выделяю “a “, 2 раза нажимаю “delete”. Если после этого нажать ctrl-z - в документ добавляются 2 слова. По условию ctrl-z должен менять не более 1 слова за раз
 ? 5) (см прикрепленный файл) при наборе текста внезапно после строчки <rm “$f”> появляется пустая строка. Обычный файл, пробелов после последнего символа в этой строке нет
 ? 6) автопрокрутка не работает нигде (при выделении мышкой, PgUp/PgDn, колесико мышки). Из-за этого работать с большим текстом невозможно в принципе
 + 7) иногда при ползании вниз-вверх по файлу каретка просто пропадает (см. второй прикрепленный файл)
 a b
 + 1) Окно сдвинуто, каретка в самом начале окна, backspace
 По коду:
 1) не используйте интерфейс ObservableImpl - это плохое решение, потому что сразу же теряется типизация кода. Для Java 1.0 такой вариант был неплохим решением (потому что там cast’ы приходилось ставить везде), после введения generics такое решение уже не может считаться удовлетворительным
 2) OtherModel - плохое имя, ничего не говорит о назначении класса. Грубо говоря, так можно было бы назвать все модельные классы
 + 3) Класс SelectionModel implements ObservableImpl, но нигде не вызывает notifyObservers. Зачем тогда такое наследование?
 4) Поля типа ObservableImpl - это очень плохой способ реализации паттерна “Listener”. Во-первых, такой подход вообще нерасширяем (например, нельзя listener’у добавить аргумент), во-вторых, абсолютно не понятен другим людям (т.к. все привыкли к другой реализации Listener’ов)
 5) метод setShowSelection - назначение не понятно. Выделение нужно показывать всегда, просто иногда само выделение пусто. То же с reverted - назначение метода не понятно. Может быть, что-то прояснится, когда Вы переименуете класс OtherModel - сейчас Вы и сами вряд ли понимаете, моделью чего он является.
 ? 6) я не понял, зачем нужен класс ChangeableTextModel, он все равно все методы делегирует нижележащей модели
 */

public class NotepadStarter {
    private static final Logger log = Logger.getLogger(NotepadStarter.class);

    public static void main(String args[]) throws IOException, NotepadException, InterruptedException {
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

        final OtherController otherController = new OtherController(textModel, fileService,caretModel,windowModel,otherModel, view, gui);
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
