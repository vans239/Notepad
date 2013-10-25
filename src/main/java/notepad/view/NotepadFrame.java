package notepad.view;

import notepad.model.OtherModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;

public class NotepadFrame extends JFrame {
    private static final Logger log = Logger.getLogger(NotepadFrame.class);

    //in future revisions should take path from *.properties
    private static final String DEFAULT_PATH = "X:\\Dropbox\\programms\\Java\\Notepad+";

    private Mode mode = Mode.INSERT;
    private JMenu modeMenu;
    private String currentDirectoryPath = DEFAULT_PATH;

    private OtherModel otherModel;
    private Observable observable;

    public NotepadFrame(NotepadView notepadView) {
        JMenuBar mb = new JMenuBar();
        JMenu mnuFile = new JMenu("File");
        JMenuItem mnuItemOpen = new JMenuItem("Open");
        JMenuItem mnuItemSave = new JMenuItem("Save");
        JMenuItem mnuItemQuit = new JMenuItem("Quit");
        modeMenu = new JMenu(otherModel.getMode().name());
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

    //todo observer swapmode
    /*{        mode = mode.swap();
        modeMenu.setText(mode.name());
    }
    */

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
            observable.notifyObservers();//todo file save  save.getSelectedFile().getPath()
        }
    }

    public void oFile() {
        JFileChooser open = new JFileChooser(currentDirectoryPath);
        int fileOpen = open.showOpenDialog(this);
        if (fileOpen == JFileChooser.APPROVE_OPTION) {
            observable.notifyObservers();//todo file open  open.getSelectedFile().getPath()
        }
    }

    public void launchFrame() {
        pack();
        setVisible(true);
    }
}