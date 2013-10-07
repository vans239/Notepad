package notepad.temp;

import notepad.temp.SampleUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.AttributedCharacterIterator;

/**
 * This class demonstrates how to hit-test a TextLayout.  Hit-testing
 * is the mapping of a graphical location to a character position within
 * text.
 *
 * This class constructs a TextLayout from an AttributedCharacterIterator
 * and displays the TextLayout.  When the mouse is clicked inside this
 * Component, the mouse position is mapped to a character position, and the
 * carets for this character position are displayed.
 */
public class HitTestSample extends Component {
    private static final Color STRONG_CARET_COLOR = Color.red;
    private static final Color WEAK_CARET_COLOR = Color.black;

    private TextLayout textLayout;
    private int insertionIndex;

    public HitTestSample() {
        FontRenderContext frc = new FontRenderContext(null, false, false);
        textLayout = new TextLayout(SampleUtils.longEnglish.getIterator(), frc);
        insertionIndex = 0;
        addMouseListener(new HitTestMouseListener());
    }

    private Point2D computeLayoutOrigin() {
        Dimension size = getSize();
        Point2D.Float origin = new Point2D.Float();
        origin.x = (float) (size.width - textLayout.getAdvance()) / 2;
        origin.y = (float) (size.height - textLayout.getDescent() + textLayout.getAscent()) / 2;
        return origin;
    }

    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        Point2D origin = computeLayoutOrigin();

        graphics2D.translate(origin.getX(), origin.getY());

        textLayout.draw(graphics2D, 0, 0);

        Shape[] carets = textLayout.getCaretShapes(insertionIndex);

        graphics2D.setColor(STRONG_CARET_COLOR);
        graphics2D.draw(carets[0]);

        if (carets[1] != null) {
            graphics2D.setColor(WEAK_CARET_COLOR);
            graphics2D.draw(carets[1]);
        }
    }

    private class HitTestMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            Point2D origin = computeLayoutOrigin();
            float clickX = (float) (e.getX() - origin.getX());
            float clickY = (float) (e.getY() - origin.getY());
            TextHitInfo currentHit = textLayout.hitTestChar(clickX, clickY);
            insertionIndex = currentHit.getInsertionIndex();
            repaint();
        }
    }

    public static void main(String[] args) {


        Component sample = new HitTestSample();
        SampleUtils.showComponentInFrame(sample, "Hit Test Sample");
    }
}