package notepad.view;

import notepad.view.textlayout.EmptyTextLayout;
import notepad.view.textlayout.NonEmptyTextLayout;
import notepad.view.textlayout.SmartTextLayout;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.FontRenderContext;

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

    public SmartTextLayout nextLayout(float wrappingWidth) {
        int maxSymbols = (int) (wrappingWidth / symbolWidth);
        String s = text.substring(position);
        int end;
        if (s.length() < maxSymbols) {
            end = s.length();
        } else {
            end = s.substring(0, Math.min(maxSymbols, s.length())).lastIndexOf(' ') + 1;
            if (end == 0) {
                end = Math.min(s.length(), maxSymbols);
            }
        }
        position += end;
        if(s.substring(0, end).isEmpty()){
            return new EmptyTextLayout(font, frc);
        }
        return new NonEmptyTextLayout(s.substring(0, end), font, frc);
    }
}
