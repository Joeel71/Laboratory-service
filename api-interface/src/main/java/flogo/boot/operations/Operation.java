package flogo.boot.operations;

import flogo.boot.concept.Concept;
import flogo.boot.concept.ConceptSelector;
import flogo.boot.utils.api.ApiConnection;
import flogo.boot.utils.file.FileReader;

import java.io.File;

public abstract class Operation {

    protected ApiConnection apiConnection;
    protected ConceptSelector conceptSelector;

    public Operation apiConnection(ApiConnection apiConnection){
        this.apiConnection = apiConnection;
        return this;
    }

    public Operation conceptSelector(ConceptSelector conceptSelector){
        this.conceptSelector = conceptSelector;
        return this;
    }

    protected String getConcept(String[] args) {
        return args[1];
    }

    protected String getName(String[] args) {
        return args[2];
    }
    public abstract String execute(String[] args);
}
