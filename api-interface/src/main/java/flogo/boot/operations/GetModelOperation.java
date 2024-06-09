package flogo.boot.operations;

public class GetModelOperation extends Operation{

    private static final String MODEL_PATH = "model";
    @Override
    public String execute(String[] args) {
        return apiConnection.getModel(MODEL_PATH, getExperimentName(args), getExperimentName(args), getPath(args));
    }

    private String getLaboratoryName(String[] args) {
        return args[1];
    }

    private String getExperimentName(String[] args) {
        return args[2];
    }

    private String getPath(String[] args) {
        return args[3];
    }
}
