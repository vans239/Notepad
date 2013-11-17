package notepad.text.full.model;

import notepad.NotepadException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class Utf8FlushModel extends AbstractTextModel {
    private static final Logger log = Logger.getLogger(Utf8FlushModel.class);
    private final Charset charset = Charset.defaultCharset();
    private RandomAccessFile raf;
    private File file;
    private long length;
    private static final int DEFAULT_SIZE = 1024;

    public Utf8FlushModel(File file) throws NotepadException {
        this.file = file;
        try {
            length = countCharsBuffer(file);
            this.raf = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            throw new NotepadException("", e);
        } catch (IOException e) {
            throw new NotepadException(String.format("Can't work with file [%s]", file.getPath()));
        }
    }

    private long countCharsBuffer(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        int charCount = 0;
        char[] cbuf = new char[1024];
        int read;
        while ((read = reader.read(cbuf)) > -1) {
            charCount += read;
        }
        reader.close();
        return charCount;
    }

    private long charPosToBytePos(long length) throws NotepadException {
        try {
            raf.seek(0);

            long pos = 0;
            for (long i = 0; i < length; ++i) {
                int firstByte = raf.read();
                int symbolLength = getSymbolLength(firstByte);
                if (symbolLength != 0) {
                    pos += symbolLength;
                    raf.skipBytes(symbolLength - 1);
                } else {
                    pos += 1;
                }
            }
            return pos;
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    private final byte buff[] = new byte[8];

    private char readChar() throws IOException {
        int firstByte = raf.read();
        buff[0] = (byte) firstByte;
        int symbolLength = getSymbolLength(firstByte);
        if (symbolLength != 0) {
            raf.read(buff, 1, symbolLength - 1);
            return new String(buff, 0, symbolLength, charset).charAt(0); //todo change to char decode
        } else {
            return new String(buff, 0, 1, charset).charAt(0);
        }
    }

    private int getSymbolLength(int firstByte) {
        int symbolLength = 0;
        int bit = 1 << 7;
        while ((firstByte & bit) != 0) {
            ++symbolLength;
            firstByte = firstByte ^ bit;
            bit = bit >> 1;
        }
        return symbolLength;
    }

    @Override
    public long length() throws NotepadException {
        return length;
    }

    @Override
    public String get(long pos, int length) throws NotepadException {
        try {
            long bytePos = charPosToBytePos(pos);
            raf.seek(bytePos);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; ++i) {
                sb.append(readChar());
            }
            return sb.toString();
        } catch (IOException e) {
            throw new NotepadException("There are no enough data");
        }
    }

    @Override
    public void flush(File file) throws NotepadException {
        if (this.file.equals(file)) {
            return;
        }
        try {
            FileUtils.copyFile(this.file, file);
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    @Override
    public void close() throws NotepadException {
        try {
            raf.close();
        } catch (IOException e) {
            throw new NotepadException("", e);
        }
    }

    @Override
    public void doInsert(long pos, String s) throws NotepadException {
        this.length += s.length();
        long bytePos = charPosToBytePos(pos);
        shiftAndChangeSize(bytePos, s.getBytes(charset).length);
        doReplace(pos, s);
    }


    @Override
    public void doReplace(long pos, String s) throws NotepadException {
        byte[] bytes = s.getBytes(charset);
        byteReplace(charPosToBytePos(pos), bytes, 0, bytes.length);
    }


    @Override
    public void doRemove(long pos, int length) throws NotepadException {
        this.length -= length;
        long bytePos = charPosToBytePos(pos);
        int byteLength = (int) (charPosToBytePos(pos + length) - bytePos);
        shiftAndChangeSize(bytePos + byteLength, -byteLength);
    }


    private void shiftAndChangeSize(long bytePos, int shift) throws NotepadException {
        try {
            long beforeSize = raf.length();
            if (shift > 0) {
                raf.setLength(beforeSize + shift);
            }

            int buffSize = Math.max(DEFAULT_SIZE, shift);
            byte[] buff = new byte[buffSize];
            byte[] buff2 = new byte[buffSize];
            long currBytePos = bytePos;
            int oldLen = Math.max(shift, 0);
            while (currBytePos != beforeSize) {
                int currLen = Math.min((int) (beforeSize - currBytePos), buffSize);
                raf.seek(currBytePos);
                raf.read(buff, 0, currLen);
                byteReplace(currBytePos - oldLen + shift, buff2, 0, oldLen);
                buff2 = Arrays.copyOf(buff, currLen);
                currBytePos += currLen;
                oldLen = currLen;
            }
            byteReplace(currBytePos - oldLen + shift, buff2, 0, oldLen);

            if (shift < 0) {
                raf.setLength(beforeSize + shift);
            }
        } catch (IOException e) {
            throw new NotepadException("Can't shift data", e);
        }
    }

    private void byteReplace(long bytePos, byte[] buff, int offset, int length) throws NotepadException {
        try {
            raf.seek(bytePos);
            raf.write(buff, offset, length);
        } catch (IOException e) {
            throw new NotepadException("can't replace text", e);
        }
    }
}
