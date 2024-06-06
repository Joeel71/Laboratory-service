package flogo.boot.operations;

public class GetModelOperation extends Operation{

    private static final String MODEL_PATH = "model";
    @Override
    public String execute(String[] args) {
        return apiConnection.getObject(MODEL_PATH, getArchitectureName(args), getPath(args));
    }

    private String getArchitectureName(String[] args) {
        return args[1];
    }

    private String getPath(String[] args) {
        return args[2];
    }
}
