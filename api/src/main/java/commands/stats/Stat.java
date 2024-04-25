package commands.stats;

import api.ResponseBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import concepts.ServerFilesManager;
import spark.Request;

import java.util.List;
import java.util.Map;

public abstract class Stat {

    private static final String LABORATORY_NAME_PARAMETER = "laboratory";
    private static final String EXPERIMENT_NAME_PARAMETER = "experiment";
    protected static final String LOGGER_DELIMITER = "\t";

    protected ServerFilesManager serverFilesManager;

    public Stat serverConceptManager(ServerFilesManager serverFilesManager){
        this.serverFilesManager = serverFilesManager;
        return this;
    }

    protected String laboratoryName(Request request) {
        return request.queryParams(LABORATORY_NAME_PARAMETER);
    }

    protected String experimentName(Request request) {
        return request.queryParams(EXPERIMENT_NAME_PARAMETER);
    }

    protected String serialize(List<String[]> stat){
        return fillValuesOnJson(new JsonObject(), stat).toString();
    }

    protected String serialize(List<String[]> stat, Map<String, List<String>> mapStat){
        return fillListOnJson(fillValuesOnJson(new JsonObject(), stat), mapStat).toString();
    }

    private JsonObject fillListOnJson(JsonObject jsonObject, Map<String, List<String>> mapStat) {
        for (String key: mapStat.keySet())
            addArrayToJson(jsonObject, mapStat, key);
        return jsonObject;
    }

    private static void addArrayToJson(JsonObject jsonObject, Map<String, List<String>> mapStat, String key) {
        JsonArray jsonArray = new JsonArray();
        for (String value: mapStat.get(key))
            jsonArray.add(value);
        jsonObject.add(key, jsonArray);
    }

    private static JsonObject fillValuesOnJson(JsonObject jsonObject, List<String[]> stat) {
        stat.forEach(jsonParameters -> jsonObject.addProperty(jsonParameters[0], jsonParameters[1]));
        return jsonObject;
    }

    public abstract String executeStat(Request request, ResponseBuilder builder);
}
