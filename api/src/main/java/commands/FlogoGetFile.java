package commands;

import api.ResponseBuilder;
import concepts.Concept;
import concepts.ServerFilesManager;
import spark.Request;

public class FlogoGetFile implements Command {

    private static final String NAME_PARAMETER = "name";
    private final ServerFilesManager serverFilesManager;
    private final Concept concept;

    public FlogoGetFile(ServerFilesManager serverFilesManager, Concept concept) {
        this.serverFilesManager = serverFilesManager;
        this.concept = concept;
    }
    @Override
    public String execute(Request request, ResponseBuilder responseBuilder) {
        concept.name(extractName(request));
        if (!serverFilesManager.isNameTaken(concept))
            return responseBuilder.errorResponse("This file has not been uploaded to the server");
        return responseBuilder.successResponse(serverFilesManager.loadConcept(concept).content(), concept.name(), "The file has been transferred");
    }

    private String extractName(Request request) {
        return request.params(NAME_PARAMETER);
    }

}
