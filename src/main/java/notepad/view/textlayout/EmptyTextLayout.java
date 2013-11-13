package notepad.view.textlayout;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
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

    public EmptyTextLayout(boolean isNewLine, FontMetrics metrics) {
        this.isNewLine = isNewLine;
        this.metrics = metrics;
    }

    @Override
    public TextHitInfo hitTestChar(int x, int y) {
        return TextHitInfo.leading(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmptyTextLayout that = (EmptyTextLayout) o;

        if (isNewLine != that.isNewLine) return false;
        if (metrics != null ? !metrics.equals(that.metrics) : that.metrics != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isNewLine ? 1 : 0);
        result = 31 * result + (metrics != null ? metrics.hashCode() : 0);
        return result;
    }

    @Override
    public Shape[] getCaretShapes(int index) {
        if (index != 0) {
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
    public String toString() {
        return "EmptyTextLauout [" + isNewLine + "]";
    }

    @Override
    public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj, int start, int end) {
    }

    @Override
    public EmptyTextLayout clone() {
        return this; //object is immutable
    }

    @Override
    public Point getPoint(int index) {
        return new Point(0,0);
    }
}
