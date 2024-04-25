package flogo.boot.concept;


import java.io.ByteArrayOutputStream;

public class ArchitectureConcept extends Concept {

    private static final ArchitectureNameExtractor NAME_EXTRACTOR = new ArchitectureNameExtractor();

    @Override
    public String conceptName() {
        return "architecture";
    }

    @Override
    public ByteArrayOutputStream toOutputStream() {
        String serialization = jsonSerialization(NAME_EXTRACTOR);
        return byteArrayConverter.addJson(serialization, JSON_NAME).convert();
    }

    private static class ArchitectureNameExtractor implements NameExtractor {
        private static final String ARCHITECTURE_NAME_MARK = "Architecture\\(";
        @Override
        public String extract(String content) {
            return content.split(ARCHITECTURE_NAME_MARK)[1].split("\"")[1];
        }
    }
}
