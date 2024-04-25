package api;

import spark.Response;

import java.io.IOException;

public class ResponseBuilder {

    private static final int ERROR_STATUS = 500;
    private static final int SUCCESS_STATUS = 200;
    private Response response;

    public ResponseBuilder response(Response response){
        this.response = response;
        return this;
    }

    public String successResponse(String message) {
        response.status(SUCCESS_STATUS);
        return message;
    }

    public String errorResponse(String errorMessage) {
        response.status(ERROR_STATUS);
        return errorMessage;
    }

    public String successResponse(byte[] bytes, String filename, String message) {
        try {
            response.header("Content-Disposition", "attachment; filename=" + filename);
            response.type("application/file");
            response.raw().getOutputStream().write(bytes);
            response.raw().getOutputStream().flush();
            response.raw().getOutputStream().close();
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
