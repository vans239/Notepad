package notepad;

import notepad.controller.NotepadController;
import notepad.controller.adapter.*;
import notepad.controller.adapter.MouseAdapter;
import notepad.controller.event.FileEvent;
import notepad.controller.event.InitEvent;
import notepad.controller.event.SaveEvent;
import notepad.controller.listener.*;
import notepad.controller.listener.MouseListener;
import notepad.manager.FileManager;
import notepad.manager.undo.DeleteMerger;
import notepad.manager.undo.InsertMerger;
import notepad.manager.UndoManager;
import notepad.view.Mode;
import notepad.view.NotepadView;
import org.apache.log4j.Logger;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Notepad extends JFrame {
    private static final Logger log = Logger.getLogger(Notepad.class);

    private int fileSave;
    private int fileOpen;
    private JFileChooser sFile;
    private JFileChooser oFile;
    private NotepadController controller;

    private Mode mode = Mode.INSERT;
    private JMenu modeMenu = new JMenu(mode.name());
    private String currentDirectoryPath = "X:\\Dropbox\\programms\\Java\\Notepad+";

    public Notepad(NotepadController controller, NotepadView notepadView) {
        this.controller = controller;

        JMenuBar mb = new JMenuBar();
        JMenu mnuFile = new JMenu("File");
        JMenuItem mnuItemOpen = new JMenuItem("Open");
        JMenuItem mnuItemSave = new JMenuItem("Save");
        JMenuItem mnuItemQuit = new JMenuItem("Quit");

        getContentPane().setLayout(new BorderLayout());

        add(notepadView);

        addWindowListener(new ListenCloseWdw());
        mnuItemOpen.addActionListener(new ListenMenuOpen());
        mnuItemSave.addActionListener(new ListenMenuSave());
        mnuItemQuit.addActionListener(new ListenCloseWdw());

        setJMenuBar(mb);
        mb.add(mnuFile);
        mb.add(modeMenu);
        mnuFile.add(mnuItemOpen);
        mnuFile.add(mnuItemSave);
        mnuFile.add(mnuItemQuit);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
    }

    public void swapMode(){
        mode = mode.swap();
        modeMenu.setText(mode.name());
    }

    public Mode getMode(){
        return mode;
    }

    public class ListenCloseWdw extends WindowAdapter implements ActionListener {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }


    public class ListenMenuSave implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            sFile();
            if (fileSave == JFileChooser.APPROVE_OPTION) {
                controller.fireControllerEvent(new FileEvent(FileEvent.FileStatus.SAVE, sFile.getSelectedFile().getPath()));
                log.info(String.format("File for save: [%s]", sFile.getSelectedFile().getPath()));
            }
        }
    }

    public class ListenMenuOpen implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            oFile();
            if (fileOpen == JFileChooser.APPROVE_OPTION) {
                controller.fireControllerEvent(new FileEvent(FileEvent.FileStatus.OPEN, oFile.getSelectedFile().getPath()));
                log.info(String.format("File for open: [%s]", oFile.getSelectedFile().getPath()));
            }
        }
    }

    public void sFile() {
        JFileChooser save = new JFileChooser(currentDirectoryPath);
        fileSave = save.showSaveDialog(this);
        sFile = save;
    }

    public void oFile() {
        JFileChooser open = new JFileChooser(currentDirectoryPath);
        fileOpen = open.showOpenDialog(this);
        oFile = open;
    }

    public void launchFrame() {
        pack();
        setVisible(true);
    }

    public static void main(String args[]) throws IOException {
        final FileManager fileManager = new FileManager();
        final NotepadController controller = new NotepadController();

        final UndoManager undoManager = new UndoManager();
        undoManager.addMerger(new InsertMerger());
        undoManager.addMerger(new DeleteMerger());

        controller.addChangeTextListener(new InitListener(fileManager));
        controller.fireControllerEvent(new InitEvent());

        final NotepadView view = new NotepadView(controller);
        final Notepad gui = new Notepad(controller, view);

        KeyboardAdapter.addKeyboardListener(gui, controller);
        MouseAdapter.addMouseListener(view, controller);

        controller.addChangeTextListener(new FileListener(fileManager));
        controller.addChangeTextListener(new MouseListener(view));
        controller.addChangeTextListener(new ArrowListener(view));
        controller.addChangeTextListener(new TypingListener(view, gui));
        controller.addChangeTextListener(new CaretListener(view));
        controller.addChangeTextListener(new PatchListener());
        controller.addChangeTextListener(new UndoListener(undoManager));
        controller.addChangeTextListener(new UpdateListener(view));

        gui.launchFrame();
    }
}