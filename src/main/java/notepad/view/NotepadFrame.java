package notepad.view;

import notepad.model.OtherModel;
import notepad.utils.ImmediateObservable;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
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
        setTitle(getNextTitle());
        setPreferredSize(new Dimension(600,480));
        setMinimumSize(new Dimension(150, 140));
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        JMenuBar mb = new JMenuBar();
        JMenu mnuFile = new JMenu("File");
        JMenuItem mnuItemOpen = new JMenuItem("Open");
        JMenuItem mnuItemSaveAs = new JMenuItem("Save as...");
        JMenuItem mnuItemSave = new JMenuItem("Save");
        JMenuItem mnuItemQuit = new JMenuItem("Quit");

        statusBar = new StatusBar(otherModel.getMode());
        add(notepadView, BorderLayout.CENTER);
        add(statusBar, BorderLayout.PAGE_END);
        addWindowListener(new ListenCloseWdw());
        mnuItemOpen.addActionListener(new ListenMenuOpen());
        mnuItemSaveAs.addActionListener(new ListenMenuSaveAs());
        mnuItemSave.addActionListener(new ListenMenuSave());
        mnuItemQuit.addActionListener(new ListenCloseWdw());

        setJMenuBar(mb);
        mb.add(mnuFile);

        mnuFile.add(mnuItemOpen);
        mnuFile.add(mnuItemSave);
        mnuFile.add(mnuItemSaveAs);
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
                setTitle(getNextTitle());
            }
        });
        otherModel.fileObservable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                setTitle(getNextTitle());
            }
        });
    }

    public class ListenCloseWdw extends WindowAdapter implements ActionListener {
        @Override
        public void windowClosing(WindowEvent e) {
            exit();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            exit();
        }
    }

    private void exit() {
        if (!saveIfUserWants())
            return;
        System.exit(0);
    }

    public class ListenMenuSaveAs implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            sFile();
        }
    }

    public class ListenMenuSave implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(otherModel.getFile() != null){
                saveObservable.notifyObservers();
            } else {
                sFile();
            }
        }
    }

    public class ListenMenuOpen implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (!saveIfUserWants())
                return;
            oFile();
        }
    }

    public boolean saveIfUserWants() {
        if (otherModel.isEdited()) {
            int ans = JOptionPane.showConfirmDialog(
                    this, "Do you want to save changes?",
                    "Notepad+",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (ans == JOptionPane.YES_OPTION) {
                sFile();
            }
            if(ans == JOptionPane.CANCEL_OPTION){
                return false;
            }
        }
        return true;
    }

    public void sFile() {
        JFileChooser save = new JFileChooser(currentDirectoryPath);
        int fileSave = save.showSaveDialog(this);
        if (fileSave == JFileChooser.APPROVE_OPTION) {
            saveObservable.notifyObservers(save.getSelectedFile());
        }
    }

    public void oFile() {
        JFileChooser open = new JFileChooser(currentDirectoryPath);
        int fileOpen = open.showOpenDialog(this);
        if (fileOpen == JFileChooser.APPROVE_OPTION) {
            openObservable.notifyObservers(open.getSelectedFile());
        }
    }

    public String getNextTitle(){
        File file = otherModel.getFile();
        return (otherModel.isEdited() ? "* " : "") + (file == null ? "untitled" : file.getName()) + " - " + FRAME_NAME;
    }

    public void launchFrame() {
        pack();
        setVisible(true);
    }
}