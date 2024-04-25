package concepts;

import utils.encoders.HashEncoder;
import utils.files.FileWriter;
import utils.files.comprehension.ComprehensionUtils;

import java.io.File;
import java.util.Map;

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
