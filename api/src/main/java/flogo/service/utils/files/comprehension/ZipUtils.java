package flogo.service.utils.files.comprehension;

import spark.Request;

import javax.servlet.ServletException;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils implements ComprehensionUtils {

    private static final String FILE_DELIMITER = "/";
    private static final String FILE_EXTENSION = ".zip";
    private static final int BUFFER_SIZE = 4096;
    @Override
    public byte[] download(Request request) {
        try {
            return download(request.raw().getPart("attachment").getInputStream());
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] download(InputStream inputStream) {
        try {
            ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
                fileOutputStream.write(buffer, 0, bytesRead);
            return fileOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] read(File file) {
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, bytesRead);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean decompress(File zipfile) {
        try {
            String destDirectory = destDirectory(zipfile);
            File destDir = new File(destDirectory);
            if (!destDir.exists()) destDir.mkdir();
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
            unzip(zin, destDirectory);
            zin.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void unzip(ZipInputStream zin, String destDirectory) throws IOException {
        ZipEntry entry = zin.getNextEntry();
        while (entry != null) {
            String filePath = destDirectory + FILE_DELIMITER + entry.getName();
            if (!entry.isDirectory()) extractFile(zin, filePath);
            else {
                File dir = new File(filePath);
                dir.mkdir();
            }
            zin.closeEntry();
            entry = zin.getNextEntry();
        }
        zin.close();
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        checkFolder(new File(filePath));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath, true));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1)
            bos.write(bytesIn, 0, read);
        bos.close();
    }

    private void checkFolder(File file) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
    }

    private String destDirectory(File file){
        String parentFile = file.getParent();
        return parentFile + FILE_DELIMITER + file.getName().split("\\.")[0];
    }

    @Override
    public File compress(File file){
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputFileName(file)))) {
            compressFile(file, "", zipOut);
            return new File(outputFileName(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String outputFileName(File file) {
        return file.getParent() + FILE_DELIMITER + file.getName() + FILE_EXTENSION;
    }

    private void compressFile(File file, String fileName, ZipOutputStream zipOut) {
        File[] children = file.listFiles();
        if (children == null)
            writeFile(file, fileName, zipOut);
        else
            for (File childFile : children)
                compressFile(childFile, getFileName(fileName, childFile.getName()), zipOut);
    }

    private String getFileName(String fileName, String childFileName) {
        return (fileName.isEmpty()) ? childFileName : fileName + "/" + childFileName;
    }

    private void writeFile(File file, String fileName, ZipOutputStream zipOut) {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[BUFFER_SIZE];
            int length;
            while ((length = fis.read(bytes)) >= 0)
                zipOut.write(bytes, 0, length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
