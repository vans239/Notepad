package notepad.text.window;

import notepad.NotepadException;
import notepad.model.OtherModel;
import notepad.text.full.ChangeTextEvent;
import notepad.text.full.ChangeTextListener;
import notepad.text.full.TextModel;
import notepad.view.MonospacedLineBreakMeasurer;
import notepad.view.TextLayoutInfo;
import notepad.view.textlayout.SmartTextLayout;
import org.apache.log4j.Logger;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class TextWindowModel extends Observable {
    private static final Logger log = Logger.getLogger(TextWindowModel.class);
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
        textModel.addChangeTextListener(new ChangeTextListener() {
            @Override
            public void actionPerformed(ChangeTextEvent event) {
                try {
                    update();
                } catch (NotepadException e) {
                    log.error("", e);
                }
            }
        });
    }

    public void init(Dimension size, FontMetrics metrics, FontRenderContext frc){
        this.size = size;
        this.metrics = metrics;
        this.frc = frc;
        updateMaxLength();
    }

    private void updateMaxLength() {
        int linesCount = size.height / metrics.getHeight();
        int linesWidth = size.width / metrics.charWidth('a');
        maxLength = 3 * linesWidth * linesCount / 2;
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
        setChanged();
        notifyObservers();
    }

    public void updateScroll(int i) throws NotepadException {
        text = textModel.get(windowPosition, Math.min((int) (textModel.length() - windowPosition), maxLength));
        initLayouts();
        setChanged();
        notifyObservers(i);
    }

    public int getWindowLength(){
        return text.length();
    }

    /**
     *  last line may contains '\n'. In some cases we don't need this character.
     */
    public int getEffectiveWindowLength(){
        if(text.endsWith("\n")){
            return text.length() - 1;
        }
        return text.length();
    }

    /**
     * @return length of last line if we can scroll down, -1 otherwise
     * @throws NotepadException
     */
    public int down() throws NotepadException {
        TextLayoutInfo textLayoutInfo = layouts.get(0);
        SmartTextLayout nextTextLayout = getDownTextLayout();
        if(nextTextLayout == null){
            return -1;
        }
        int count = textLayoutInfo.getLayout().getCharacterCount();
        windowPosition += count;
        updateScroll(count);
        return count;
    }

    /**
     * @return length of first line if we can scroll up, -1 otherwise
     * @throws NotepadException
     */
    public int up() throws NotepadException {
        SmartTextLayout prevTextLayout = getUpTextLayout();
        if(prevTextLayout == null){
            return -1;
        }
        int count = -prevTextLayout.getCharacterCount();
        windowPosition += count;
        updateScroll(count);
        return count;
    }

    public void goTo(long windowPosition) throws NotepadException {
        this.windowPosition = windowPosition;
        update();
    }

    public ArrayList<TextLayoutInfo> getLayouts() {
        return layouts;
    }

    private SmartTextLayout getUpTextLayout() throws NotepadException {
        if(getWindowPosition() == 0){
            return null;
        }
        long startText = Math.max(getWindowPosition() - MAX_LINE_LENGTH, 0);
        String prevText = textModel.get(startText, (int) (getWindowPosition() - startText));
        int from;
        boolean takePrev;
        if (prevText.charAt(prevText.length() - 1) == '\n') {
            from = prevText.substring(0, prevText.length() - 1).lastIndexOf('\n') + 1;
            takePrev = true;
        } else {
            from = prevText.lastIndexOf('\n') + 1;
            takePrev = false;
        }
        prevText = prevText.substring(from);

        SmartTextLayout textLayout = null;
        final MonospacedLineBreakMeasurer lineMeasurer =
                new MonospacedLineBreakMeasurer(prevText, metrics, frc, size.width);
        SmartTextLayout prev = null;
        for (final SmartTextLayout stl : lineMeasurer) {
            prev = textLayout;
            textLayout = stl;
        }
        if(takePrev){
            return prev;
        }
        return textLayout;
    }

    private SmartTextLayout getDownTextLayout() throws NotepadException {
        if(getWindowPosition() + text.length() == textModel.length()){
            return null;
        }
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
