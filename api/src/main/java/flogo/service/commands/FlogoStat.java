package flogo.service.commands;

import flogo.service.api.ResponseBuilder;
import flogo.service.concepts.ServerFilesManager;
import flogo.service.commands.stats.Stat;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

public class FlogoStat implements Command {

    private static final String STAT_PARAMETER = "stat";

    private final ServerFilesManager serverFilesManager;
    private final Map<String, Stat> statMap;

    public FlogoStat(ServerFilesManager serverFilesManager) {
        this.serverFilesManager = serverFilesManager;
        this.statMap = new HashMap<>();
    }

    public FlogoStat addStat(String nameStat, Stat stat){
        statMap.put(nameStat, stat);
        return this;
    }

    @Override
    public String execute(Request request, ResponseBuilder responseBuilder) {
        return statMap.get(extractStat(request))
                .serverConceptManager(serverFilesManager)
                .executeStat(request, responseBuilder);
    }

    private String extractStat(Request request) {
        return request.params(STAT_PARAMETER);
    }
}
