package notepad.view.textlayout;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Arrays;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class NonEmptyTextLayout implements SmartTextLayout {
    private static final Logger log = Logger.getLogger(NonEmptyTextLayout.class);
    private TextLayout textLayout;
    private AttributedString attributedString;
    private FontRenderContext frc;
    private boolean isNewLine;

    public NonEmptyTextLayout(boolean isNewLine, String text, Font f, FontRenderContext frc) {
       /* if (isNewLine) {
            text = text + " ";
        }*/
        this.isNewLine = isNewLine;

        this.frc = frc;
        attributedString = new AttributedString(text);
        attributedString.addAttribute(TextAttribute.FONT, f);
        textLayout = new TextLayout(attributedString.getIterator(), frc);
    }

    public NonEmptyTextLayout(TextLayout textLayout, AttributedString attributedString, FontRenderContext frc) {
        this.textLayout = textLayout;
        this.attributedString = attributedString;
        this.frc = frc;
    }

    @Override
    public int getFullCharacterCount() {
        return textLayout.getCharacterCount() + (isNewLine ? 1 : 0);
    }

    @Override
    public TextHitInfo hitTestChar(int x, int y) {
        TextHitInfo textHitInfo = textLayout.hitTestChar(x + 1, y);
        if(x <= getPoint(textHitInfo.getCharIndex()).x) {
            return textHitInfo;
        }
        return TextHitInfo.afterOffset(textHitInfo.getCharIndex() + 1);
    }

    @Override
    public String toString() {
        return "NonEmptyTextLayout [" + textLayout.toString() + "]";
    }

    @Override
    //this equals only for test purposes
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NonEmptyTextLayout that = (NonEmptyTextLayout) o;


        if (frc != null ? !frc.equals(that.frc) : that.frc != null) return false;
        if (textLayout != null ? textLayout.getCharacterCount() != that.textLayout.getCharacterCount() : that.textLayout != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = textLayout != null ? textLayout.hashCode() : 0;
        result = 31 * result + (attributedString != null ? attributedString.hashCode() : 0);
        result = 31 * result + (frc != null ? frc.hashCode() : 0);
        return result;
    }

    @Override
    public Point getPoint(final int index){
        Point p = new Point(0,0);
        textLayout.hitToPoint(TextHitInfo.afterOffset(index), p);
        return p;
    }

    @Override
    public Shape[] getCaretShapes(int index) {
        return textLayout.getCaretShapes(index);
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        textLayout.draw(g2d, x, y);
    }

    @Override
    public void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj, int start, int end) {
        attributedString.addAttribute(attribute, obj, start, end);
        textLayout = new TextLayout(attributedString.getIterator(), frc);
    }

    @Override
    public SmartTextLayout clone(){
        return new NonEmptyTextLayout(textLayout, new AttributedString(attributedString.getIterator()), frc);
    }
}
