package notepad.view;

import notepad.NotepadException;
import notepad.controller.NotepadController;
import notepad.utils.Segment;
import notepad.utils.SegmentL;
import notepad.view.textlayout.SmartTextLayout;
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
    private static final int MAX_LINE_LENGTH = 500;

    private final FontMetrics metrics = getFontMetrics(FONT);

    private int maxLength;
    private long viewPosition = 0;
    private int caretPosition = 0;

    private SegmentL selectionSegment;

    private boolean isShowSelection = false;
    private String text;
    private NotepadController controller;
    private ArrayList<TextLayoutInfo> layouts = new ArrayList<TextLayoutInfo>();
    private FontRenderContext frc = getFontMetrics(FONT).getFontRenderContext();

    public NotepadView(final NotepadController controller) throws NotepadException {
        this.controller = controller;
        updateMaxLength();
        update();
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updateMaxLength();
                update();
            }
        });
    }

    public boolean isShowSelection() {
        return isShowSelection;
    }

    public void updateSelectionSegment(SegmentL draggedSegment) {
        this.selectionSegment = draggedSegment;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public void showSelectionSegment(boolean isShow) {
        isShowSelection = isShow;
    }

    public SegmentL getSelectionSegment() {
        return selectionSegment;
    }

    //todo viewposition set to 0 after open new file!
    public void updateMaxLength() {
        //todo find real coeff
        maxLength = 3 * (getSize().width / getFontMetrics(FONT).charWidth('a')) * (getSize().height / getFontMetrics(FONT).getHeight()) / 2;
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

    public int scrollNext() throws NotepadException {
        TextLayoutInfo textLayoutInfo = layouts.get(0);
        SmartTextLayout nextTextLayout = getNextTextLayout();
        viewPosition += textLayoutInfo.getLayout().getCharacterCount();
        caretPosition -= textLayoutInfo.getLayout().getCharacterCount();
        return nextTextLayout.getCharacterCount();
    }

    public int scrollPrev() throws NotepadException {
        SmartTextLayout prevTextLayout = getPrevTextLayout();
        viewPosition -= prevTextLayout.getCharacterCount();
        return prevTextLayout.getCharacterCount();
    }

    public void scrollLeft() throws NotepadException {
        if (viewPosition == 0) {
            return;
        }
        scrollPrev();
        caretPosition--;
    }

    public void scrollRight() throws NotepadException {
        if (getViewPosition() + text.length() == controller.length()) {
            caretPosition = text.length();
            return;
        }
        scrollNext();
        caretPosition++;
    }

    public void scrollUp() throws NotepadException {
        if (viewPosition == 0) {
            return;
        }
        int prevCount = scrollPrev();
        caretPosition = Math.min(caretPosition, prevCount - 1);
    }

    public void scrollDown() throws NotepadException {
        if (getViewPosition() + text.length() == controller.length()) {
            return;
        }
        int nextCount = scrollNext();
        int newTextLength = text.length() - layouts.get(0).getLayout().getCharacterCount() + nextCount;
        int currLineLength = layouts.get(layouts.size() - 1).getLayout().getCharacterCount();
        if (newTextLength - getCaretPosition() - 1 < currLineLength) {
            caretPosition += newTextLength - getCaretPosition() - 1;
            if (getEditPosition() == controller.length() - 1) {
                caretPosition++;
            }
        } else {
            caretPosition += currLineLength;
        }
    }

    public SmartTextLayout getPrevTextLayout() throws NotepadException {
        long startText = Math.max(getViewPosition() - MAX_LINE_LENGTH, 0);
        String prevText = controller.get(startText, (int) (viewPosition - startText));
        int from;
        if (prevText.lastIndexOf('\n') == prevText.length() - 1) {
            from = prevText.substring(0, prevText.length() - 1).lastIndexOf('\n') + 1;
        } else {
            from = prevText.lastIndexOf('\n') + 1;
        }
        prevText = prevText.substring(from);

        SmartTextLayout textLayout = null;
        final MonospacedLineBreakMeasurer lineMeasurer =
                new MonospacedLineBreakMeasurer(prevText, getFontMetrics(FONT), frc, getWidth());
        for (final SmartTextLayout stl : lineMeasurer) {
            textLayout = stl;
        }
        return textLayout;
    }

    public SmartTextLayout getNextTextLayout() throws NotepadException {
        final String nextText = controller.get(getViewPosition() + text.length(),
                (int) Math.min(MAX_LINE_LENGTH, controller.length() - (getViewPosition() + text.length())));
        final MonospacedLineBreakMeasurer lineMeasurer =
                new MonospacedLineBreakMeasurer(nextText, getFontMetrics(FONT), frc, getWidth());
        return lineMeasurer.iterator().next();
    }

    //todo left, right on caret shift

    public void updateCaretShift(final int shift) throws NotepadException {
        final Segment segment = getAvailableCaretShift();
        if (!segment.in(shift)) {
            throw new IllegalArgumentException();
        }
        caretPosition += shift;
        log.info(String.format("New caret pos[%d]", caretPosition));
    }

    public void updateCaretGoTo(final long value) {
        caretPosition = Long.valueOf(value - viewPosition).intValue();
        log.info(String.format("New caret pos[%d]", caretPosition));
    }

    public ArrayList<TextLayoutInfo> getLayouts() {
        return layouts;
    }

    public Segment getAvailableCaretShift() {
        return new Segment(-caretPosition, text.length() - caretPosition);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        final Graphics2D g2d = (Graphics2D) g;
        final int drawPosX = 0;
        final int drawPosY = 0;

        g2d.translate(drawPosX, drawPosY);
        if (isShowSelection) {
            drawDragged(g2d);
        }
        drawLayouts(g2d);
        drawCaret(g2d);
    }

    public void update() {
        try {
            updateText();
        } catch (NotepadException e) {
            log.error("Can't update text for view", e);
        }
        repaint();
    }

    private void updateText() throws NotepadException {
        text = controller.get(viewPosition, Math.min((int) (controller.length() - viewPosition), maxLength));
        initLayouts();
    }

    private void initLayouts() {
        layouts.clear();
        int breakWidth = getSize().width;
        int height = getSize().height;
        int x = 0;
        int y = 0;
        int position = 0;

        final MonospacedLineBreakMeasurer lineMeasurer =
                new MonospacedLineBreakMeasurer(text, getFontMetrics(FONT), frc, breakWidth);
        for (final SmartTextLayout layout : lineMeasurer) {
            y += metrics.getHeight();
            TextLayoutInfo layoutInfo = new TextLayoutInfo(layout, new Point(x, y), position);
            layouts.add(layoutInfo);
            position += layout.getCharacterCount();
            if (y + metrics.getHeight() > height) {
                break;
            }
        }
        text = text.substring(0, position);
        caretPosition = Math.min(text.length(), caretPosition); // in case resize
    }

    private void drawLayouts(Graphics2D g2d) {
        for (final TextLayoutInfo layoutInfo : layouts) {
            g2d.setBackground(BACKGROUND);
            g2d.setColor(TEXT_COLOR);
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
                g2d.setBackground(BACKGROUND);
                g2d.setColor(CARET_COLOR);
                g2d.draw(carets[0]);
                g2d.translate(-layoutInfo.getOrigin().x, -layoutInfo.getOrigin().y);
            }
        }
    }

    private void drawDragged(Graphics2D g2d) {
        for (int i = 0; i < layouts.size(); ++i) {
            final TextLayoutInfo layoutInfo = layouts.get(i);
            final SegmentL segment =
                    new SegmentL(layoutInfo.getPosition() + viewPosition, viewPosition + layoutInfo.getPosition() + layoutInfo.getLayout().getCharacterCount());
            g2d.translate(layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
            final SegmentL intersection =
                    segment.intersection(selectionSegment);
            final Segment blackboxSegment = new Segment((int) (intersection.getStart() - layoutInfo.getPosition() - viewPosition), (int) (intersection.getEnd() - layoutInfo.getPosition() - viewPosition));
            if (blackboxSegment.getEnd() != blackboxSegment.getStart()) {
                layoutInfo.getLayout().addAttribute(TextAttribute.FOREGROUND, INV_TEXT_COLOR, blackboxSegment.getStart(), blackboxSegment.getEnd());
                layoutInfo.getLayout().addAttribute(TextAttribute.BACKGROUND, INV_BACKGROUND, blackboxSegment.getStart(), blackboxSegment.getEnd());
            }
            g2d.translate(-layoutInfo.getOrigin().x, -layoutInfo.getOrigin().y);
        }
    }

    //todo private?
    public boolean caretInThisTextLayout(TextLayoutInfo layoutInfo, boolean isLastLayout) {
        int from = layoutInfo.getPosition();
        int to = from + layoutInfo.getLayout().getCharacterCount();
        return (caretPosition >= from && caretPosition < to)
                || (caretPosition >= from && isLastLayout);
    }
}
