package flogo.boot.operations;

import flogo.boot.concept.Concept;

public class ListOperation extends Operation {
    @Override
    public String execute(String[] args) {
        Concept concept = conceptSelector.select(getConcept(args));
        return apiConnection.listObjects(concept.conceptName());
    }
}
