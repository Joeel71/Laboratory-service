package concepts;

import utils.encoders.HashEncoder;

import java.util.Arrays;

public class LaboratoryConcept extends Concept {

    private static final String DATASET_NAME_MARK = "DatasetGenerator\\(name=\"";
    private static final String ARCHITECTURE_NAME_MARK = "Experiment\\(name=\"";

    public LaboratoryConcept(HashEncoder encoder) {
        super(encoder);
    }

    public String datasetName() {
        return content.split(DATASET_NAME_MARK)[1].split("\"")[0];
    }

    public String[] architecturesNames() {
        String[] laboratoryArray = content.split(ARCHITECTURE_NAME_MARK);
        return Arrays.stream(Arrays.copyOfRange(laboratoryArray, 1, laboratoryArray.length))
                .map(experiment -> experiment.split("\"")[0])
                .toArray(String[]::new);
    }
}
