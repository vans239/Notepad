package notepad.model;

import javafx.util.Pair;
import notepad.text.window.TextWindowModel;
import notepad.view.TextLayoutInfo;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class CaretModel extends Observable {
    private static final Logger log = Logger.getLogger(CaretModel.class);
    private TextWindowModel windowModel;
    private int caretPosition = 0;

    private int wantedX = 0;

    public CaretModel(TextWindowModel windowModel) {
        this.windowModel = windowModel;
        windowModel.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if(arg != null){
                    caretPosition -= (Integer) arg;
                }
            }
        });
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public long getCaretPositionAbs(){
        return caretPosition + windowModel.getWindowPosition();
    }

    public void goTo(final int caretPosition){
        setCaret(caretPosition);
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
        goTo(getHitIndex(point));
    }

    private void updateWantedX() {
        TextLayoutInfo caretLayout = getCaretLayout();
        Point p = caretLayout.getLayout().getPoint(caretPosition - caretLayout.getPosition());
        wantedX = p.x;
//        log.info("WantedX: " + wantedX);
    }

    private void goToWantedX(TextLayoutInfo textLayoutInfo){
        setCaret(textLayoutInfo.getPosition() + textLayoutInfo.getLayout().hitTestChar(wantedX , 0).getCharIndex());
    }

    //only updates
    private void setCaret(final int caretPosition){
        this.caretPosition = caretPosition;
        setChanged();
        notifyObservers();
//        log.info("CaretPos: " + caretPosition);
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

    private int getHitIndex(final Point clicked) {
        ArrayList<TextLayoutInfo> layouts = windowModel.getLayouts();
        TextLayoutInfo nearestLayout = layouts.get(0);
        for (final TextLayoutInfo layoutInfo : layouts) {
            if (distance(layoutInfo, clicked) < distance(nearestLayout, clicked)) {
                nearestLayout = layoutInfo;
            }
        }
        final TextHitInfo hitInfo = nearestLayout.getLayout().hitTestChar(clicked.x, clicked.y);
        return nearestLayout.getPosition() + hitInfo.getInsertionIndex();
    }

    private int distance(final TextLayoutInfo textLayoutInfo, final Point clicked) {
        return Math.abs(textLayoutInfo.getOrigin().y - clicked.y);
    }
}
