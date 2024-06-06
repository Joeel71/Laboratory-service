package commands;

import api.ResponseBuilder;
import concepts.*;
import spark.Request;
import utils.ScriptLauncher;
import utils.encoders.HashEncoder;

public class FlogoExecutor implements Command {

    private static final String ARCHITECTURE_PARAMETER = "architecture";
    private static final String LABORATORY_PARAMETER = "laboratory";
    private static final String EXPECTED_MODE = "test";
    protected static final String LOGGER_DELIMITER = "\t";
    private final ServerFilesManager serverFilesManager;
    private final HashEncoder encoder;
    private final ScriptLauncher scriptLauncher;

    public FlogoExecutor(ServerFilesManager serverFilesManager, HashEncoder encoder, ScriptLauncher scriptLauncher) {
        this.serverFilesManager = serverFilesManager;
        this.encoder = encoder;
        this.scriptLauncher = scriptLauncher;
    }

    @Override
    public String execute(Request request, ResponseBuilder builder) {
        Concept laboratory = serverFilesManager.loadConcept(new LaboratoryConcept(encoder).name(laboratoryName(request)));
        if (!serverFilesManager.moveToExecute(laboratory))
            return builder.errorResponse(laboratory.name() + " have not been uploaded");
        if (!serverFilesManager.moveToExecute(new DatasetConcept(encoder).name(getDatasetName(laboratory))))
            return builder.errorResponse(getDatasetName(laboratory) + " have not been uploaded");

        for(String architectureName: getArchitecturesNames(laboratory))
            if (!serverFilesManager.moveToExecute(new ArchitectureConcept(encoder).name(architectureName)))
                return builder.errorResponse(architectureName(request) + " have not been uploaded");


        serverFilesManager.decompressDatasetExecuteFile();
        execute();
        serverFilesManager.cleanExecuteFolder();
        return builder.successResponse(bestArchitecture(laboratory.name()));
    }

    private String bestArchitecture(String laboratoryName) {
        return serverFilesManager.readLoggerResult()
                .map(line -> line.split(LOGGER_DELIMITER))
                .filter(lineArray -> lineArray[1].equals(laboratoryName))
                .filter(lineArray -> lineArray[6].contains(EXPECTED_MODE))
                .map(lineArray -> lineArray[2])
                .findFirst().get();
    }

    private String[] getArchitecturesNames(Concept laboratory) {
        return ((LaboratoryConcept) laboratory).architecturesNames();
    }

    private void execute() {
        scriptLauncher.launch(serverFilesManager.laboratoryExecutePath())
                .waitProcess();
    }

    private String getDatasetName(Concept laboratory) {
        return ((LaboratoryConcept) laboratory).datasetName();
    }
    private String laboratoryName(Request request) {
        return request.params(LABORATORY_PARAMETER);
    }
    private String architectureName(Request request) {
        return request.params(ARCHITECTURE_PARAMETER);
    }
}
