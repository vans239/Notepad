package notepad.view;

import notepad.model.OtherModel;
import notepad.utils.ImmediateObservable;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

public class NotepadFrame extends JFrame {
    private static final Logger log = Logger.getLogger(NotepadFrame.class);

    //contains future revisions should take path from *.properties
    private static final String DEFAULT_PATH = "X:\\Dropbox\\programms\\Java\\Notepad+";
    private static final String FRAME_NAME = "Notepad";

    private String currentDirectoryPath = DEFAULT_PATH;

    private final OtherModel otherModel;
    private StatusBar statusBar;
    public final ImmediateObservable saveObservable = new ImmediateObservable();
    public final ImmediateObservable sizeObservable = new ImmediateObservable();
    public final ImmediateObservable openObservable = new ImmediateObservable();

    public NotepadFrame(NotepadView notepadView, final OtherModel otherModel) {
        this.otherModel = otherModel;
        setTitle(FRAME_NAME);
        JMenuBar mb = new JMenuBar();
        JMenu mnuFile = new JMenu("File");
        JMenuItem mnuItemOpen = new JMenuItem("Open");
        JMenuItem mnuItemSave = new JMenuItem("Save");
        JMenuItem mnuItemQuit = new JMenuItem("Quit");

        statusBar = new StatusBar("", otherModel.isEdited(), otherModel.getMode());
        add(notepadView, BorderLayout.CENTER);
        add(statusBar, BorderLayout.PAGE_END);
        addWindowListener(new ListenCloseWdw());
        mnuItemOpen.addActionListener(new ListenMenuOpen());
        mnuItemSave.addActionListener(new ListenMenuSave());
        mnuItemQuit.addActionListener(new ListenCloseWdw());

        setJMenuBar(mb);
        mb.add(mnuFile);

        mnuFile.add(mnuItemOpen);
        mnuFile.add(mnuItemSave);
        mnuFile.add(mnuItemQuit);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
        otherModel.swapObservable.addObserver(new Observer(){
            @Override
            public void update(Observable o, Object arg) {
                statusBar.setMode(otherModel.getMode());
            }
        });
        otherModel.isEditObservable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                statusBar.setIsEdited(otherModel.isEdited());
            }
        });
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
        }
    }

    public class ListenMenuOpen implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (otherModel.isEdited()) {
                int ans = JOptionPane.showConfirmDialog(
                        NotepadFrame.this, "Do you want to save changes?",
                        "Notepad+",
                        JOptionPane.YES_NO_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    sFile();
                }
            }
            oFile();
        }
    }

    public void sFile() {
        JFileChooser save = new JFileChooser(currentDirectoryPath);
        int fileSave = save.showSaveDialog(this);
        if (fileSave == JFileChooser.APPROVE_OPTION) {
            saveObservable.notifyObservers(save.getSelectedFile());
            statusBar.setFile(save.getSelectedFile().getName());
        }
    }

    public void oFile() {
        JFileChooser open = new JFileChooser(currentDirectoryPath);
        int fileOpen = open.showOpenDialog(this);
        if (fileOpen == JFileChooser.APPROVE_OPTION) {
            openObservable.notifyObservers(open.getSelectedFile());
            statusBar.setFile(open.getSelectedFile().getName());
        }
    }

    public void launchFrame() {
        pack();
        setVisible(true);
    }
}