package concepts;

import utils.files.FileReader;
import utils.files.FileWriter;
import utils.files.comprehension.ComprehensionUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerFilesManager {

    private static final String CONCEPT_MARKER = "Concept";
    private static final String PATH_DELIMITER = "/";
    private static final String TSV_FILE_DELIMITER = "\t";
    private static final String SERVER_FILE_EXTENSION = ".tsv";
    private static final String EXEC_PATH = "execute/files";
    private static final String REPOSITORY_PATH = "repository";
    private static final String PREDICT_PATH = "predict/model";
    private static final String LOGGER_PATH = "executions/logger/result.tsv";
    private final String initialPath;
    private final ComprehensionUtils comprehensionUtils;

    public ServerFilesManager(String initialPath, ComprehensionUtils comprehensionUtils) {
        this.initialPath = initialPath;
        this.comprehensionUtils = comprehensionUtils;
    }

    public Concept saveConcept(Concept concept) {
        checkFolder(conceptFile(concept));
        FileWriter.write(conceptFile(concept), concept.content());
        FileWriter.writeLine(serverFile(conceptType(concept)), concept.name() + TSV_FILE_DELIMITER + concept.codification());
        return concept;
    }

    public boolean isNameTaken(Concept concept){
        return loadServerFile(concept).containsKey(concept.name());
    }

    public boolean isFileUploaded(Concept concept) {
        return loadServerFile(concept).containsValue(concept.codification);
    }

    public String listAvailable(Concept concept) {
        return String.join("\n", loadServerFile(concept).keySet());
    }

    public Concept loadConcept(Concept concept) {
        if (concept instanceof DatasetConcept datasetConcept) datasetConcept.datasetFile(comprehensionUtils.read(conceptFile(concept)));
        else concept.content(FileReader.read(conceptFile(concept)));
        return concept;
    }

    public boolean deleteConcept(Concept concept) {
        if (!isNameTaken(concept)) return false;
        deleteOnServerFile(concept);
        deleteFile(conceptFile(concept));
        return true;

    }

    public boolean moveToExecute(Concept concept){
        if (!isNameTaken(concept)) return false;
        if (!concept.hasContent()) loadConcept(concept);
        moveTo(concept, executionFile(conceptType(concept)));
        return true;
    }

    public boolean moveToPredict(Concept concept) {
        moveTo(concept, predictFile(conceptType(concept)));
        return true;
    }

    public void decompressDatasetExecuteFile() {
        comprehensionUtils.decompress(executionFile("dataset"));
        deleteFile(executionFile("dataset"));
    }

    public byte[] getModelFile(ComprehensionUtils comprehensionUtils) {
        return comprehensionUtils.read(comprehensionUtils.compress(predictFolder()));
    }

    public void cleanExecuteFolder() {
        deleteFile(executeFolder());
    }

    public String laboratoryExecutePath() {
        return executionFile("laboratory").toString();
    }

    public Stream<String> readLoggerResult() {
        return Arrays.stream(FileReader.read(loggerFile()).split("\n"));
    }

    private File loggerFile() {
        return new File(initialPath + PATH_DELIMITER + LOGGER_PATH);
    }

    private void moveTo(Concept concept, File file) {
        checkFolder(file);
        FileWriter.write(file, concept.content());
    }

    private File executionFile(String conceptType) {
        return new File(executeFolder() + PATH_DELIMITER + conceptType + fileExtension(conceptType));
    }

    private File predictFile(String conceptType) {
        return new File(predictFolder() + PATH_DELIMITER + conceptType + fileExtension(conceptType));
    }

    private File predictFolder() {
        return new File(initialPath + PATH_DELIMITER + PREDICT_PATH);
    }

    private File executeFolder() {
        return new File(initialPath + PATH_DELIMITER + EXEC_PATH);
    }

    private File conceptFile(Concept concept) {
        return new File(initialPath + PATH_DELIMITER + REPOSITORY_PATH +PATH_DELIMITER + conceptType(concept) + PATH_DELIMITER + concept.name() + fileExtension(concept));
    }

    private Map<String, String> loadServerFile(Concept concept) {
        return FileReader.readTsv(serverFile(conceptType(concept)));
    }

    private String fileExtension(Concept concept) {
        if (concept instanceof DatasetConcept) return ".zip";
        return ".py";
    }

    private String fileExtension(String conceptType){
        if (conceptType.equals("dataset")) return ".zip";
        return ".py";
    }

    private String conceptType(Concept concept) {
        return concept.getClass().getSimpleName().replace(CONCEPT_MARKER, "").toLowerCase();
    }

    private void checkFolder(File file) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
    }

    private File serverFile(String conceptType){
        return new File(initialPath + PATH_DELIMITER + REPOSITORY_PATH + PATH_DELIMITER + conceptType + PATH_DELIMITER + conceptType + SERVER_FILE_EXTENSION);
    }

    private void deleteOnServerFile(Concept concept) {
        String fileServerContent = Arrays.stream(FileReader.read(serverFile(conceptType(concept))).split("\n"))
                .filter(line -> !line.split(TSV_FILE_DELIMITER)[0].equals(concept.name()))
                .collect(Collectors.joining("\n"));
        FileWriter.write(serverFile(conceptType(concept)), fileServerContent);
    }

    private void deleteFile(File file){
        if (file.isDirectory())
            Arrays.stream(file.listFiles()).sequential().forEach(this::deleteFile);
        file.delete();
    }
}
