package concepts;

import utils.encoders.HashEncoder;

public class LaboratoryConcept extends Concept {

    private static final String DATASET_NAME_MARK = "DatasetGenerator\\(name=\"";

    public LaboratoryConcept(HashEncoder encoder) {
        super(encoder);
    }

    public String datasetName() {
        return content.split(DATASET_NAME_MARK)[1].split("\"")[0];
    }
}
