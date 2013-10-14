/*
package notepad.view;

import notepad.NotepadException;
import notepad.controller.NotepadController;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.ArrayList;

*/
/**
 * Evgeny Vanslov
 * vans239@gmail.com
 *//*

public class TextLineModel {
    private static final Logger log = Logger.getLogger(TextLineModel.class);
    private FontRenderContext frc = getFontMetrics(font).getFontRenderContext();
    private ArrayList<TextLayoutInfo> layouts = new ArrayList<TextLayoutInfo>();
    private String text;
    private NotepadController controller;

    private void updateText() throws NotepadException {
        text = controller.get(viewPosition, Math.min((int) (controller.length() - viewPosition), maxLength));
        if (caretPosition > text.length()) {
            caretPosition = text.length();
        }
        initLayouts();
    }

    private void initLayouts() {
        layouts.clear();
        int breakWidth = getSize().width;
        int height = getSize().height;
        int x = 0;
        int y = 0;
        int position = 0;
        final String lineSeparator = "\n";
        String lines[] = text.split(lineSeparator);
        for (String line : lines) {
            if (y > height) {
                break;
            }
            line += " " ; //lineSeparator.length
            if (line.isEmpty()) {
                final TextLayout layout = new TextLayout(new AttributedString(" ").getIterator(), frc);
                y += layout.getAscent() + layout.getDescent() + layout.getLeading();
                layouts.add(new TextLayoutInfo(layout, new Point(x, y), position));
                continue;
            }
            final MonospacedLineBreakMeasurer lineMeasurer =
                    new MonospacedLineBreakMeasurer(line, getFontMetrics(font), frc);
            lineMeasurer.setPosition(0);
            while (lineMeasurer.getPosition() < line.length() && y <= height) {
                final TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                y += layout.getAscent() + layout.getDescent() + layout.getLeading();
                layouts.add(new TextLayoutInfo(layout, new Point(x, y), position));
                position += layout.getCharacterCount();
            }
        }
        if (y > height) {
            final TextLayoutInfo textLayoutInfo = layouts.get(layouts.size() - 1);
            layouts.remove(layouts.size() - 1);
            text = text.substring(0, textLayoutInfo.getPosition());
            caretPosition = Math.min(text.length(), caretPosition);
        }
    }

    private void draw(Graphics2D g2d) {
        for (final TextLayoutInfo layoutInfo : layouts) {
            layoutInfo.getLayout().draw(g2d, layoutInfo.getOrigin().x, layoutInfo.getOrigin().y);
        }
    }

    public ArrayList<TextLayoutInfo> getLayouts() {
        return layouts;
    }
}
*/
