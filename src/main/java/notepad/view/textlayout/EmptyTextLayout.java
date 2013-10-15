package notepad.view.textlayout;

import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.GeneralPath;
import java.text.AttributedCharacterIterator;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class EmptyTextLayout implements SmartTextLayout {
    private static final Logger log = Logger.getLogger(EmptyTextLayout.class);
    private boolean isNewLine;
    private FontMetrics metrics;

    @Override
    public int getCharacterCount() {
        return isNewLine ? 1 : 0;
    }

    public EmptyTextLayout(boolean isNewLine, FontMetrics metrics){
        this.isNewLine = isNewLine;
        this.metrics = metrics;
    }

    @Override
    public TextHitInfo hitTestChar(int x, int y) {
        return TextHitInfo.leading(0);
    }

    @Override
    public Shape[] getCaretShapes(int index) {
        if(index != 0){
            throw new IllegalArgumentException("No such element");
        }
        final Shape shapes[] = new Shape[1];
        final GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 2);
        shape.moveTo(0, -metrics.getAscent());
        shape.lineTo(0, metrics.getDescent());
        shapes[0] = shape;
        return shapes;
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
    }

    @Override
    public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj, int start, int end) {
    }
}
