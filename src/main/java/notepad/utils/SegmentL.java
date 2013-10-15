package notepad.utils;

import org.apache.log4j.Logger;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class SegmentL {
    private static final Logger log = Logger.getLogger(SegmentL.class);
    private final long start;
    private final long end;

    public SegmentL(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getEnd() {
        return end;
    }

    public long getStart() {
        return start;
    }

    public boolean in(final long t) {
        return t >= start && t <= end;
    }

    public long nearest(final long t) {
        if (t < start) {
            return start;
        } else if (t > end) {
            return end;
        }
        return t;
    }

    public long distance(final long t) {
        if (t < start) {
            return start - t;
        } else if (t > end) {
            return end - t;
        }
        return 0;
    }

    public boolean in(SegmentL that) {
        return in(that.start) && in(that.end);
    }

    public SegmentL intersection(SegmentL that){
        long newStart = Math.max(this.start, that.start);
        long newEnd = Math.min(this.end, that.end);
        if(newEnd < newStart){
            return new SegmentL(0,0);
        }
        return new SegmentL(newStart, newEnd);
    }
}
