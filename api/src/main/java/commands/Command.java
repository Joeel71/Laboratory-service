package commands;

import api.ResponseBuilder;
import spark.Request;

public interface Command {

    String execute(Request request, ResponseBuilder responseBuilder);
}
