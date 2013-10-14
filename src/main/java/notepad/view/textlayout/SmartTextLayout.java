package notepad.view.textlayout;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.TextHitInfo;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface SmartTextLayout {
    int getCharacterCount();
    TextHitInfo hitTestChar(int x, int y);
    Shape getLogicalHighlightShape(int firstEndpoint, int secondEndpoint);
    Shape[] getCaretShapes(int index);
    void draw(Graphics2D g2d, int x, int y);
}
