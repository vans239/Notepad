package notepad.text.model;

import notepad.NotepadException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
//ReadTextModel
//WriteTextModel
public class FlushTextModel extends AbstractTextModel{
    private static final Logger log = Logger.getLogger(FlushTextModel.class);
    private RandomAccessFile raf;
    private File file;

    private static final int DEFAULT_SIZE = 1024;

    public FlushTextModel(File file) throws NotepadException {
        this.file = file;
        try {
            this.raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            throw new NotepadException("", e);
        }
    }

    @Override
    public long length() throws NotepadException {
        try {
            return raf.length();
        } catch (IOException e) {
            throw new NotepadException("can't read length", e);
        }
    }

    @Override
    public String get(long pos, int length) throws NotepadException {
        int err;
        byte[] bytes = new byte[length];
        try {
            raf.seek(pos);
            err = raf.read(bytes);
        } catch (IOException e) {
            throw new NotepadException("There are no enough data");
        }
        if (err == -1) {
            throw new NotepadException("There are no enough data");
        }
        return new String(bytes);
    }

    @Override
    public void flush(File file) throws NotepadException {
        if(this.file.equals(file)){
            return;
        }
        try {
            FileUtils.copyFile(this.file, file);
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    @Override
    public void _insert(long pos, String s) throws NotepadException {
        shiftAndChangeSize(pos, s.length());
        _replace(pos, s);
    }


    @Override
    public void _replace(long pos, String s) throws NotepadException {
        byte[] bytes = s.getBytes();
        replace(pos, bytes, 0, bytes.length);
    }


    @Override
    public void _remove(long pos, int length) throws NotepadException {
        shiftAndChangeSize(pos + length, -length);
    }


    protected void shiftAndChangeSize(long pos, int shift) throws NotepadException {
        try {
            long beforeSize = raf.length();
            if (shift > 0) {
                raf.setLength(beforeSize + shift);
            }

            int buffSize = Math.max(DEFAULT_SIZE, shift);
            byte[] buff = new byte[buffSize];
            byte[] buff2 = new byte[buffSize];
            long currPos = pos;
            int oldLen = Math.max(shift, 0);
            while (currPos != beforeSize) {
                //todo long -> int bad
                int currLen = Math.min((int) (beforeSize - currPos), buffSize);
                raf.seek(currPos);
                raf.read(buff, 0, currLen);
                replace(currPos - oldLen + shift, buff2, 0, oldLen);
                buff2 = Arrays.copyOf(buff, currLen);
                currPos += currLen;
                oldLen = currLen;
            }
            replace(currPos - oldLen + shift, buff2, 0, oldLen);

            if (shift < 0) {
                raf.setLength(beforeSize + shift);
            }
        } catch (IOException e) {
            throw new NotepadException("Can't shift data", e);
        }
    }

    private void replace(long pos, byte[] buff, int offset, int length) throws NotepadException {
        try {
            raf.seek(pos);
            raf.write(buff, offset, length);
        } catch (IOException e) {
            throw new NotepadException("can't replace text", e);
        }
    }
}
