package notepad.text.window;

import notepad.NotepadException;
import notepad.text.full.TextModel;
import notepad.view.MonospacedLineBreakMeasurer;
import notepad.view.TextLayoutInfo;
import notepad.view.textlayout.SmartTextLayout;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class TextWindowModel extends Observable {
    private static final int MAX_LINE_LENGTH = 500;

    private final TextModel textModel;

    private FontMetrics metrics;
    private FontRenderContext frc;

    private int maxLength;
    private long windowPosition = 0;
    private String text;
    private Dimension size;
    private ArrayList<TextLayoutInfo> layouts = new ArrayList<TextLayoutInfo>();

    public TextWindowModel(TextModel textModel) {
        this.textModel = textModel;
    }

    public void init(Dimension size, FontMetrics metrics, FontRenderContext frc){
        this.size = size;
        this.metrics = metrics;
        this.frc = frc;
        updateMaxLength();
    }

    private void updateMaxLength() {
        maxLength = 3 * (size.width / metrics.charWidth('a')) * (size.height / metrics.getHeight()) / 2;
    }

    public void setSize(Dimension size) throws NotepadException {
        this.size = size;
        updateMaxLength();
        update();
    }

    public long getWindowPosition() {
        return windowPosition;
    }

    public void update() throws NotepadException {
        text = textModel.get(windowPosition, Math.min((int) (textModel.length() - windowPosition), maxLength));
        initLayouts();
        notifyObservers();
    }

    public int getWindowLength(){
        return text.length();
    }

    /**
     * @return length of last line if we can scroll down, -1 otherwise
     * @throws NotepadException
     */
    public int down() throws NotepadException {
        TextLayoutInfo textLayoutInfo = layouts.get(0);
        SmartTextLayout nextTextLayout = getDownTextLayout();
        windowPosition += textLayoutInfo.getLayout().getCharacterCount();
        update();
        return nextTextLayout.getCharacterCount();
    }

    /**
     * @return length of first line if we can scroll up, -1 otherwise
     * @throws NotepadException
     */
    public int up() throws NotepadException {
        SmartTextLayout prevTextLayout = getUpTextLayout();
        windowPosition -= prevTextLayout.getCharacterCount();
        update();
        return prevTextLayout.getCharacterCount();
    }

    public void goTo(long windowPosition) throws NotepadException {
        this.windowPosition = windowPosition;
        update();
    }

    public ArrayList<TextLayoutInfo> getLayouts() {
        return layouts;
    }

    private SmartTextLayout getUpTextLayout() throws NotepadException {
        long startText = Math.max(getWindowPosition() - MAX_LINE_LENGTH, 0);
        String prevText = textModel.get(startText, (int) (getWindowPosition() - startText));
        int from;
        if (prevText.lastIndexOf('\n') == prevText.length() - 1) {
            from = prevText.substring(0, prevText.length() - 1).lastIndexOf('\n') + 1;
        } else {
            from = prevText.lastIndexOf('\n') + 1;
        }
        prevText = prevText.substring(from);

        SmartTextLayout textLayout = null;
        final MonospacedLineBreakMeasurer lineMeasurer =
                new MonospacedLineBreakMeasurer(prevText, metrics, frc, size.width);
        for (final SmartTextLayout stl : lineMeasurer) {
            textLayout = stl;
        }
        return textLayout;
    }

    private SmartTextLayout getDownTextLayout() throws NotepadException {
        final String nextText = textModel.get(getWindowPosition() + text.length(),
                (int) Math.min(MAX_LINE_LENGTH, textModel.length() - (getWindowPosition() + text.length())));
        final MonospacedLineBreakMeasurer lineMeasurer =
                new MonospacedLineBreakMeasurer(nextText, metrics, frc, size.width);
        return lineMeasurer.iterator().next();
    }

    private void initLayouts() {
        layouts.clear();
        int breakWidth = size.width;
        int height = size.height;
        int x = 0;
        int y = 0;
        int position = 0;

        final MonospacedLineBreakMeasurer lineMeasurer =
                new MonospacedLineBreakMeasurer(text, metrics, frc, breakWidth);
        for (final SmartTextLayout layout : lineMeasurer) {
            y += metrics.getHeight();
            TextLayoutInfo layoutInfo = new TextLayoutInfo(layout, new Point(x, y), position);
            layouts.add(layoutInfo);
            position += layout.getCharacterCount();
            if (y + metrics.getHeight() > height) {
                break;
            }
        }
        text = text.substring(0, position);
    }
}
