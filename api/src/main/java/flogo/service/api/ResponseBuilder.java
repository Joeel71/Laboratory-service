package flogo.service.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    private static final int ERROR_STATUS = 500;
    private static final int SUCCESS_STATUS = 200;
    private static final String RESPONSE_JSON_FIELD = "response";
    private Response response;

    public ResponseBuilder response(Response response){
        this.response = response;
        return this;
    }

    public String successResponse(String message) {
        response.status(SUCCESS_STATUS);
        return serializeResponse(message);
    }

    public String successResponse(String[] message) {
        response.status(SUCCESS_STATUS);
        return serializeResponse(message);
    }

    public String errorResponse(String errorMessage) {
        response.status(ERROR_STATUS);
        return serializeResponse(errorMessage);
    }

    public String successResponse(byte[] bytes, String filename, String message) {
        try {
            response.header("Content-Disposition", "attachment; filename=" + filename);
            response.type("application/file");
            response.raw().getOutputStream().write(bytes);
            response.raw().getOutputStream().flush();
            response.raw().getOutputStream().close();
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String serializeResponse(String response){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(RESPONSE_JSON_FIELD, response);
        return new Gson().toJson(jsonObject);
    }

    private String serializeResponse(String[] response){
        Map<String, String[]> jsonMap = new HashMap<>();
        jsonMap.put("response", response);
        return new Gson().toJson(jsonMap);
    }
}
