package notepad.model;

import notepad.utils.SegmentL;
import org.apache.log4j.Logger;

import java.util.Observable;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class SelectionModel{
    private static final Logger log = Logger.getLogger(SelectionModel.class);
    private long start = 0;
    private long end = 0;

    public SegmentL getSelection(){
        return new SegmentL(Math.min(start, end), Math.max(start, end));
    }

    public void setStart(final long start){
        this.start = start;
        end = start;
    }

    public void setEnd(final long end){
        this.end = end;
    }

    public void unshowSelection() {
        end = start;
    }

    public boolean isShowSelection() {
        return start != end;
    }
}
