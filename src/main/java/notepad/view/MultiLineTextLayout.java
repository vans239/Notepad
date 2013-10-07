package notepad.view;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

public class MultiLineTextLayout {
    private static final Color CARET_COLOR = Color.red;
    private static final Color TEXT_COLOR = Color.black;

    private Dimension size;
    private String text;
    private ArrayList<TextLayoutInfo> layouts = new ArrayList<TextLayoutInfo>();
    private int caretPosition;

    public MultiLineTextLayout(final String text, final int caret, final Dimension size) {
        this.text = text;
        this.size = size;
        this.caretPosition = caret;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public ArrayList<TextLayoutInfo> getLayouts() {
        return layouts; //todo immutable?
    }

    public void updateCaret(final int shift) {
        if (caretPosition + shift > text.length()) {
            caretPosition = text.length();
        } else if (caretPosition + shift < 0) {
            caretPosition = 0;
        } else {
            caretPosition += shift;
        }
    }

    public void draw(Graphics2D g2d, final int drawPosX, final int drawPosY) {
        g2d.translate(drawPosX, drawPosY);
        initLayouts(g2d);
        drawLayouts(g2d);
        drawCaret(g2d);
    }

    private void initLayouts(Graphics2D g2d) {
        layouts.clear();
        final FontRenderContext frc = g2d.getFontRenderContext();
        int breakWidth = size.width;
        int x = 0;
        int y = 0;
        int position = 0;

        String lines[] = text.split("\n");

        for (final String line : lines) {
            if(y > size.getHeight()){
                break;
            }
            if(line.isEmpty()){
//               final TextLayout layout = new TextLayout(new AttributedString("").getIterator(), frc);
//                y += layout.getAscent() + layout.getDescent() + layout.getLeading();
//                layouts.add(new TextLayoutInfo(layout, new Point(x, y), position));
                continue;
            }
            final AttributedCharacterIterator paragraph = new AttributedString(line).getIterator();
            final LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
            //todo write own line measurer
            lineMeasurer.setPosition(paragraph.getBeginIndex());
            while (lineMeasurer.getPosition() < paragraph.getEndIndex() && y < size.getHeight()) {
                final TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                y += layout.getAscent() + layout.getDescent() + layout.getLeading();
                layouts.add(new TextLayoutInfo(layout, new Point(x, y), position));
                position += layout.getCharacterCount();
            }
            if (y > size.getHeight()) {
                final TextLayoutInfo textLayoutInfo = layouts.get(layouts.size() - 1);
                layouts.remove(layouts.size() - 1);
                text = text.substring(0, textLayoutInfo.getPosition());
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