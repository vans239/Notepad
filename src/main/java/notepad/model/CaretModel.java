package notepad.model;

import notepad.text.window.TextWindowModel;
import notepad.utils.observe.Observable;
import notepad.utils.observe.ObservableImpl;
import notepad.utils.observe.Observer;
import notepad.view.TextLayoutInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.util.ArrayList;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class CaretModel extends ObservableImpl<Void> {
    private static final Logger log = Logger.getLogger(CaretModel.class);
    private TextWindowModel windowModel;
    private int caretPosition = 0;
    private boolean isLeading = false;

    private int wantedX = 0;

    public CaretModel(TextWindowModel windowModel) {
        this.windowModel = windowModel;
        windowModel.addObserver(new Observer<Integer>() {
            @Override
            public void update(Observable<Integer> o, Integer arg) {
                if(arg != null){
                    caretPosition -= arg;
                }
            }
        });
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public boolean isLeading(){
        return isLeading;
    }

    public long getCaretPositionAbs(){
        return caretPosition + windowModel.getWindowPosition();
    }

    public void goTo(final int caretPosition){
        setCaret(caretPosition, true);
        updateWantedX();
    }

    public boolean goUp(){
        Pair<TextLayoutInfo, Integer> caretPair = getCaretLayoutPair();
        if(caretPair.getValue() > 0){
            TextLayoutInfo textLayoutInfo = windowModel.getLayouts().get(caretPair.getValue() - 1);
            goToWantedX(textLayoutInfo);
            return true;
        }
        return false;
    }

    public boolean goDown(){
        Pair<TextLayoutInfo, Integer> caretPair = getCaretLayoutPair();
        if(caretPair.getValue() + 1 < windowModel.getLayouts().size()){
            TextLayoutInfo textLayoutInfo = windowModel.getLayouts().get(caretPair.getValue() + 1);
            goToWantedX(textLayoutInfo);
            return true;
        }
        return false;
    }

    public boolean goLeft(){
        if (caretPosition > 0) {
            goTo(caretPosition - 1);
            return true;
        }
        return false;
    }

    public boolean goRight(){
        if(caretPosition < windowModel.getEffectiveWindowLength()){
            goTo(caretPosition + 1);
            return true;
        }
        return false;
    }

    public void goToPoint(final Point point){
        Pair<TextLayoutInfo, TextHitInfo> hitInfoPair = getHitInfoPair(point);
        setCaret(hitInfoPair.getLeft().getPosition() + hitInfoPair.getRight().getCharIndex(), hitInfoPair.getRight().isLeadingEdge());
        updateWantedX();
    }

    private void updateWantedX() {
        TextLayoutInfo caretLayout = getCaretLayout();
        Point p = caretLayout.getLayout().getPoint(caretPosition - caretLayout.getPosition());
        wantedX = p.x;
    }

    private void goToWantedX(TextLayoutInfo textLayoutInfo){
        TextHitInfo textHitInfo = textLayoutInfo.getLayout().hitTestChar(wantedX, 0);
        int charIndex = textHitInfo.getCharIndex();
        int newCaretPosition = textLayoutInfo.getPosition() + charIndex;
        setCaret(newCaretPosition, textHitInfo.isLeadingEdge());
    }

    //only updates
    private void setCaret(final int caretPosition, boolean isLeading){
        this.isLeading = isLeading;
        this.caretPosition = caretPosition + (isLeading ? 0 : 1);
        notifyObservers();
    }

    public TextLayoutInfo getCaretLayout(){
        return getCaretLayoutPair().getKey();
    }

    private Pair<TextLayoutInfo, Integer> getCaretLayoutPair(){
        ArrayList<TextLayoutInfo> layouts = windowModel.getLayouts();
        for (int i = 0; i < layouts.size(); ++i) {
            final TextLayoutInfo layoutInfo = layouts.get(i);
            if (caretInThisTextLayout(layoutInfo, i == layouts.size() - 1)) {
                return Pair.of(layoutInfo, i);
            }
        }
        throw new RuntimeException("Unreachable place");
    }

    private boolean caretInThisTextLayout(TextLayoutInfo layoutInfo, boolean isLastLayout) {
        int from = layoutInfo.getPosition();
        int to = from + layoutInfo.getLayout().getFullCharacterCount() ;
        return caretPosition >= from && (caretPosition < to || (caretPosition == to && (!isLeading || isLastLayout)));
    }

    private Pair<TextLayoutInfo, TextHitInfo> getHitInfoPair(final Point clicked) {
        ArrayList<TextLayoutInfo> layouts = windowModel.getLayouts();
        TextLayoutInfo nearestLayout = layouts.get(0);
        for (final TextLayoutInfo layoutInfo : layouts) {
            if (distance(layoutInfo, clicked) < distance(nearestLayout, clicked)) {
                nearestLayout = layoutInfo;
            }
        }
        final TextHitInfo hitInfo = nearestLayout.getLayout().hitTestChar(clicked.x, clicked.y);
        return Pair.of(nearestLayout, hitInfo);
    }

    private int distance(final TextLayoutInfo textLayoutInfo, final Point clicked) {
        return Math.abs(textLayoutInfo.getOrigin().y - clicked.y);
    }
}
