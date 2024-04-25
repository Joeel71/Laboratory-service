package flogo.boot.operations;

import flogo.boot.concept.Concept;

public class DeleteOperation extends Operation {
    @Override
    public String execute(String[] args) {
        Concept concept = conceptSelector.select(getConcept(args));
        return apiConnection.deleteObject(concept.conceptName(), getName(args));
    }
}
