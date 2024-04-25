package flogo.boot.concept;

import flogo.boot.utils.converters.ByteArrayConverter;

import java.util.HashMap;
import java.util.Map;

public class ConceptSelector {

    private static final Map<String, Concept> CONCEPT_MAP = new HashMap<>();
    static {
        CONCEPT_MAP.put("laboratory", new LaboratoryConcept());
        CONCEPT_MAP.put("dataset", new DatasetConcept());
        CONCEPT_MAP.put("architecture", new ArchitectureConcept());
    }

    private final ByteArrayConverter byteArrayConverter;

    public ConceptSelector(ByteArrayConverter byteArrayConverter) {
        this.byteArrayConverter = byteArrayConverter;
    }

    public Concept select(String concept){
        return CONCEPT_MAP.get(concept)
                .byteArrayConverter(byteArrayConverter);
    }
}
