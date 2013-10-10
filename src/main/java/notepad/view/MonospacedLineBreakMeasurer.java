package notepad.view;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class MonospacedLineBreakMeasurer {
    private static final Logger log = Logger.getLogger(MonospacedLineBreakMeasurer.class);
    private String text;
    private Font font;
    private FontRenderContext frc;
    private int position;
    private int symbolWidth;

    public MonospacedLineBreakMeasurer(String text, FontMetrics fontMetrics, FontRenderContext frc) {
        this.text = text;
        this.font = fontMetrics.getFont();
        this.frc = frc;
        if (font == null || !Font.MONOSPACED.equals(font.getName())) {
            throw new IllegalArgumentException("Font isn't monospaced");
        }
        this.symbolWidth = fontMetrics.charWidth('a');
    }

    void setPosition(int pos) {
        position = pos;
    }

    public int getPosition() {
        return position;
    }

    public TextLayout nextLayout(float wrappingWidth) {
        int maxSymbols = (int) (wrappingWidth / symbolWidth);
        String s = text.substring(position);
        int end;
        //todo \r\n
        if (s.length() < maxSymbols) {
            end = s.length();
        } else {
            end = s.substring(0, Math.min(maxSymbols, s.length())).lastIndexOf(' ') + 1;
            if (end == 0) {
                end = Math.min(s.length(), maxSymbols);
            }
        }
        position += end;
        return new TextLayout(s.substring(0, end), font, frc);
    }
}
