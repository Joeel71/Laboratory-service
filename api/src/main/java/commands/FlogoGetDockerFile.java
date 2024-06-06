package commands;

import api.ResponseBuilder;
import concepts.ArchitectureConcept;
import concepts.Concept;
import concepts.ServerFilesManager;
import spark.Request;
import utils.encoders.HashEncoder;
import utils.files.FileReader;
import utils.files.comprehension.ComprehensionUtils;

import java.io.File;

public class FlogoGetDockerFile implements Command{

    private static final String ARCHITECTURE_NAME_PARAMETER = "architecture";
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
//        serverFilesManager.cleanPredictFolder(architecture);
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
        return request.params(ARCHITECTURE_NAME_PARAMETER);
    }
}
