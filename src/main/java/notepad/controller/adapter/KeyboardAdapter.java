package notepad.controller.adapter;

import notepad.controller.NotepadController;
import notepad.controller.event.KeyboardEvent;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class KeyboardAdapter {
    private static final Logger log = Logger.getLogger(KeyboardAdapter.class);

    public static void addKeyboardListener(final Component component, final NotepadController controller){
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                controller.fireControllerEvent(new KeyboardEvent(Type.TYPED, e));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                controller.fireControllerEvent(new KeyboardEvent(Type.PRESSED, e));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                controller.fireControllerEvent(new KeyboardEvent(Type.RELEASED, e));
            }
        });
    }
}
