package notepad.view;

import notepad.view.textlayout.EmptyTextLayout;
import notepad.view.textlayout.NonEmptyTextLayout;
import notepad.view.textlayout.SmartTextLayout;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.Iterator;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MonospacedLineBreakMeasurer implements Iterable<SmartTextLayout>{
    private static final Logger log = Logger.getLogger(MonospacedLineBreakMeasurer.class);
    private static final String lineSeparator = "\n";

    private final Font font;
    private final FontRenderContext frc;
    private final FontMetrics fontMetrics;
    private final int symbolWidth;
    private final int wrappingWidth;
    private String text;

    public MonospacedLineBreakMeasurer(String text, FontMetrics fontMetrics, FontRenderContext frc, int wrappingWidth) {
        this.text = text;
        this.font = fontMetrics.getFont();
        this.frc = frc;
        if (font == null || !Font.MONOSPACED.equals(font.getName())) {
            throw new IllegalArgumentException("Font isn't monospaced");
        }
        this.symbolWidth = fontMetrics.charWidth('a');
        this.fontMetrics = fontMetrics;
        this.wrappingWidth = wrappingWidth;
    }

    @Override
    public Iterator<SmartTextLayout> iterator() {
        return new Iterator<SmartTextLayout>() {
            private final int maxSymbols = wrappingWidth / symbolWidth;

            private boolean endLine = true;
            private String text = MonospacedLineBreakMeasurer.this.text;

            @Override
            public boolean hasNext() {
                return endLine || !text.isEmpty();
            }

            @Override
            public SmartTextLayout next() {
                endLine = false;
                int end;
                SmartTextLayout curr;
                if(text.isEmpty()){
                    return new EmptyTextLayout(false, fontMetrics);
                }
                if("\n".equals(text)){
                    text = "";
                    return new EmptyTextLayout(true, fontMetrics);
                }
                if (text.length() < maxSymbols) {
                    end = text.length();
                    curr = new NonEmptyTextLayout(false, text.substring(0, end), font, frc);
                } else {
                    int min = Math.min(maxSymbols, text.length());
                    end = text.substring(0, min).lastIndexOf(' ') + 1;
                    if (end == 0) {
                        end = min;
                    }
                    curr = new NonEmptyTextLayout(false, text.substring(0, end), font, frc);
                }

                int separatorIndex = text.indexOf(lineSeparator);
                if (separatorIndex != -1 && separatorIndex + 1 < end) {
                    end = separatorIndex + 1;
                    if (end != 0) {
                        curr = new NonEmptyTextLayout(true, text.substring(0, end - 1), font, frc);
                    } else {
                        curr = new EmptyTextLayout(true, fontMetrics);
                    }
                    endLine = true;
                }
                text = text.substring(end);
                return curr;
            }

            @Override
            public void remove() {
                throw new NotImplementedException();
            }
        };
    }
}
