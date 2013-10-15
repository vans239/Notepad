package notepad.controller.listener;

import notepad.NotepadException;
import notepad.controller.ControllerEvent;
import notepad.controller.ControllerListener;
import notepad.controller.NotepadController;
import notepad.controller.event.CaretEvent;
import notepad.controller.event.MouseEvent;
import notepad.text.TextModel;
import notepad.utils.Segment;
import notepad.utils.SegmentL;
import notepad.view.NotepadView;
import notepad.view.TextLayoutInfo;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.util.ArrayList;

import static notepad.controller.adapter.MouseType.*;


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

    private long hit1;

    @Override
    public void actionPerformed(NotepadController controller, TextModel textModel, ControllerEvent event)
            throws NotepadException {
        if (event instanceof MouseEvent) {
            final MouseEvent mouseEvent = (MouseEvent) event;

            if (mouseEvent.getType().equals(CLICKED)) {
                log.info(mouseEvent.getEvent().getPoint());
                long index = view.getViewPosition() + getHitIndex(mouseEvent.getEvent().getPoint());
                controller.fireControllerEvent(
                        new CaretEvent(CaretEvent.CaretEventType.GOTO, index));
                view.showSelectionSegment(false);
            } else if (mouseEvent.getType().equals(PRESSED)) {
                hit1 = view.getViewPosition() + getHitIndex(mouseEvent.getEvent().getPoint());
            } else if (mouseEvent.getType().equals(RELEASED) || mouseEvent.getType().equals(DRAGGED)) {
                int hit2 = getHitIndex(mouseEvent.getEvent().getPoint());
                view.updateSelectionSegment(new SegmentL(Math.min(hit1, hit2), Math.max(hit1, hit2)));
                view.showSelectionSegment(true);
                controller.fireControllerEvent(new CaretEvent(CaretEvent.CaretEventType.GOTO, hit2));
            }
        }
    }

    public int getHitIndex(final Point clicked) {
        ArrayList<TextLayoutInfo> layouts = view.getLayouts();
        TextLayoutInfo nearestLayout = layouts.get(0);
        for (final TextLayoutInfo layoutInfo : layouts) {
            if (distance(layoutInfo, clicked) < distance(nearestLayout, clicked)) {
                nearestLayout = layoutInfo;
            }
        }
        final TextHitInfo hitInfo = nearestLayout.getLayout().hitTestChar(clicked.x, clicked.y);
        return nearestLayout.getPosition() + hitInfo.getInsertionIndex();

    }

    private int distance(final TextLayoutInfo textLayoutInfo, final Point clicked) {
        return Math.abs(textLayoutInfo.getOrigin().y - clicked.y);
    }
}
