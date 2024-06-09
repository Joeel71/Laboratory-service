package flogo.service.commands.stats;

import flogo.service.api.ResponseBuilder;
import spark.Request;

import java.util.List;

public class BestExperimentStat extends Stat{

    private static final String EXPECTED_MODE = "test";
    @Override
    public String executeStat(Request request, ResponseBuilder builder){
        String[] result = serverFilesManager.readLoggerResult()
                .map(line -> line.split(LOGGER_DELIMITER))
                .filter(lineArray -> lineArray[0].equals(laboratoryName(request)))
                .filter(lineArray -> lineArray[5].contains(EXPECTED_MODE))
                .findFirst().get();
        return builder.successResponse(serialize(castResult(result)));
    }

    private List<String[]> castResult(String[] result) {
        return List.of(new String[]{"laboratory", result[0]},
                new String[]{"experiment", result[1]},
                new String[]{"measurement", result[6]});
    }
}
