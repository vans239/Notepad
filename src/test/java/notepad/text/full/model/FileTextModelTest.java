package notepad.text.full.model;

import notepad.NotepadException;
import notepad.text.full.TextModel;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class FileTextModelTest {
    private static final Logger log = Logger.getLogger(FileTextModelTest.class);

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private TextModel textModel;

    @Before
    public void init() throws NotepadException, IOException {
        File file = testFolder.newFile("coding.txt");

        FileOutputStream output = new FileOutputStream(file);
        FileInputStream input = new FileInputStream("src/test/test-data/FileTextModel.txt");
        IOUtils.copy(input, output);
        output.close();
        input.close();
        textModel = new Utf8FlushModel(file);
        file.deleteOnExit();
    }
    @Test
    public void get() throws NotepadException {
        Assert.assertEquals("In", textModel.get(0, 2));
    }
    @Test
    public void length() throws NotepadException {
        Assert.assertEquals(9, textModel.length());
    }

    @Test
    public void replace() throws NotepadException {
        textModel.replace(3, "abc");
        Assert.assertEquals("Insabcces", textModel.get(0, (int) textModel.length()));
    }

    @Test
    public void insert() throws NotepadException {
        textModel.insert(3, "abc");
        Assert.assertEquals(12, textModel.length());
        Assert.assertEquals("Insabctances", textModel.get(0, (int) textModel.length()));
    }

    @Test
    public void remove() throws NotepadException {
        textModel.remove(3, 2);
        Assert.assertEquals("Insnces", textModel.get(0, (int) textModel.length()));
    }
}
