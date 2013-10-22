package notepad.manager;

import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class Context {
    private long viewPosition;
    private int caretPosition;

    public Context(long viewPosition, int caretPosition) {
        this.viewPosition = viewPosition;
        this.caretPosition = caretPosition;
    }

    public long getViewPosition() {
        return viewPosition;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    @Override
    public String toString() {
        return "Context{" +
                "viewPosition=" + viewPosition +
                ", caretPosition=" + caretPosition +
                '}';
    }
}
