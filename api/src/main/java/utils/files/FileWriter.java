package utils.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriter {

    public static void write(File file, String content){
        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLine(File file, String line){
        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(file, true))) {
            writer.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(File file, byte[] content){
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
