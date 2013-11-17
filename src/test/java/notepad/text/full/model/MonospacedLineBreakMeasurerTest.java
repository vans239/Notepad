package notepad.text.full.model;

import notepad.view.MonospacedLineBreakMeasurer;
import notepad.view.textlayout.EmptyTextLayout;
import notepad.view.textlayout.NonEmptyTextLayout;
import notepad.view.textlayout.SmartTextLayout;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;

public class MonospacedLineBreakMeasurerTest {
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    private static FontRenderContext frc = new FontRenderContext(null, false, false);
    private static int width = 640;
    private static FontMetrics fontMetrics;
    {
        Canvas c = new Canvas();
        fontMetrics = c.getFontMetrics(FONT);
    }


    private void assertLayout(MonospacedLineBreakMeasurer measurer, ArrayList<SmartTextLayout> expected){
        ArrayList<SmartTextLayout> actual = new ArrayList<SmartTextLayout>();
        for(SmartTextLayout layout : measurer){
            actual.add(layout);
        }
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void emptyText(){
        ArrayList<SmartTextLayout> layouts = new ArrayList<SmartTextLayout>();
        layouts.add(new EmptyTextLayout(false, fontMetrics));
        assertLayout(new MonospacedLineBreakMeasurer("", fontMetrics, frc, width), layouts);
    }

    @Test
    public void emptyLinesText(){
        ArrayList<SmartTextLayout> layouts = new ArrayList<SmartTextLayout>();
        layouts.add(new EmptyTextLayout(true, fontMetrics));
        layouts.add(new EmptyTextLayout(false, fontMetrics));
        assertLayout(new MonospacedLineBreakMeasurer("\n", fontMetrics, frc, width), layouts);
    }

    @Test
    public void smartText(){
        ArrayList<SmartTextLayout> layouts = new ArrayList<SmartTextLayout>();
        layouts.add(new NonEmptyTextLayout(true, "abc", FONT, frc));
        layouts.add(new EmptyTextLayout(true, fontMetrics));
        layouts.add(new EmptyTextLayout(false, fontMetrics));
        assertLayout(new MonospacedLineBreakMeasurer("abc\n\n", fontMetrics, frc, width), layouts);
    }

    @Test
    public void smartText2(){
        ArrayList<SmartTextLayout> layouts = new ArrayList<SmartTextLayout>();
        layouts.add(new NonEmptyTextLayout(true, "a", FONT, frc));
        layouts.add(new EmptyTextLayout(false, fontMetrics));
        assertLayout(new MonospacedLineBreakMeasurer("a\n", fontMetrics, frc, width), layouts);
    }
}
