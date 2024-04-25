package utils.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReader {

    private static final String TSV_DELIMITER = "\t";
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

    public static Map<String, String> readTsv(File file){
        Map<String, String> map = new HashMap<>();
        if (!file.exists()) return map;
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(TSV_DELIMITER);
                map.put(split[0], split[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
