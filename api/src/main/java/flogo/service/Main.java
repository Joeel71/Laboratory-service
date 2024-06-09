package flogo.service;

import flogo.service.api.WebService;
import flogo.service.concepts.ArchitectureConcept;
import flogo.service.concepts.DatasetConcept;
import flogo.service.concepts.LaboratoryConcept;
import flogo.service.concepts.ServerFilesManager;
import flogo.service.commands.*;
import flogo.service.commands.FlogoStat;
import flogo.service.commands.stats.BestExperimentStat;
import flogo.service.commands.stats.ExperimentStat;
import flogo.service.commands.stats.ExperimentsOnLaboratoryStat;
import flogo.service.utils.ScriptLauncher;
import flogo.service.utils.encoders.HashEncoder;
import flogo.service.utils.encoders.MD5HashEncoder;
import flogo.service.utils.files.comprehension.ComprehensionUtils;
import flogo.service.utils.files.comprehension.ZipUtils;

public class Main {

    public static void main(String[] args) {

        String serverPath = getServerPath(args);

        MD5HashEncoder md5HashEncoder = new MD5HashEncoder();
        ScriptLauncher scriptLauncher = new ScriptLauncher(serverPath);
        WebService webService = new WebService();

        addGets(webService, new ServerFilesManager(serverPath, new ZipUtils()), md5HashEncoder, new ZipUtils());
        addPost(webService, new ServerFilesManager(serverPath, new ZipUtils()), md5HashEncoder, new ZipUtils(), scriptLauncher);
        addDeletes(webService, new ServerFilesManager(serverPath, new ZipUtils()), md5HashEncoder);

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
        webService.addGet("/flogo/model/:laboratory/:experiment", new FlogoGetDockerFile(serverFilesManager, comprehensionUtils, encoder));
    }

    private static void addDeletes(WebService webService, ServerFilesManager serverFilesManager, MD5HashEncoder encoder){
        webService.addDelete("/flogo/architecture/:name", new FlogoDeleteFile(serverFilesManager, new ArchitectureConcept(encoder)))
                .addDelete("/flogo/laboratory/:name", new FlogoDeleteFile(serverFilesManager, new LaboratoryConcept(encoder)))
                .addDelete("/flogo/dataset/:name", new FlogoDeleteFile(serverFilesManager, new DatasetConcept(encoder)));

    }

    private static FlogoStat createLaboratoryStat(ServerFilesManager serverFilesManager) {
        return new FlogoStat(serverFilesManager)
                .addStat("best-experiment", new BestExperimentStat())
                .addStat("experiments-on-laboratory", new ExperimentsOnLaboratoryStat())
                .addStat("experiment-stat", new ExperimentStat());
    }

    private static String getServerPath(String[] args) {
        return args[0];
    }
}
