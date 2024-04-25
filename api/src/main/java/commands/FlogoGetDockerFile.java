package commands;

import api.ResponseBuilder;
import concepts.ArchitectureConcept;
import concepts.Concept;
import concepts.ServerFilesManager;
import spark.Request;
import utils.encoders.HashEncoder;
import utils.files.comprehension.ComprehensionUtils;

public class FlogoGetDockerFile implements Command{

    private static final String LABORATORY_NAME_PARAMETER = "laboratory";
    private static final String EXPECTED_MODE = "test";
    protected static final String LOGGER_DELIMITER = "\t";
    private final ServerFilesManager serverFilesManager;
    private final ComprehensionUtils comprehensionUtils;
    private final HashEncoder encoder;

    public FlogoGetDockerFile(ServerFilesManager serverFilesManager, ComprehensionUtils comprehensionUtils, HashEncoder encoder) {
        this.serverFilesManager = serverFilesManager;
        this.comprehensionUtils = comprehensionUtils;
        this.encoder = encoder;
    }

    @Override
    public String execute(Request request, ResponseBuilder responseBuilder) {
        String[] bestOnLaboratory = getBestOnLaboratory(request);
        Concept architecture = serverFilesManager.loadConcept(new ArchitectureConcept(encoder).name(bestOnLaboratory[0]));
        serverFilesManager.moveToPredict(architecture);
        byte[] modelFile = serverFilesManager.getModelFile(comprehensionUtils);
        return responseBuilder.successResponse(modelFile, "model", "The model has been transferred");
    }

    private String[] getBestOnLaboratory(Request request) {
        return serverFilesManager.readLoggerResult()
                .map(line -> line.split(LOGGER_DELIMITER))
                .filter(lineArray -> lineArray[1].equals(extractLaboratoryName(request)))
                .filter(lineArray -> lineArray[6].equals(EXPECTED_MODE))
                .map(lineArray -> new String[]{lineArray[0], lineArray[2]})
                .findFirst().get();
    }

    private String extractLaboratoryName(Request request) {
        return request.params(LABORATORY_NAME_PARAMETER);
    }
}
