package notepad.temp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class LineBreakPanel extends JPanel {

    private static final Hashtable<TextAttribute, Object> map =
            new Hashtable<TextAttribute, Object>();

    static {
        map.put(TextAttribute.FAMILY, "Serif");
        map.put(TextAttribute.SIZE, new Float(18.0));
    }

    private static AttributedString vanGogh = new AttributedString(
            "Many people believe that Vincent van Gogh painted his best works " +
                    "during the two-year period he spent in Provence. Here is where he " +
                    "painted The Starry Night--which some consider to be his greatest " +
                    "work of all. However, as his artistic brilliance reached new " +
                    "heights in Provence, his physical and mental health plummeted. ",
            map);

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        final Graphics2D g2d = (Graphics2D) g;

        final AttributedCharacterIterator paragraph = vanGogh.getIterator();
        final FontRenderContext frc = g2d.getFontRenderContext();
        final LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        int breakWidth = getSize().width;
        float drawPosY = 0;
        int drawPosX = 0;

        lineMeasurer.setPosition(paragraph.getBeginIndex());
        while (lineMeasurer.getPosition() < paragraph.getEndIndex()) {
            TextLayout layout = lineMeasurer.nextLayout(breakWidth);

            drawPosY += layout.getAscent();
            layout.draw(g2d, drawPosX, drawPosY);
            drawPosY += layout.getDescent() + layout.getLeading();
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Line Break Sample");

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.add(new LineBreakPanel());
        f.setSize(new Dimension(400, 250));
        f.setVisible(true);
    }
}
