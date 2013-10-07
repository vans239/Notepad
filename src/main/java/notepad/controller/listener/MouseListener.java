package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.event.CaretEvent;
import notepad.view.NotepadView;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.MouseEvent;
import notepad.text.TextModel;
import notepad.view.TextLayoutInfo;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.util.ArrayList;

import static notepad.controller.adapter.Type.*;


/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MouseListener implements ControllerListener {
    private static final Logger log = Logger.getLogger(MouseListener.class);

    private final NotepadView view;

    public MouseListener(final NotepadView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event)
            throws NotepadException {
        if (event instanceof MouseEvent) {
            final MouseEvent mouseEvent = (MouseEvent) event;
            if (mouseEvent.getType().equals(CLICKED)) {
                log.info(mouseEvent.getEvent().getPoint());
                updateCaret(controller, mouseEvent.getEvent().getPoint());
            }
        }
    }

    public void updateCaret(NotepadController controller, final Point clicked) {
        ArrayList<TextLayoutInfo> layouts = view.getLayouts();
        TextLayoutInfo nearestLayout = layouts.get(0);
        for (final TextLayoutInfo layoutInfo : layouts) {
            if (distance(layoutInfo, clicked) < distance(nearestLayout, clicked)) {
                nearestLayout = layoutInfo;
            }
        }
        final TextHitInfo hitInfo = nearestLayout.getLayout().hitTestChar(clicked.x, clicked.y);
        controller.fireControllerEvent(
                new CaretEvent(CaretEvent.CaretEventType.GOTO, nearestLayout.getPosition() + hitInfo.getInsertionIndex()));
    }

    private int distance(final TextLayoutInfo textLayoutInfo, final Point clicked) {
        return Math.abs(textLayoutInfo.getOrigin().y - clicked.y);
    }
}
