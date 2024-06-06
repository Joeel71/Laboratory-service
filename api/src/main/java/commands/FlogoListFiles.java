package commands;

import api.ResponseBuilder;
import concepts.Concept;
import concepts.ServerFilesManager;
import spark.Request;

public class FlogoListFiles implements Command {

    private final ServerFilesManager serverFilesManager;
    private final Concept concept;

    public FlogoListFiles(ServerFilesManager serverFilesManager, Concept concept) {
        this.serverFilesManager = serverFilesManager;
        this.concept = concept;
    }

    @Override
    public String execute(Request request, ResponseBuilder responseBuilder) {
        String[] available = serverFilesManager.listAvailable(concept);
        if (available.length == 0)
            return responseBuilder.successResponse("There is not any file uploaded to the server");
        return responseBuilder.successResponse(available);
    }
}
