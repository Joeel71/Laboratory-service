package flogo.boot.concept;

import com.google.gson.JsonObject;
import flogo.boot.utils.converters.ByteArrayConverter;
import flogo.boot.utils.file.FileReader;

import java.io.ByteArrayOutputStream;
import java.io.File;

public abstract class Concept {

    protected static final String JSON_NAME = "definition";

    private String content;
    protected ByteArrayConverter byteArrayConverter;

    public Concept byteArrayConverter(ByteArrayConverter byteArrayConverter){
        this.byteArrayConverter = byteArrayConverter;
        return this;
    }

    public Concept readFrom(String path){
        this.content = FileReader.read(new File(path));
        return this;
    }

    protected String jsonSerialization(NameExtractor nameExtractor) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", nameExtractor.extract(content));
        jsonObject.addProperty("content", content);
        return jsonObject.toString();
    }

    public abstract String conceptName();

    public abstract ByteArrayOutputStream toOutputStream();

    interface NameExtractor{
        String extract(String content);
    }
}
