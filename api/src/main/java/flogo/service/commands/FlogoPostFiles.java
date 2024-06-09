package flogo.service.commands;

import flogo.service.api.ResponseBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import flogo.service.concepts.Concept;
import flogo.service.concepts.DatasetConcept;
import flogo.service.concepts.ServerFilesManager;
import spark.Request;
import flogo.service.utils.files.comprehension.ComprehensionUtils;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FlogoPostFiles implements Command {

    private final ServerFilesManager serverFilesManager;
    private final ComprehensionUtils comprehensionUtils;
    private final Concept concept;

    public FlogoPostFiles(ServerFilesManager serverFilesManager, ComprehensionUtils comprehensionUtils, Concept concept) {
        this.serverFilesManager = serverFilesManager;
        this.comprehensionUtils = comprehensionUtils;
        this.concept = concept;
    }

    @Override
    public String execute(Request request, ResponseBuilder responseBuilder) {
        return manage(request, responseBuilder);
    }

    public String manage(Request request, ResponseBuilder responseBuilder) {
        if (concept instanceof DatasetConcept datasetConcept)
            datasetConcept.datasetFile(comprehensionUtils.download(request));
        byte[] definition = extractDefinition(request);
        concept.name(extractName(definition))
                .content(extractContent(definition));
        if (serverFilesManager.isFileUploaded(concept))
            return responseBuilder.errorResponse("File already uploaded");
        if (serverFilesManager.isNameTaken(concept))
            return responseBuilder.errorResponse("Name already selected");
        serverFilesManager.saveConcept(concept);
        return responseBuilder.successResponse("The object named " + concept.name() + " have been uploaded");
    }

    private String extractContent(byte[] jsonContent) {
        String jsonString = new String(jsonContent, StandardCharsets.UTF_8);
        return new Gson().fromJson(jsonString, JsonObject.class).get("content").getAsString();
    }

    private String extractName(byte[] jsonContent) {
        String jsonString = new String(jsonContent, StandardCharsets.UTF_8);
        return new Gson().fromJson(jsonString, JsonObject.class).get("name").getAsString();
    }

    private byte[] extractDefinition(Request request){
        try {
            return request.raw().getPart("definition").getInputStream().readAllBytes();
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
