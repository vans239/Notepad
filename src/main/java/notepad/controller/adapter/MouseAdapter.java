package notepad.controller.adapter;

import notepad.controller.NotepadController;
import org.apache.log4j.Logger;

import java.awt.*;

import static notepad.controller.adapter.Type.*;
import static notepad.controller.adapter.Type.EXITED;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MouseAdapter {
    private static final Logger log = Logger.getLogger(MouseAdapter.class);

    public static void addMouseListener(final Component component, final NotepadController controller){
        component.addMouseListener(new java.awt.event.MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.fireControllerEvent(new notepad.controller.event.MouseEvent(CLICKED, e));
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                controller.fireControllerEvent(new notepad.controller.event.MouseEvent(PRESSED, e));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                controller.fireControllerEvent(new notepad.controller.event.MouseEvent(RELEASED, e));
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                controller.fireControllerEvent(new notepad.controller.event.MouseEvent(ENTERED, e));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                controller.fireControllerEvent(new notepad.controller.event.MouseEvent(EXITED, e));
            }
        });
    }
}
