package notepad.view.textlayout;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.text.AttributedCharacterIterator;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface SmartTextLayout {
    int getCharacterCount();

    TextHitInfo hitTestChar(int x, int y);

    Shape[] getCaretShapes(int index);

    void draw(Graphics2D g2d, int x, int y);

    void addAttribute(AttributedCharacterIterator.Attribute attribute, Object obj, int start, int end);
}
