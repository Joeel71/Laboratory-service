package flogo.boot.concept;

import flogo.boot.utils.file.Zipper;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class DatasetConcept extends Concept{

    private static final String METADATA_FILENAME = "meta-dataset.tsv";
    private static final String FILE_DELIMITER = "/";
    private static final DatasetNameExtractor NAME_EXTRACTOR = new DatasetNameExtractor();
    private static final String ZIP_NAME = "attachment";
    private File datasetFile;
    private File zipFile;

    @Override
    public Concept readFrom(String path){
        this.datasetFile = new File(path);
        return super.readFrom(path + FILE_DELIMITER + METADATA_FILENAME);
    }

    @Override
    public String conceptName() {
        return "dataset";
    }

    @Override
    public ByteArrayOutputStream toOutputStream() {
        String serialization = jsonSerialization(NAME_EXTRACTOR);
        zipFile = zipFile(datasetFile);
        return byteArrayConverter.addJson(serialization, JSON_NAME)
                .addZipFile(zipFile, ZIP_NAME)
                .convert();
    }

    public void deleteZipFile(){
        zipFile.delete();
    }

    private File zipFile(File file) {
        return Zipper.zip(file);
    }

    private static class DatasetNameExtractor implements NameExtractor{
        private static final String DATASET_NAME_MARK = "name\t";
        @Override
        public String extract(String content) {
            return content.split(DATASET_NAME_MARK)[1].split("\n")[0];
        }
    }
}
