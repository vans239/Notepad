package notepad.text.full.model;

import notepad.NotepadException;
import notepad.utils.SegmentL;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class BufferedTextModel extends AbstractTextModel {
    private static final Logger log = Logger.getLogger(BufferedTextModel.class);
    private static final int BUFFER_SIZE = 100000;
    private static int bufferSize = BUFFER_SIZE;

    private AbstractTextModel textModel;

    private long position;
    private String beforeBufferText;
    private StringBuilder sb;

    public BufferedTextModel(AbstractTextModel textModel) throws NotepadException {
        this.textModel = textModel;
        initBuffer(new SegmentL(0, 0));
    }

    @Override
    public long length() throws NotepadException {
        return textModel.length() - beforeBufferText.length() + sb.length();
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
    public void close() throws NotepadException {
        textModel.close();
    }

    private void flushBuffer() throws NotepadException {
        String buffer = sb.toString();
        textModel.doRemove(position, beforeBufferText.length());
        textModel.doInsert(position, buffer);
    }

    @Override
    public void doInsert(long pos, String s) throws NotepadException {
        updateBuffer(new SegmentL(pos, pos + s.length()));
        sb.insert((int) (pos - position), s);
    }

    @Override
    public void doReplace(long pos, String s) throws NotepadException {
        updateBuffer(new SegmentL(pos, pos + s.length()));
        sb.replace((int) (pos - position), (int) (pos - position + s.length()), s);
    }

    @Override
    public void doRemove(long pos, int length) throws NotepadException {
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
        beforeBufferText = s;
    }

    private SegmentL getBufferSegment() {
        return new SegmentL(position, position + sb.length());
    }
}
