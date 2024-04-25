package flogo.boot.utils.file;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zipper {

    private static final String FILE_DELIMITER = "/";
    private static final String FILE_EXTENSION = ".zip";
    private static final int BUFFER_SIZE = 4096;

    public static File zip(File file){
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputFileName(file)))) {
            zipFile(file, "", zipOut);
            return new File(outputFileName(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String outputFileName(File file) {
        return file.getParent() + FILE_DELIMITER + file.getName() + FILE_EXTENSION;
    }

    private static void zipFile(File file, String fileName, ZipOutputStream zipOut) {
        File[] children = file.listFiles();
        if (children == null)
            writeFile(file, fileName, zipOut);
        else
            for (File childFile : children)
                zipFile(childFile, getFileName(fileName, childFile.getName()), zipOut);
    }

    private static String getFileName(String fileName, String childFileName) {
        return (fileName.isEmpty()) ? childFileName : fileName + "/" + childFileName;
    }

    private static void writeFile(File file, String fileName, ZipOutputStream zipOut) {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0)
                zipOut.write(bytes, 0, length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean unzip(File zipfile) {
        try {
            String destDirectory = destDirectory(zipfile);
            File destDir = new File(destDirectory);
            if (!destDir.exists()) destDir.mkdir();
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
            unzip(zin, destDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void unzip(ZipInputStream zin, String destDirectory) throws IOException {
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

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    private static String destDirectory(File file){
        String parentFile = file.getParent();
        return parentFile + FILE_DELIMITER + file.getName().split("\\.")[0];
    }
}
