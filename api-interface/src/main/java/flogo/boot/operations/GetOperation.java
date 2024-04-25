package flogo.boot.operations;

import flogo.boot.concept.Concept;

public class GetOperation extends Operation {
    @Override
    public String execute(String[] args) {
        Concept concept = conceptSelector.select(getConcept(args));
        return apiConnection.getObject(concept.conceptName(), getName(args), getPath(args));
    }

    private String getPath(String[] args) {
        return args[3];
    }
}
