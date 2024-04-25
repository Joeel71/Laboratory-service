package flogo.boot.utils.converters;

import java.io.*;

public class ApiByteArrayConverter implements ByteArrayConverter{

    private static final String BOUNDARY = "boundary";
    private static final  String CARRIAGE_RETURN = "\r\n";
    private final ByteArrayOutputStream byteArrayOutputStream;
    private final PrintWriter writer;

    public ApiByteArrayConverter() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.writer = new PrintWriter(byteArrayOutputStream);
    }

    @Override
    public ByteArrayConverter addJson(String json, String jsonName) {
        newObject();
        writer.append("Content-Disposition: form-data; name=\"").append(jsonName).append("\"").append(CARRIAGE_RETURN)
                .append("Content-Type: application/json").append(CARRIAGE_RETURN)
                .append(CARRIAGE_RETURN)
                .append(json).append(CARRIAGE_RETURN)
                .flush();
        return this;
    }

    @Override
    public ByteArrayConverter addZipFile(File file, String zipName) {
        newObject();
        writer.append("Content-Disposition: form-data; name=\"").append(zipName).append("\"").append(CARRIAGE_RETURN)
                .append(CARRIAGE_RETURN)
                .flush();
        addFile(byteArrayOutputStream, file);
        return this;
    }

    @Override
    public ByteArrayOutputStream convert() {
        writer.append(CARRIAGE_RETURN).flush();
        writer.append("--").append(BOUNDARY).append("--").append(CARRIAGE_RETURN);
        writer.close();
        return byteArrayOutputStream;
    }

    private void newObject() {
        writer.append("--").append(BOUNDARY).append(CARRIAGE_RETURN);
    }

    private void addFile(OutputStream outputStream, File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, bytesRead);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
