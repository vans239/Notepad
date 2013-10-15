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

    private final FontMetrics metrics = getFontMetrics(FONT);
    private static final Color CARET_COLOR = Color.red;
    private static final Color TEXT_COLOR = Color.black;
    private static final Color INV_TEXT_COLOR = Color.white;
    private static final Color BACKGROUND = Color.white;
    private static final Color INV_BACKGROUND = Color.blue;


    private int maxLength;
    private long viewPosition = 0;
    private int caretPosition = 0;
    private Segment selectionSegment;

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

    public void updateSelectionSegment(Segment draggedSegment) {
        this.selectionSegment = draggedSegment;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public void showSelectionSegment(boolean isShow) {
        isShowSelection = isShow;
    }

    public Segment getSelectionSegment() {
        return selectionSegment;
    }

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

    public void scrollUp(){
        log.info("scroll up");
    }

    public void scrollDown(){
        log.info("scroll down");
    }

    public SegmentL getAvailableScrollShift() throws NotepadException {
        return new SegmentL(-viewPosition, Math.max(0, controller.length() - viewPosition - 1));
    }

    public void updateCaretShift(final int shift) throws NotepadException {
        final Segment segment = getAvailableCaretShift();
        if (segment.in(shift)) {
            caretPosition += shift;
        } else {
            //todo shift should be small now
            final SegmentL segmentL = getAvailableScrollShift();
            int realShift;
            if(shift > 0){
                realShift = layouts.get(0).getLayout().getCharacterCount();
            } else {
                realShift = -layouts.get(0).getLayout().getCharacterCount();
            }
            viewPosition += segmentL.nearest(realShift);
            caretPosition = (int) (Math.min(viewPosition + caretPosition - realShift + shift, controller.length()) - viewPosition);
        }
        if(caretPosition > text.length()){
            log.info("aa");
        }
        log.info(String.format("New caret pos[%d]", caretPosition));
    }

    public void updateCaretGoTo(final int value) {
        caretPosition = value;
        log.info(String.format("New caret pos[%d]", caretPosition));
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
        int draggedFrom = selectionSegment.getStart();
        int draggedTo = selectionSegment.getEnd();

        for (int i = 0; i < layouts.size(); ++i) {
            final TextLayoutInfo layoutInfo = layouts.get(i);
            final Segment segment =
                    new Segment(layoutInfo.getPosition(), layoutInfo.getPosition() + layoutInfo.getLayout().getCharacterCount());
            if (draggedFrom < segment.getEnd()) {
                g2d.translate(layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
                final Segment blackboxSegment =
                        new Segment(segment.nearest(draggedFrom) - layoutInfo.getPosition(), segment.nearest(draggedTo) - layoutInfo.getPosition());
                if (blackboxSegment.getEnd() != blackboxSegment.getStart()) {
                    layoutInfo.getLayout().addAttribute(TextAttribute.FOREGROUND, INV_TEXT_COLOR, blackboxSegment.getStart(), blackboxSegment.getEnd() );
                    layoutInfo.getLayout().addAttribute(TextAttribute.BACKGROUND, INV_BACKGROUND, blackboxSegment.getStart(), blackboxSegment.getEnd() );
                }
                g2d.translate(-layoutInfo.getOrigin().x, -layoutInfo.getOrigin().y);
            }
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
