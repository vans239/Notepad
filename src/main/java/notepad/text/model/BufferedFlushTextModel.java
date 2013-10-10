package notepad.text.model;

import notepad.NotepadException;
import notepad.utils.SegmentL;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
//todo change encoding. Now this class properly works only on 1-byte coding
public class BufferedFlushTextModel extends AbstractTextModel {
    private static final Logger log = Logger.getLogger(BufferedFlushTextModel.class);
    private static final int BUFFER_SIZE = 30000;
    private static int bufferSize = BUFFER_SIZE;

    private FlushTextModel textModel;

    private long position;
    private int beforeEditionSize;
    private StringBuilder sb;

    public BufferedFlushTextModel(File file) throws NotepadException {
        textModel = new FlushTextModel(file);
        initBuffer(new SegmentL(0, 0));
    }

    @Override
    public long length() throws NotepadException {
        return textModel.length() - beforeEditionSize + sb.length();
    }

    @Override
    public String get(long pos, int length) throws NotepadException {
        updateBuffer(new SegmentL(pos, pos + length));
        return sb.substring((int) (pos - position), (int) (pos - position + length));
    }

    @Override
    public void flush(File file) throws NotepadException {
        flushBuffer();
        textModel.flush(file);
    }

    @Override
    public void close() {
        textModel.close();
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
        if (!getBufferSegment().in(segmentL)) {
            log.info("Buffer flushing");
            flushBuffer();
            initBuffer(segmentL);
        }
    }

    private void initBuffer(SegmentL segmentL) throws NotepadException {
        long segmentLength = segmentL.getEnd() - segmentL.getStart();
        while (segmentLength > bufferSize) {
            bufferSize *= 2;
        }
        long trailingSpace = (bufferSize - segmentLength) / 2;
        SegmentL fileSegment = new SegmentL(0, textModel.length());
        position = fileSegment.nearest(segmentL.getStart() - trailingSpace);
        int length = (int) (fileSegment.nearest(position + bufferSize) - position);
        final String s = textModel.get(position, length);
        sb = new StringBuilder(s);
        beforeEditionSize = s.length();
    }

    private SegmentL getBufferSegment() {
        return new SegmentL(position, position + sb.length());
    }
}
