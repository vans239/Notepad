package notepad.controller;

import notepad.model.CaretModel;
import notepad.model.OtherModel;
import notepad.model.SelectionModel;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MouseController implements MouseListener, MouseMotionListener {
    private static final Logger log = Logger.getLogger(MouseController.class);

    private final CaretModel caretModel;
    private final SelectionModel selectionModel;
    private final OtherModel otherModel;

    public MouseController(CaretModel caretModel, SelectionModel selectionModel, OtherModel otherModel) {
        this.caretModel = caretModel;
        this.selectionModel = selectionModel;
        this.otherModel = otherModel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        caretModel.goToPoint(deindent(e));
        otherModel.setShowSelection(false);
    }

    private Point deindent(MouseEvent e) {
        Point point = e.getPoint();
        point.translate(-otherModel.getTextIndent(), 0);
        return point;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        caretModel.goToPoint(deindent(e));
        selectionModel.setStart(caretModel.getCaretPositionAbs());
        otherModel.setShowSelection(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        caretModel.goToPoint(deindent(e));
        selectionModel.setEnd(caretModel.getCaretPositionAbs());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }



    @Override
    public void mouseDragged(MouseEvent e) {
        mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
