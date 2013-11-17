package notepad.text.full.model;

import notepad.NotepadException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class BufferedTextModelTest {
    private static final Logger log = Logger.getLogger(FileTextModelTest.class);

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private BufferedTextModel textModel;
    private File file;

    @Before
    public void init() throws NotepadException, IOException {
        file = testFolder.newFile("coding.txt");
        textModel = new BufferedTextModel(new InMemoryTextModel(file));
        file.deleteOnExit();
    }

    @Test
    public void simpleTest() throws NotepadException, IOException {
        textModel.insert(0, "abc");
        textModel.flush(file);
        textModel.insert(3, "d");
        show();
    }

    public void show() throws NotepadException {
        System.out.println(textModel.get(0, (int) textModel.length()));
    }
}
