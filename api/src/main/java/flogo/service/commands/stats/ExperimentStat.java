package flogo.service.commands.stats;

import flogo.service.api.ResponseBuilder;
import spark.Request;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ExperimentStat extends Stat{

    private static final String EXPECTED_MODE = "validation";

    @Override
    public String executeStat(Request request, ResponseBuilder builder) {
        List<String> measurements = serverFilesManager.readLoggerResult()
                .map(line -> line.split(LOGGER_DELIMITER))
                .filter(lineArray -> lineArray[0].equals(laboratoryName(request)))
                .filter(lineArray -> lineArray[1].equals(experimentName(request)))
                .filter(lineArray -> lineArray[5].equals(EXPECTED_MODE))
                .sorted(Comparator.comparing(lineArray -> lineArray[4]))
                .map(lineArray -> lineArray[6])
                .toList();
        return builder.successResponse(serialize(castNames(request), castMeasurements(measurements)));
    }

    private Map<String, List<String>> castMeasurements(List<String> measurements) {
        return Map.of("loss", measurements);
    }

    private List<String[]> castNames(Request request) {
        return List.of(new String[]{"laboratory", laboratoryName(request)},
                new String[]{"experiment", experimentName(request)});
    }
}
