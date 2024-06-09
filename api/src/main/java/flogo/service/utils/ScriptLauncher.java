package flogo.service.utils;

import java.io.IOException;

public class ScriptLauncher {

    private static final String VIRTUAL_VENV_PATH = "/execute/flogo/Scripts/python.exe";
    private final String initialPath;
    private Process process;

    public ScriptLauncher(String initialPath) {
        this.initialPath = initialPath;
        this.process = null;
    }

    public ScriptLauncher launch(String fileToExecute){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(initialPath + VIRTUAL_VENV_PATH, fileToExecute);
            process = processBuilder.start();
            process.waitFor();
            return this;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ScriptLauncher waitProcess(){
        try {
            if (process == null) return this;
            process.waitFor();
            return this;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
