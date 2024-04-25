package flogo.boot.concept;


import java.io.ByteArrayOutputStream;

public class LaboratoryConcept extends Concept {

    private static final LaboratoryNameExtractor NAME_EXTRACTOR = new LaboratoryNameExtractor();

    @Override
    public String conceptName() {
        return "laboratory";
    }

    @Override
    public ByteArrayOutputStream toOutputStream() {
        String serialization = jsonSerialization(NAME_EXTRACTOR);
        return byteArrayConverter.addJson(serialization, JSON_NAME).convert();
    }

    private static class LaboratoryNameExtractor implements NameExtractor {
        private static final String LABORATORY_NAME_MARK = "Laboratory\\(";
        @Override
        public String extract(String content) {
            return content.split(LABORATORY_NAME_MARK)[1].split("\"")[1];
        }
    }
}
