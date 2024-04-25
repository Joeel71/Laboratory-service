package flogo.boot.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileReader {
    public static String read(File file){
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) content.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
