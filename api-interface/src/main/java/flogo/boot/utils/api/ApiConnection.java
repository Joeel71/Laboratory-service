package flogo.boot.utils.api;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public interface ApiConnection {

    String postObject(String concept, ByteArrayOutputStream body);
    String deleteObject(String concept, String name);
    String execute(String[] pathParams);
    String listObjects(String concept);
    String getObject(String concept, String name, String path);
    String getModel(String concept, String laboratory, String experiment, String path);
    String getStat(String stat, Map<String, String> queryParams);
}
