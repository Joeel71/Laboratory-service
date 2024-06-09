package flogo.service.concepts;

import flogo.service.utils.encoders.HashEncoder;

public class DatasetConcept extends Concept{

    private byte[] datasetFile;

    public DatasetConcept(HashEncoder encoder) {
        super(encoder);
    }

    public DatasetConcept datasetFile(byte[] datasetFile) {
        this.datasetFile = datasetFile;
        return this;
    }

    @Override
    public byte[] content() {
        return datasetFile;
    }

    @Override
    public boolean hasContent(){
        return datasetFile != null;
    }
}
