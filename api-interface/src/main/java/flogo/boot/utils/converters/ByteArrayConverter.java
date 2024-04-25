package flogo.boot.utils.converters;

import java.io.ByteArrayOutputStream;
import java.io.File;

public interface ByteArrayConverter {

    ByteArrayConverter addJson(String json, String jsonName);

    ByteArrayConverter addZipFile(File file, String zipName);

    ByteArrayOutputStream convert();
}
