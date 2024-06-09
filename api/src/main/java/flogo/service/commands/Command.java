package flogo.service.commands;

import flogo.service.api.ResponseBuilder;
import spark.Request;

public interface Command {

    String execute(Request request, ResponseBuilder responseBuilder);
}
