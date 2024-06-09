package flogo.service.commands;

import flogo.service.api.ResponseBuilder;
import flogo.service.concepts.Concept;
import flogo.service.concepts.ServerFilesManager;
import spark.Request;

public class FlogoDeleteFile implements Command {

    private static final String NAME_PARAMETER = "name";

    private final ServerFilesManager serverFilesManager;
    private final Concept concept;

    public FlogoDeleteFile(ServerFilesManager serverFilesManager, Concept concept) {
        this.serverFilesManager = serverFilesManager;
        this.concept = concept;
    }

    @Override
    public String execute(Request request, ResponseBuilder responseBuilder) {
        concept.name(extractName(request));
        if (!serverFilesManager.isNameTaken(concept)) return responseBuilder.errorResponse("This file was not on the server");
        serverFilesManager.deleteConcept(concept);
        return responseBuilder.successResponse("File delete");
    }

    private String extractName(Request request) {
        return request.params(NAME_PARAMETER);
    }

}
