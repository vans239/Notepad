package notepad.manager;

import notepad.text.full.ChangeTextEvent;
import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class Patch {
    private static final Logger log = Logger.getLogger(Patch.class);
    private final Context context;
    private final ChangeTextEvent cte;

    public Patch(Context context, ChangeTextEvent cte) {
        this.context = context;
        this.cte = cte;
    }

    public Context getContext() {
        return context;
    }

    public ChangeTextEvent getCte() {
        return cte;
    }

    @Override
    public String toString() {
        return "Patch{" +
                "context=" + context +
                ", cte=" + cte +
                '}';
    }
}
