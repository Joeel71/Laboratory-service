package flogo.boot.utils.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlogoApiConnection implements ApiConnection {

    private static final String INITIAL_URL = "http://localhost:8080/flogo/";
    private static final String API_DELIMITER = "/";
    private static final String BOUNDARY = "boundary";
    private static final String EXECUTE_PATH = "execute";
    private static final String STATS_PATH = "stats";
    private static final String QUERY_PARAMS_DELIMITER = "&";
    private static final String INIT_QUERY_PARAMS = "?";
    private static final String RESPONSE_NAME_FIELD = "response";


    @Override
    public String postObject(String concept, ByteArrayOutputStream body) {
        try {
            HttpURLConnection connection = createPostConnection(createURL(concept));
            OutputStream outputStream = connection.getOutputStream();
            body.writeTo(outputStream);
            return readResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteObject(String concept, String name) {
        try {
            HttpURLConnection connection = createDeleteConnection(createURL(addPathParams(concept, new String[]{name})));
            return readResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String execute(String[] pathParams){
        try {
            HttpURLConnection connection = createPostConnection(createURL(addPathParams(EXECUTE_PATH, pathParams)));
            return readResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String addPathParams(String initialPath, String[] pathParams) {
        return initialPath + API_DELIMITER + String.join(API_DELIMITER, pathParams);
    }

    @Override
    public String listObjects(String concept) {
        try {
            HttpURLConnection connection = createGetConnection(createURL(concept));
            return readResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getObject(String url, String name, String path) {
        try {
            HttpURLConnection connection = createGetConnection(createURL(addPathParams(url, new String[]{name})));
            return readResponse(connection, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getStat(String stat, Map<String, String> queryParams) {
        try {
            HttpURLConnection connection = createGetConnection(createURL(addPathParams(STATS_PATH, new String[]{stat}), queryParams));
            return readResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL createURL(String finalPath) throws MalformedURLException {
        return new URL(INITIAL_URL + finalPath);
    }

    private URL createURL(String initialPath, Map<String, String> queryParameters) throws MalformedURLException {
        StringBuilder pathBuilder = new StringBuilder().append(initialPath).append(INIT_QUERY_PARAMS);
        for(String key: queryParameters.keySet())
            pathBuilder.append(key).append("=").append(queryParameters.get(key)).append(QUERY_PARAMS_DELIMITER);
        return createURL(pathBuilder.deleteCharAt(pathBuilder.length() - 1).toString());
    }

    private String readResponse(HttpURLConnection connection, String path) throws IOException {
        if (path.isEmpty()) return readResponse(connection);
        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) return readResponse(connection);
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(path)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, bytesRead);
        }
        return "File downloaded";
    }

    private String deserializeAnswer(String json){
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        if (jsonObject.get(RESPONSE_NAME_FIELD).isJsonArray())
            return String.join("\n", deserializerArray(jsonObject));
        return jsonObject.get(RESPONSE_NAME_FIELD).getAsString();
    }

    private List<String> deserializerArray(JsonObject jsonObject) {
        List<String> responseList = new ArrayList<>();
        for (JsonElement jsonElement: jsonObject.getAsJsonArray(RESPONSE_NAME_FIELD))
            responseList.add(jsonElement.getAsString());
        return responseList;
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            return deserializeAnswer(readBuffer(new BufferedReader(new InputStreamReader(connection.getInputStream()))));
        return deserializeAnswer(readBuffer(new BufferedReader(new InputStreamReader(connection.getErrorStream()))));
    }

    private String readBuffer(BufferedReader reader) throws IOException {
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = reader.readLine()) != null) response.append(inputLine).append("\n");
        reader.close();
        return response.toString();
    }

    private HttpURLConnection createGetConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }

    private HttpURLConnection createDeleteConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");
        return connection;
    }

    private HttpURLConnection createPostConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        connection.setDoOutput(true);
        return connection;
    }
}
