package flogo.service.commands.stats;

import flogo.service.api.ResponseBuilder;
import spark.Request;

import java.util.*;
import java.util.stream.Collectors;

public class ExperimentsOnLaboratoryStat extends Stat{

    private static final String EXPECTED_MODE = "validation";

    @Override
    public String executeStat(Request request, ResponseBuilder builder) {
        Map<String, String> result = serverFilesManager.readLoggerResult()
                .map(line -> line.split(LOGGER_DELIMITER))
                .filter(lineArray -> lineArray[0].equals(laboratoryName(request)))
                .filter(lineArray -> lineArray[5].equals(EXPECTED_MODE))
                .collect(Collectors.groupingBy(lineArray -> lineArray[1],
                        Collectors.collectingAndThen(
                                Collectors.minBy(Comparator.comparingDouble(lineArray -> Double.parseDouble(lineArray[6]))),
                                min -> min.get()[6]))
                );
        return builder.successResponse(serialize(castLaboratoryResult(laboratoryName(request)), castMapResult(result)));
    }

    private Map<String, List<String>> castMapResult(Map<String, String> result) {
        HashMap<String, List<String>> castResult = new HashMap<>();
        castResult.put("experiments", new ArrayList<>(result.keySet()));
        List<String> measurements = new ArrayList<>();
        castResult.get("experiments").forEach(key -> measurements.add(result.get(key)));
        castResult.put("measurements", measurements);
        return castResult;
    }

    private List<String[]> castLaboratoryResult(String laboratory) {
        List<String[]> arrayList = new ArrayList<>();
        String[] laboratoryArray = {"laboratory", laboratory};
        arrayList.add(laboratoryArray);
        return arrayList;
    }
}
