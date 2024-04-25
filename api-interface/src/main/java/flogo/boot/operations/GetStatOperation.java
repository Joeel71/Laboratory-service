package flogo.boot.operations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GetStatOperation extends Operation{

    private static final String KEY_VALUE_DELIMITER = "=";
    private static final String FILE_PATH_PARAMETER = "file";
    @Override
    public String execute(String[] args) {
        return apiConnection.getStat(stat(args), paramsOnRequest(args));
    }

    private Map<String, String> paramsOnRequest(String[] args) {
        return Arrays.stream(args, 2, args.length)
                .map(arg -> arg.split(KEY_VALUE_DELIMITER))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1], (oldValue, newValue) -> newValue, HashMap::new));
    }

    private String stat(String[] args) {
        return args[1];
    }
}
