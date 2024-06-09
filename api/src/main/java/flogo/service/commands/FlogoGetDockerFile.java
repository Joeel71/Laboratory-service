package flogo.service.commands;

import flogo.service.api.ResponseBuilder;
import flogo.service.concepts.ArchitectureConcept;
import flogo.service.concepts.Concept;
import flogo.service.concepts.ServerFilesManager;
import spark.Request;
import flogo.service.utils.encoders.HashEncoder;
import flogo.service.utils.files.FileReader;
import flogo.service.utils.files.comprehension.ComprehensionUtils;

import java.io.File;

public class FlogoGetDockerFile implements Command{

    private static final String EXPERIMENT_NAME_PARAMETER = "experiment";
    private static final String LABORATORY_NAME_PARAMETER = "laboratory";
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
        String laboratory = laboratoryOf(extractExperimentName(request));
        Concept architecture = serverFilesManager.loadConcept(new ArchitectureConcept(encoder).name(extractExperimentName(request)));
        serverFilesManager.moveToPredict(architecture);
        serverFilesManager.moveBestCheckPoint(laboratory, extractExperimentName(request));
        File zipFile = comprehensionUtils.compress(serverFilesManager.predictFolder());
        serverFilesManager.cleanPredictFolder();
        return responseBuilder.successResponse(FileReader.readBytes(zipFile), "model", "The model has been transferred");
    }

    private String laboratoryOf(String experimentName) {
        return serverFilesManager.readLoggerResult()
                .map(line -> line.split(LOGGER_DELIMITER))
                .filter(lineArray -> lineArray[1].equals(experimentName))
                .map(lineArray -> lineArray[0])
                .findFirst().get();
    }

    private String extractExperimentName(Request request) {
        return request.params(EXPERIMENT_NAME_PARAMETER);
    }

    private String extractLaboratoryName(Request request) {
        return request.params(LABORATORY_NAME_PARAMETER);
    }
}
