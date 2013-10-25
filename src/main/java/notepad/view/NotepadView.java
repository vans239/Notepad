package notepad.view;

import notepad.NotepadException;
import notepad.model.CaretModel;
import notepad.model.OtherModel;
import notepad.model.SelectionModel;
import notepad.text.window.TextWindowModel;
import notepad.utils.Segment;
import notepad.utils.SegmentL;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.util.ArrayList;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class NotepadView extends JPanel {
    private static final Logger log = Logger.getLogger(NotepadView.class);
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    private static final Color CARET_COLOR = Color.red;
    private static final Color TEXT_COLOR = Color.black;
    private static final Color INV_TEXT_COLOR = Color.white;
    private static final Color BACKGROUND = Color.white;
    private static final Color INV_BACKGROUND = Color.blue;

    private final FontMetrics metrics = getFontMetrics(FONT);
    private final FontRenderContext frc = getFontMetrics(FONT).getFontRenderContext();

    private TextWindowModel windowModel;
    private CaretModel caretModel;
    private OtherModel otherModel;
    private SelectionModel selectionModel;

    public NotepadView(TextWindowModel windowModel, CaretModel caretModel, OtherModel otherModel, SelectionModel selectionModel)
            throws NotepadException {
        this.windowModel = windowModel;
        this.caretModel = caretModel;
        this.otherModel = otherModel;
        this.selectionModel = selectionModel;

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                try {
                    NotepadView.this.windowModel.setSize(getSize()); //todo may it should be by controller?
                } catch (NotepadException exc) {
                    log.error("", exc);
                }
            }
        });
    }

    public FontMetrics getMetrics() {
        return metrics;
    }

    public FontRenderContext getFontRenderContext() {
        return frc;
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 480);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        final Graphics2D g2d = (Graphics2D) g;
        final int drawPosX = 0;
        final int drawPosY = 0;

        g2d.translate(drawPosX, drawPosY);
        if (otherModel.isShowSelection()) {
            drawDragged(g2d);
        }
        drawLayouts(g2d);
        drawCaret(g2d);
    }

    private void drawLayouts(Graphics2D g2d) {
        for (final TextLayoutInfo layoutInfo : windowModel.getLayouts()) {
            g2d.setBackground(BACKGROUND);
            g2d.setColor(TEXT_COLOR);
            layoutInfo.getLayout().draw(g2d, layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
        }
    }

    private void drawCaret(Graphics2D g2d) {
        final TextLayoutInfo layoutInfo = caretModel.getCaretLayout();
        g2d.translate(layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
        final Shape[] carets = layoutInfo.getLayout().getCaretShapes(
                caretModel.getCaretPosition() - layoutInfo.getPosition());
        g2d.setBackground(BACKGROUND);
        g2d.setColor(CARET_COLOR);
        g2d.draw(carets[0]);
        g2d.translate(-layoutInfo.getOrigin().x, -layoutInfo.getOrigin().y);
    }

    private void drawDragged(Graphics2D g2d) {
        for (final TextLayoutInfo layoutInfo : windowModel.getLayouts()) {
            final SegmentL segment =
                    new SegmentL(layoutInfo.getPosition() + windowModel.getWindowPosition(),
                            windowModel.getWindowPosition() + layoutInfo.getPosition() + layoutInfo.getLayout().getCharacterCount());
            g2d.translate(layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
            final SegmentL intersection =
                    segment.intersection(selectionModel.getSelection());
            final Segment blackboxSegment = new Segment((int)
                    (intersection.getStart() - layoutInfo.getPosition() - windowModel.getWindowPosition()),
                    (int) (intersection.getEnd() - layoutInfo.getPosition() - windowModel.getWindowPosition()));
            if (blackboxSegment.getEnd() != blackboxSegment.getStart()) {
                layoutInfo.getLayout().addAttribute(TextAttribute.FOREGROUND, INV_TEXT_COLOR, blackboxSegment.getStart(), blackboxSegment.getEnd());
                layoutInfo.getLayout().addAttribute(TextAttribute.BACKGROUND, INV_BACKGROUND, blackboxSegment.getStart(), blackboxSegment.getEnd());
            }
            g2d.translate(-layoutInfo.getOrigin().x, -layoutInfo.getOrigin().y);
        }
    }
}
