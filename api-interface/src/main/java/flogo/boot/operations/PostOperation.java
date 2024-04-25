package flogo.boot.operations;

import flogo.boot.concept.Concept;

public class PostOperation extends Operation{

    @Override
    public String execute(String[] args) {
        Concept concept = conceptSelector.select(getConcept(args)).readFrom(getPath(args));
        return apiConnection.postObject(concept.conceptName(), concept.toOutputStream());
    }

    private String getPath(String[] args) {
        return args[2];
    }
}
