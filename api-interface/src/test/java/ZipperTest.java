import flogo.boot.utils.file.Zipper;
import org.junit.Test;

import java.io.File;

public class ZipperTest {

    private static final File UNZIP_FILE = new File("C:/Users/Joel/Desktop/numeric_dataset");
    private static final File ZIP_FILE = new File("C:/Users/Joel/Desktop/numeric_dataset.zip");

    @Test
    public void should_test_the_zip_method() {
        System.out.println(Zipper.zip(UNZIP_FILE));
    }

    @Test
    public void should_test_the_unzip_method() {
        System.out.println(Zipper.unzip(ZIP_FILE));
    }
}
