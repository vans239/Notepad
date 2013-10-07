package notepad.text.model;

import notepad.NotepadException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class FileTextModelTest {
    private static final Logger log = Logger.getLogger(FileTextModelTest.class);

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private FlushTextModel fileTextModel;

    @Before
    public void init() throws NotepadException, IOException {
        File file = testFolder.newFile("temp.txt");

        FileOutputStream output = new FileOutputStream(file);
        FileInputStream input = new FileInputStream("src/test/test-data/FlushTextModel.txt");
        IOUtils.copy(input, output);
        output.close();
        input.close();
        fileTextModel = new FlushTextModel(file);
    }

    @Test
    public void length() throws NotepadException {
        Assert.assertEquals(9, fileTextModel.length());
    }

    @Test
    public void replace() throws NotepadException {
        fileTextModel.replace(3, "abc");
        Assert.assertEquals("Insabcces", fileTextModel.get(0, (int) fileTextModel.length()));
    }

    @Test
    public void shiftRight() throws NotepadException {
        fileTextModel.shiftAndChangeSize(3, 3);
        Assert.assertEquals(12, fileTextModel.length());
        Assert.assertEquals("Ins\u0000\u0000\u0000tances", fileTextModel.get(0, (int) fileTextModel.length()));
    }

    @Test
    public void shiftLeft() throws NotepadException {
        fileTextModel.shiftAndChangeSize(3, -2);
        Assert.assertEquals("Itances", fileTextModel.get(0, (int) fileTextModel.length()));
    }

    @Test
    public void insert() throws NotepadException {
        fileTextModel.insert(3, "abc");
        Assert.assertEquals(12, fileTextModel.length());
        Assert.assertEquals("Insabctances", fileTextModel.get(0, (int) fileTextModel.length()));
    }

    @Test
    public void remove() throws NotepadException {
        System.out.println("The most direct computation would be for the enemy to try all 2^r possible keys, one by one.".length());
        System.out.println("In this letter I make some remarks on a general principle relevant to enciphering in general and my machine.".length());
        System.out.println("We see immediately that one needs little information to begin to break down the process.".length());

        fileTextModel.remove(3, 2);
        Assert.assertEquals("Insnces", fileTextModel.get(0, (int) fileTextModel.length()));
    }
}
