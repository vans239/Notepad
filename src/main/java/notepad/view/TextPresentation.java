package notepad.view;

import notepad.view.textlayout.SmartTextLayout;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
//todo delete class
public class TextPresentation {
    private static final Logger log = Logger.getLogger(TextPresentation.class);
    private Deque<SmartTextLayout> deque;
    private ArrayList<SmartTextLayout> cached;

    public void init(){

    }

    public ArrayList<SmartTextLayout> getLayouts(){
        return cached;
    }

    public void scrollUp(){
        deque.pollLast();
        deque.addFirst(findBefore());
        cache();
    }

    public void scrollDown(){
        deque.pollFirst();
        deque.addLast(findLast());
        cache();
    }

    private void cache() {
        //todo unmodifiable list!!!!
        cached = new ArrayList<SmartTextLayout>(deque);
    }

    private SmartTextLayout findBefore() {
        throw new sun.reflect.generics.reflectiveObjects.NotImplementedException();
    }

    private SmartTextLayout findLast() {
        throw new sun.reflect.generics.reflectiveObjects.NotImplementedException();
    }


}
