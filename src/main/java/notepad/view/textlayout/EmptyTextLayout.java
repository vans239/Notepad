package notepad.view.textlayout;

import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class EmptyTextLayout implements SmartTextLayout {
    private static final Logger log = Logger.getLogger(EmptyTextLayout.class);

    @Override
    public int getCharacterCount() {
        return 1;

    }

    public EmptyTextLayout(Font f, FontRenderContext frc){
    }

    @Override
    public TextHitInfo hitTestChar(int x, int y) {
        return TextHitInfo.leading(0);
    }

    @Override
    public Shape getLogicalHighlightShape(int firstEndpoint, int secondEndpoint) {
        return new Rectangle();
    }

    @Override
    public Shape[] getCaretShapes(int index) {
        return null;
    }
    @Override
    public void draw(Graphics2D g2d, int x, int y) {
    }
}
