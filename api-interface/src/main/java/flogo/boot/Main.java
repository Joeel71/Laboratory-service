package flogo.boot;

import flogo.boot.concept.ConceptSelector;
import flogo.boot.operations.*;
import flogo.boot.utils.converters.ApiByteArrayConverter;
import flogo.boot.utils.api.ApiConnection;
import flogo.boot.utils.api.FlogoApiConnection;

public class Main {

    private static final ApiConnection API_CONNECTION = new FlogoApiConnection();
    private static final ConceptSelector CONCEPT_SELECTOR = new ConceptSelector(new ApiByteArrayConverter());


    public static void main(String[] args) {
        OperationSelector operationSelector = new OperationSelector(API_CONNECTION, CONCEPT_SELECTOR);
        addOperations(operationSelector);
        Operation operation = operationSelector.select(getOperation(args));
        System.out.println(operation.execute(args));
    }

    private static void addOperations(OperationSelector operationSelector) {
        operationSelector.add(new GetOperation(), "get");
        operationSelector.add(new PostOperation(), "post");
        operationSelector.add(new ListOperation(), "list");
        operationSelector.add(new GetStatOperation(), "stat");
        operationSelector.add(new DeleteOperation(), "delete");
        operationSelector.add(new ExecuteOperation(), "execute");
        operationSelector.add(new GetModelOperation(), "model");
    }

    private static String getOperation(String[] args) {
        return args[0];
    }
}