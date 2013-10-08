package notepad.utils;

import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class Segment {
    private static final Logger log = Logger.getLogger(Segment.class);
    private final int start;
    private final int end;

    public Segment(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public boolean in(final int t) {
        return t >= start && t <= end;
    }

    public int nearest(final int t) {
        if (t < start) {
            return start;
        } else if (t > end) {
            return end;
        }
        return t;
    }

    public int distance(final int t) {
        if (t < start) {
            return start - t;
        } else if (t > end) {
            return end - t;
        }
        return 0;
    }
}
