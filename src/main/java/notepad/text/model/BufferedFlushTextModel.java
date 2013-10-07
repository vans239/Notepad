package notepad.text.model;

import notepad.NotepadException;
import notepad.utils.SegmentL;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class BufferedFlushTextModel extends AbstractTextModel {
    private static final Logger log = Logger.getLogger(BufferedFlushTextModel.class);
    private static final int BUFFER_SIZE = 20480;

    private FlushTextModel textModel;

    private long position;
    private int beforeEditionSize;
    private StringBuilder sb;

    public BufferedFlushTextModel(File file) throws NotepadException {
        textModel = new FlushTextModel(file);
        initBuffer(new SegmentL(0,0));
    }

    @Override
    public long length() throws NotepadException {
        return textModel.length() - beforeEditionSize + sb.length();
    }

    @Override
    public String get(long pos, int length) throws NotepadException {
        updateBuffer(new SegmentL(pos, pos + length));
        return sb.substring((int) (pos - position), (int) (pos - position + length));
        //todo check length > BUFFERSIZE
        //todo check sb.length isn't too large
    }

    @Override
    public void flush(File file) throws NotepadException {
        flushBuffer();
        textModel.flush(file);
    }

    private void flushBuffer() throws NotepadException {
        int shift = sb.length() - beforeEditionSize;
        textModel.shiftAndChangeSize(position + beforeEditionSize, shift);
        textModel.replace(position, sb.toString());
    }

    @Override
    protected void _insert(long pos, String s) throws NotepadException {
        updateBuffer(new SegmentL(pos, pos + s.length()));
        sb.insert((int) (pos - position), s);
    }

    @Override
    protected void _replace(long pos, String s) throws NotepadException {
        updateBuffer(new SegmentL(pos, pos + s.length()));
        sb.replace((int) (pos - position), (int) (pos - position + s.length()), s);
    }

    @Override
    protected void _remove(long pos, int length) throws NotepadException {
        updateBuffer(new SegmentL(pos, pos + length));
        sb.delete((int) (pos - position), (int) (pos - position + length));
    }

    private void updateBuffer(SegmentL segmentL) throws NotepadException {
        if(!getBufferSegment().in(segmentL)){
            log.info("Buffer flushing");
            flushBuffer();
            initBuffer(segmentL);
        }
    }

    private void initBuffer(SegmentL segmentL) throws NotepadException {
        SegmentL fileSegment = new SegmentL(0, textModel.length());
        position = fileSegment.nearest(segmentL.getStart() - BUFFER_SIZE / 2);
        int length = (int) (fileSegment.nearest(position + BUFFER_SIZE) - position);
        final String s = textModel.get(position, length);
        sb = new StringBuilder(s);
        beforeEditionSize = s.length();
    }

    private SegmentL getBufferSegment(){
        return new SegmentL(position, position + sb.length());
    }
}
