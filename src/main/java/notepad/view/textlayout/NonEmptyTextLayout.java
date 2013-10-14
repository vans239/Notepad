package notepad.view.textlayout;

import notepad.utils.Segment;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class NonEmptyTextLayout implements SmartTextLayout {
    private static final Logger log = Logger.getLogger(NonEmptyTextLayout.class);
    private TextLayout textLayout;
    private AttributedString attributedString;
    private FontRenderContext frc;
    public NonEmptyTextLayout(boolean isNewLine, String text, Font f, FontRenderContext frc){
        if(isNewLine){
            text = text + " ";
        }
        this.frc = frc;
        attributedString = new AttributedString(text);
        attributedString.addAttribute(TextAttribute.FONT, f);
        textLayout = new TextLayout(attributedString.getIterator(), frc);
    }

    @Override
    public int getCharacterCount() {
        return textLayout.getCharacterCount();
    }

    @Override
    public TextHitInfo hitTestChar(int x, int y) {
        return textLayout.hitTestChar(x, y);
    }

    @Override
    public Shape[] getCaretShapes(int index) {
        return textLayout.getCaretShapes(index);
    }

    @Override
    public Shape getLogicalHighlightShape(int firstEndpoint, int secondEndpoint) {
        return textLayout.getLogicalHighlightShape(firstEndpoint, secondEndpoint);
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
}
