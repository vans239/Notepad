package notepad.view;

import notepad.controller.NotepadController;
import notepad.controller.event.FileEvent;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NotepadFrame extends JFrame {
    private static final Logger log = Logger.getLogger(NotepadFrame.class);

    private int fileSave;
    private int fileOpen;
    private JFileChooser sFile;
    private JFileChooser oFile;
    private NotepadController controller;

    private Mode mode = Mode.INSERT;
    private JMenu modeMenu = new JMenu(mode.name());
    private String currentDirectoryPath = "X:\\Dropbox\\programms\\Java\\Notepad+";
    private boolean isEdited = false;

    public NotepadFrame(NotepadController controller, NotepadView notepadView) {
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
    public void edited() {
        isEdited = true;
    }

    public void swapMode() {
        mode = mode.swap();
        modeMenu.setText(mode.name());
    }

    public Mode getMode() {
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
        }
    }

    public class ListenMenuOpen implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(isEdited){
                int ans = JOptionPane.showConfirmDialog(
                        NotepadFrame.this, "Do you want to save changes?",
                        "Notepad+",
                        JOptionPane.YES_NO_OPTION);
                if(ans == JOptionPane.YES_OPTION){
                    sFile();
                }
            }
            oFile();
        }
    }

    public void sFile() {
        JFileChooser save = new JFileChooser(currentDirectoryPath);
        fileSave = save.showSaveDialog(this);
        sFile = save;
        if (fileSave == JFileChooser.APPROVE_OPTION) {
            controller.fireControllerEvent(new FileEvent(FileEvent.FileStatus.SAVE, sFile.getSelectedFile().getPath()));
            log.info(String.format("File for save: [%s]", sFile.getSelectedFile().getPath()));
        }
        isEdited = false;
    }

    public void oFile() {
        JFileChooser open = new JFileChooser(currentDirectoryPath);
        fileOpen = open.showOpenDialog(this);
        oFile = open;
        if (fileOpen == JFileChooser.APPROVE_OPTION) {
            controller.fireControllerEvent(new FileEvent(FileEvent.FileStatus.OPEN, oFile.getSelectedFile().getPath()));
            isEdited = true;
            log.info(String.format("File for open: [%s]", oFile.getSelectedFile().getPath()));
        }
    }

    public void launchFrame() {
        pack();
        setVisible(true);
    }


}