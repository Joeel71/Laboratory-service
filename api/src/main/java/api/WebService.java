package api;


import commands.Command;
import spark.Spark;

import javax.servlet.MultipartConfigElement;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebService {

    private static final ResponseBuilder RESPONSE_BUILDER = new ResponseBuilder();
    private final Map<String, Command> getCommands;
    private final Map<String, Command> postCommands;
    private final int port;

    public WebService(int port){
        this.getCommands = new HashMap<>();
        this.postCommands = new HashMap<>();
        this.port = port;
    }

    public WebService(){
        this.getCommands = new HashMap<>();
        this.postCommands = new HashMap<>();
        this.port = 8080;
    }

    public void start() {
        Spark.port(port);
        before((request, response) -> request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/ruta/temporal")));

        for (String route : getCommands.keySet())
            Spark.get(route, (req, res) -> getCommands.get(route).execute(req, RESPONSE_BUILDER.response(res)));

        for (String route : postCommands.keySet())
            Spark.post(route, (req, res) -> postCommands.get(route).execute(req, RESPONSE_BUILDER.response(res)));
//        post("/flogo/dataset", (request, response) ->
//                selectManager("post")
//                        .addConcept(new DatasetConcept(encoder).datasetFile(comprehensionUtils.download(request))).execute(request, RESPONSE_BUILDER.response(response)));
    }

    public void addGet(String path, Command command) {
        getCommands.put(path, command);
    }

    public void addPost(String path, Command command){
        postCommands.put(path, command);
    }
}
