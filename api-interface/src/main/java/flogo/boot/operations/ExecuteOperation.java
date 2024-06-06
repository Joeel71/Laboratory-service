package flogo.boot.operations;

public class ExecuteOperation extends Operation{

    @Override
    public String execute(String[] args) {
        return apiConnection.execute(queryParamsList(args));
    }

    private String[] queryParamsList(String[] args) {
        return new String[]{lab(args)};
    }


    private String lab(String[] args) {
        return args[1];
    }
}
