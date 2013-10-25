package notepad.controller;

import notepad.NotepadException;
import notepad.model.CaretModel;
import notepad.model.OtherModel;
import notepad.model.SelectionModel;
import notepad.service.MoverService;
import notepad.text.full.TextModel;
import notepad.text.window.TextWindowModel;
import notepad.utils.SegmentL;
import notepad.view.TextLayoutInfo;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextHitInfo;
import java.util.ArrayList;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MouseController implements MouseListener, MouseMotionListener {
    private static final Logger log = Logger.getLogger(MouseController.class);

    private final CaretModel caretModel;
    private final SelectionModel selectionModel;
    private final OtherModel otherModel;
    private final TextWindowModel textWindowModel;
    private final OtherController otherController;

    public MouseController(CaretModel caretModel, SelectionModel selectionModel, OtherModel otherModel,
                           TextWindowModel textWindowModel, OtherController otherController) {
        this.caretModel = caretModel;
        this.selectionModel = selectionModel;
        this.otherModel = otherModel;
        this.textWindowModel = textWindowModel;
        this.otherController = otherController;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int index = getHitIndex(e.getPoint());
        caretModel.goTo(index);
        otherModel.setShowSelection(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selectionModel.setStart(textWindowModel.getWindowPosition() + getHitIndex(e.getPoint()));
        otherModel.setShowSelection(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int hit2 = getHitIndex(e.getPoint());
        selectionModel.setEnd(textWindowModel.getWindowPosition() + hit2);
        caretModel.goTo(hit2);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private int getHitIndex(final Point clicked) {
        ArrayList<TextLayoutInfo> layouts = textWindowModel.getLayouts();
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

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePressed(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
