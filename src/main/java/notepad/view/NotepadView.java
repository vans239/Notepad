package notepad.view;

import notepad.NotepadException;
import notepad.controller.NotepadController;
import notepad.controller.event.CaretEvent;
import notepad.utils.Segment;
import notepad.utils.SegmentL;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Date;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class NotepadView extends JPanel {
    private static final Logger log = Logger.getLogger(NotepadView.class);
    private static final int DEFAULT_LENGTH = 20480;

    private static final Color CARET_COLOR = Color.red;
    private static final Color TEXT_COLOR = Color.black;
    private int maxLength = DEFAULT_LENGTH;
    private long viewPosition = 0;
    private int caretPosition = 0;
    private String text;
    private NotepadController controller;
    private ArrayList<TextLayoutInfo> layouts = new ArrayList<TextLayoutInfo>();
    private FontRenderContext frc;

    public NotepadView(final NotepadController controller) throws NotepadException {
        this.controller = controller;
        update();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                update();
            }
        });
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

    public void updateScrollShift(final int shift) throws NotepadException {
        viewPosition = new SegmentL(0, controller.length()).nearest(viewPosition + shift);
    }

    public boolean isAvailableShiftScroll(final int shift) throws NotepadException {
        return new SegmentL(0, controller.length()).in(viewPosition + shift);
    }

    public SegmentL getAvailableScrollShift() throws NotepadException {
        return new SegmentL(-viewPosition, controller.length() - viewPosition);
    }

    public void updateCaretShift(final int shift) {
        final Segment segment = getAvailableCaretShift();
        if (segment.in(shift)) {
            caretPosition += shift;
        } else {
            try {
                final SegmentL segmentL = getAvailableScrollShift();
                viewPosition += segmentL.nearest(shift);
            } catch (NotepadException e) {
                log.error("", e);
            }
        }
    }

    public void updateCaretGoTo(final int value) {
        caretPosition = value;
    }

    public ArrayList<TextLayoutInfo> getLayouts() {
        return layouts;
    }

    public boolean isAvailableShiftCaret(final int shift) {
        return new Segment(0, text.length()).in(caretPosition + shift);
    }

    public Segment getAvailableCaretShift() {
        return new Segment(-caretPosition, text.length() - caretPosition);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        if (text.isEmpty()) {
            return;           //todo empty text edition
        }
        final Graphics2D g2d = (Graphics2D) g;
        final int drawPosX = 0;
        final int drawPosY = 0;

        g2d.translate(drawPosX, drawPosY);
        if (frc == null) {
            frc = g2d.getFontRenderContext();
            initLayouts();
        } else {
            frc = g2d.getFontRenderContext();
        }
        drawLayouts(g2d);
        drawCaret(g2d);
    }

    public void updateCaret(final Point clicked) {
        TextLayoutInfo nearestLayout = layouts.get(0);
        for (final TextLayoutInfo layoutInfo : layouts) {
            if (distance(layoutInfo, clicked) < distance(nearestLayout, clicked)) {
                nearestLayout = layoutInfo;
            }
        }
        final TextHitInfo hitInfo = nearestLayout.getLayout().hitTestChar(clicked.x, clicked.y);
        caretPosition = nearestLayout.getPosition() + hitInfo.getInsertionIndex();
    }

    private int distance(final TextLayoutInfo textLayoutInfo, final Point clicked) {
        return Math.abs(textLayoutInfo.getOrigin().y - clicked.y);
    }

    public void update() {
        try {
            updateText();
        } catch (NotepadException e) {
            log.error("", e);
        }
        repaint();
    }

    private void updateText() throws NotepadException {
        text = controller.get(viewPosition, Math.min((int) (controller.length() - viewPosition), maxLength));
        if (caretPosition > text.length()) {
            caretPosition = text.length();
        }
        if (frc != null) {
            initLayouts();
        }
    }

    private void initLayouts() {
        layouts.clear();
        int breakWidth = getSize().width;
        int height = getSize().height;
        int x = 0;
        int y = 0;
        int position = 0;

        String lines[] = text.split("\n");
        for (final String line : lines) {
            if (line.isEmpty()) {
//               final TextLayout layout = new TextLayout(new AttributedString("").getIterator(), frc);
//                y += layout.getAscent() + layout.getDescent() + layout.getLeading();
//                layouts.add(new TextLayoutInfo(layout, new Point(x, y), position));
                continue;
            }

            final AttributedCharacterIterator paragraph = new AttributedString(line).getIterator();
            final LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

            //todo write own line measurer
            lineMeasurer.setPosition(paragraph.getBeginIndex());
//            log.info("Start");
            int i = 0;
            while (lineMeasurer.getPosition() < paragraph.getEndIndex() && y < height) {
                final TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                y += layout.getAscent() + layout.getDescent() + layout.getLeading();
                layouts.add(new TextLayoutInfo(layout, new Point(x, y), position));
                position += layout.getCharacterCount();
                i++;
            }
//            log.info("End" + i);


            if (y > height) {
                final TextLayoutInfo textLayoutInfo = layouts.get(layouts.size() - 1);
                layouts.remove(layouts.size() - 1);
                text = text.substring(0, textLayoutInfo.getPosition());
                break;
            }
            position++;
        }
    }

    private void drawLayouts(Graphics2D g2d) {
        for (final TextLayoutInfo layoutInfo : layouts) {
            layoutInfo.getLayout().draw(g2d, layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
        }
    }

    private void drawCaret(Graphics2D g2d) {
        for (int i = 0; i < layouts.size(); ++i) {
            final TextLayoutInfo layoutInfo = layouts.get(i);
            if (caretInThisTextLayout(layoutInfo, i == layouts.size() - 1)) {
                g2d.translate(layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
                final Shape[] carets = layoutInfo.getLayout().getCaretShapes(
                        caretPosition - layoutInfo.getPosition());
                g2d.setColor(CARET_COLOR);
                g2d.draw(carets[0]);
                g2d.setColor(TEXT_COLOR);
                g2d.translate(-layoutInfo.getOrigin().x, -layoutInfo.getOrigin().y);
            }
        }
    }

    //todo private?
    public boolean caretInThisTextLayout(TextLayoutInfo layoutInfo, boolean isLastLayout) {
        int from = layoutInfo.getPosition();
        int to = from + layoutInfo.getLayout().getCharacterCount();
        return (caretPosition >= from && caretPosition < to)
                || (caretPosition > from && isLastLayout);
    }
}
