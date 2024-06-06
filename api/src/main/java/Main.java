import api.WebService;
import concepts.ArchitectureConcept;
import concepts.DatasetConcept;
import concepts.LaboratoryConcept;
import concepts.ServerFilesManager;
import commands.*;
import commands.FlogoStat;
import commands.stats.BestExperimentStat;
import commands.stats.ExperimentStat;
import commands.stats.ExperimentsOnLaboratoryStat;
import utils.ScriptLauncher;
import utils.encoders.HashEncoder;
import utils.encoders.MD5HashEncoder;
import utils.files.comprehension.ComprehensionUtils;
import utils.files.comprehension.ZipUtils;

public class Main {

    private static final String INITIAL_PATH = "C:/Users/Joel/Desktop/test_api/flogo";

    public static void main(String[] args) {

        MD5HashEncoder md5HashEncoder = new MD5HashEncoder();
        ScriptLauncher scriptLauncher = new ScriptLauncher(INITIAL_PATH);
        WebService webService = new WebService();

        addGets(webService, new ServerFilesManager(INITIAL_PATH, new ZipUtils()), md5HashEncoder, new ZipUtils());
        addPost(webService, new ServerFilesManager(INITIAL_PATH, new ZipUtils()), md5HashEncoder, new ZipUtils(), scriptLauncher);
        addDeletes(webService, new ServerFilesManager(INITIAL_PATH, new ZipUtils()), md5HashEncoder);

        webService.start();
        }

    private static void addPost(WebService webService, ServerFilesManager serverFilesManager, MD5HashEncoder encoder, ZipUtils zipUtils, ScriptLauncher scriptLauncher) {
        webService.addPost("/flogo/architecture", new FlogoPostFiles(serverFilesManager, zipUtils, new ArchitectureConcept(encoder)))
                .addPost("/flogo/laboratory", new FlogoPostFiles(serverFilesManager, zipUtils, new LaboratoryConcept(encoder)))
                .addPost("/flogo/dataset", new FlogoPostFiles(serverFilesManager, zipUtils, new DatasetConcept(encoder)))
                .addPost("/flogo/execute/:laboratory", new FlogoExecutor(serverFilesManager, encoder, scriptLauncher));
    }

    private static void addGets(WebService webService, ServerFilesManager serverFilesManager, HashEncoder encoder, ComprehensionUtils comprehensionUtils) {
        webService.addGet("/flogo/architecture/:name", new FlogoGetFile(serverFilesManager, new ArchitectureConcept(encoder)))
                .addGet("/flogo/laboratory/:name", new FlogoGetFile(serverFilesManager, new LaboratoryConcept(encoder)))
                .addGet("/flogo/dataset/:name", new FlogoGetFile(serverFilesManager, new DatasetConcept(encoder)));

        webService.addGet("/flogo/architecture", new FlogoListFiles(serverFilesManager, new ArchitectureConcept(encoder)))
                .addGet("/flogo/laboratory", new FlogoListFiles(serverFilesManager, new LaboratoryConcept(encoder)))
                .addGet("/flogo/dataset", new FlogoListFiles(serverFilesManager, new DatasetConcept(encoder)));

        webService.addGet("/flogo/stats/:stat", createLaboratoryStat(serverFilesManager));
        webService.addGet("/flogo/model/:architecture", new FlogoGetDockerFile(serverFilesManager, comprehensionUtils, encoder));
    }

    private static void addDeletes(WebService webService, ServerFilesManager serverFilesManager, MD5HashEncoder encoder){
        webService.addPost("/flogo/architecture/:name", new FlogoDeleteFile(serverFilesManager, new ArchitectureConcept(encoder)))
                .addPost("/flogo/laboratory/:name", new FlogoDeleteFile(serverFilesManager, new LaboratoryConcept(encoder)))
                .addPost("/flogo/dataset/:name", new FlogoDeleteFile(serverFilesManager, new DatasetConcept(encoder)));

    }

    private static FlogoStat createLaboratoryStat(ServerFilesManager serverFilesManager) {
        return new FlogoStat(serverFilesManager)
                .addStat("best-experiment", new BestExperimentStat())
                .addStat("experiments", new ExperimentsOnLaboratoryStat())
                .addStat("experiment", new ExperimentStat());
    }
}
