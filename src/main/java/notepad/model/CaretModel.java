package notepad.model;

import javafx.util.Pair;
import notepad.text.window.TextWindowModel;
import notepad.view.TextLayoutInfo;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class CaretModel extends Observable {
    private static final Logger log = Logger.getLogger(CaretModel.class);
    private TextWindowModel windowModel;
    private int caretPosition = 0;

    public CaretModel(TextWindowModel windowModel) {
        this.windowModel = windowModel;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public long getCaretPositionAbs(){
        return caretPosition + windowModel.getWindowPosition();
    }

    public void goTo(final int caretPosition){
        this.caretPosition = caretPosition;
    }

    public boolean goUp(){
        Pair<TextLayoutInfo, Integer> caretPair = getCaretLayoutPair();
        if(caretPair.getValue() > 0){
            caretPosition -= windowModel.getLayouts().get(caretPair.getValue() - 1).getLayout().getCharacterCount();
            return true;
        }
        return false;
    }

    public boolean goDown(){
        Pair<TextLayoutInfo, Integer> caretPair = getCaretLayoutPair();
        if(caretPair.getValue() + 1 < windowModel.getLayouts().size()){
            caretPosition -= caretPair.getKey().getLayout().getCharacterCount();
            return true;
        }
        return false;
    }

    public boolean goLeft(){
        if (caretPosition > 0) {
            --caretPosition;
            return true;
        }
        return false;
    }

    public boolean goRight(){
        if(caretPosition < windowModel.getWindowLength()){
            caretPosition++;
            return true;
        }
        return false;
    }


    public TextLayoutInfo getCaretLayout(){
        return getCaretLayoutPair().getKey();
    }

    private Pair<TextLayoutInfo, Integer> getCaretLayoutPair(){
        ArrayList<TextLayoutInfo> layouts = windowModel.getLayouts();
        for (int i = 0; i < layouts.size(); ++i) {
            final TextLayoutInfo layoutInfo = layouts.get(i);
            if (caretInThisTextLayout(layoutInfo, i == layouts.size() - 1)) {
                return new Pair<TextLayoutInfo, Integer>(layoutInfo, i);
            }
        }
        throw new RuntimeException("Unreachable place");
    }

    private boolean caretInThisTextLayout(TextLayoutInfo layoutInfo, boolean isLastLayout) {
        int from = layoutInfo.getPosition();
        int to = from + layoutInfo.getLayout().getCharacterCount();
        return (caretPosition >= from && caretPosition < to)
                || (caretPosition >= from && isLastLayout);
    }
}
