package commands.stats;

import api.ResponseBuilder;
import spark.Request;

import java.util.List;

public class BestExperimentStat extends Stat{

    private static final String EXPECTED_MODE = "test";
    @Override
    public String executeStat(Request request, ResponseBuilder builder){
        String[] result = serverFilesManager.readLoggerResult()
                .map(line -> line.split(LOGGER_DELIMITER))
                .filter(lineArray -> lineArray[1].equals(laboratoryName(request)))
                .filter(lineArray -> lineArray[6].contains(EXPECTED_MODE))
                .findFirst().get();
        return builder.successResponse(serialize(castResult(result)));
    }

    private List<String[]> castResult(String[] result) {
        return List.of(new String[]{"laboratory", result[1]},
                new String[]{"experiment", result[2]},
                new String[]{"measurement", result[7]});
    }
}
