package notepad.view;

import notepad.NotepadException;
import notepad.controller.NotepadController;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class NotepadView extends JPanel {
    private static final Logger log = Logger.getLogger(NotepadView.class);
    private static final int DEFAULT_LENGTH = 20480;

    private int maxLength = DEFAULT_LENGTH;
    private long viewPosition = 0;
    private int caretPosition = 0;
    private String text = "";
    private NotepadController controller;
    private MultiLineTextLayout mt;

    public NotepadView(final NotepadController controller) {
        this.controller = controller;
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 480);
    }

    public long getViewPosition() {
        return viewPosition;
    }

    public long getEditPosition() {
        return viewPosition + caretPosition;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        updateText();
        if (text.isEmpty()) {
            return;           //todo empty text edition
        }
        mt = new MultiLineTextLayout(text, caretPosition, getSize());
        final Graphics2D g2d = (Graphics2D) g;
        final int drawPosX = 0;
        final int drawPosY = 0;
        mt.draw(g2d, drawPosX, drawPosY);
    }

    public boolean isScrollAvailable(final int shift) throws NotepadException {
        return viewPosition + shift < controller.length() && viewPosition + shift >= 0;
    }

    public void updateScroll(final int shift) throws NotepadException {
        if(viewPosition + shift < 0){
            viewPosition = 0;
        } else if(viewPosition + shift > controller.length()){
            viewPosition = controller.length();
        } else {
            viewPosition += shift;
        }
    }

    public void updateCaret(final int shift){
        mt.updateCaret(shift);
        caretPosition = mt.getCaretPosition();  //todo ???
    }

    public void updateCaret(final ArrowType arrowType) {
        arrowType.updateCaretInsertionIndex(mt);
        caretPosition = mt.getCaretPosition(); //todo ???
    }

    public void updateCaret(final Point clicked){
        ArrayList<TextLayoutInfo> layouts = mt.getLayouts();
        TextLayoutInfo nearestLayout = layouts.get(0);
        for (final TextLayoutInfo layoutInfo : layouts) {
            if (distance(layoutInfo, clicked) < distance(nearestLayout, clicked)) {
                nearestLayout = layoutInfo;
            }
        }
        final TextHitInfo hitInfo = nearestLayout.getLayout().hitTestChar(clicked.x, clicked.y);
        caretPosition = nearestLayout.getPosition() + hitInfo.getInsertionIndex();
    }

    private int distance(final TextLayoutInfo textLayoutInfo, final Point clicked){
        return Math.abs(textLayoutInfo.getOrigin().y - clicked.y);
    }

    private void updateText() {
        try {
            text = controller.get(viewPosition, Math.min((int) (controller.length() - viewPosition), maxLength));
        } catch (NotepadException e) {
            log.error("Can't read text from text model", e);
        }
    }

}
