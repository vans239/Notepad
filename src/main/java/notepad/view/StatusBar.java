package notepad.view;

import javax.swing.*;
import java.io.File;

public class StatusBar extends JPanel{
    private JLabel modeLabel = new JLabel();
    private JLabel fileLabel = new JLabel();
    private JLabel isEditedLabel = new JLabel();

    public StatusBar(final String fileName, final boolean isEdited, final Mode mode){
        add(isEditedLabel);
        add(fileLabel);
        add(modeLabel);

        setFile(fileName);
        setIsEdited(isEdited);
        setMode(mode);
    }
    public void setFile(final String fileName){
        fileLabel.setText(fileName);
    }

    public void setIsEdited(final boolean isEdited){
        isEditedLabel.setText(isEdited ? "*" : " " );
    }

    public void setMode(final Mode mode){
        modeLabel.setText(mode.name());
    }
}
