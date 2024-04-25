package flogo.boot.operations;

import flogo.boot.concept.ConceptSelector;
import flogo.boot.utils.api.ApiConnection;

import java.util.HashMap;
import java.util.Map;

public class OperationSelector {

    private final ApiConnection apiConnection;
    private final ConceptSelector conceptSelector;
    private final Map<String, Operation> operationMap;

    public OperationSelector(ApiConnection apiConnection, ConceptSelector conceptSelector) {
        this.apiConnection = apiConnection;
        this.conceptSelector = conceptSelector;
        this.operationMap = new HashMap<>();
    }

    public Operation select(String operation){
        return operationMap.get(operation)
                .apiConnection(apiConnection)
                .conceptSelector(conceptSelector);
    }

    public void add(Operation operation, String name) {
        operationMap.put(name, operation);
    }
}
