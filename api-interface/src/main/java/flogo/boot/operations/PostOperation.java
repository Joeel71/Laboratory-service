package flogo.boot.operations;

import flogo.boot.concept.Concept;
import flogo.boot.concept.DatasetConcept;

public class PostOperation extends Operation{

    @Override
    public String execute(String[] args) {
        Concept concept = conceptSelector.select(getConcept(args)).readFrom(getPath(args));
        String response = apiConnection.postObject(concept.conceptName(), concept.toOutputStream());
        if (concept instanceof DatasetConcept datasetConcept)
            datasetConcept.deleteZipFile();
        return response;
    }

    private String getPath(String[] args) {
        return args[2];
    }
}
