package notepad.view;

import javax.swing.*;
import java.io.File;

public class StatusBar extends JPanel{
    private JLabel modeLabel = new JLabel();

    public StatusBar(final Mode mode){
        add(modeLabel);
        setMode(mode);
    }

    public void setMode(final Mode mode){
        modeLabel.setText(mode.name());
    }
}
