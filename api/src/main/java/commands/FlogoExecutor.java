package commands;

import api.ResponseBuilder;
import concepts.*;
import spark.Request;
import utils.ScriptLauncher;
import utils.encoders.HashEncoder;

public class FlogoExecutor implements Command {

    private static final String ARCHITECTURE_PARAMETER = "architecture";
    private static final String LABORATORY_PARAMETER = "laboratory";
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
        if (!serverFilesManager.moveToExecute(new ArchitectureConcept(encoder).name(architectureName(request))))
            return builder.errorResponse(architectureName(request) + " have not been uploaded");
        if (!serverFilesManager.moveToExecute(new DatasetConcept(encoder).name(getDatasetName(laboratory))))
            return builder.errorResponse(new DatasetConcept(encoder).name(getDatasetName(laboratory)) + " have not been uploaded");

        serverFilesManager.decompressDatasetExecuteFile();
        execute();
        serverFilesManager.cleanExecuteFolder();
        return builder.successResponse("Execution completed");
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
