package flogo.service.utils.files.comprehension;

import spark.Request;

import java.io.File;
import java.io.InputStream;

public interface ComprehensionUtils {

    byte[] download(Request request);
    byte[] download(InputStream inputStream);
    byte[] read(File file);
    boolean decompress(File file);
    File compress(File file);
}
