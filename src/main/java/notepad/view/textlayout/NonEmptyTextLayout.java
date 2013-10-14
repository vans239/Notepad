package notepad.view.textlayout;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class NonEmptyTextLayout implements SmartTextLayout {
    private static final Logger log = Logger.getLogger(NonEmptyTextLayout.class);
    private TextLayout textLayout;

    public NonEmptyTextLayout(String text, Font f, FontRenderContext frc){
        textLayout = new TextLayout(text, f, frc);
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
}
