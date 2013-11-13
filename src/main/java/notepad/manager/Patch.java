package notepad.manager;

import notepad.text.full.ChangeTextEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class Patch {
    private static final Logger log = Logger.getLogger(Patch.class);
    private final Context contextBefore;
    private final Context contextAfter;
    private final ChangeTextEvent cte;

    public Patch(Context contextBefore, Context contextAfter, ChangeTextEvent cte) {
        this.contextBefore = contextBefore;
        this.contextAfter = contextAfter;
        this.cte = cte;
    }

    public Context getContextBefore() {
        return contextBefore;
    }

    public ChangeTextEvent getCte() {
        return cte;
    }

    public Context getContextAfter() {
        return contextAfter;
    }

    @Override
    public String toString() {
        return "Patch {" +
                "contextBefore=" + contextBefore +
                "contextAfter=" + contextAfter +
                ", cte=" + cte +
                '}';
    }
}
