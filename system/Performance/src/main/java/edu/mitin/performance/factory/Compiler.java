package edu.mitin.performance.factory;

import java.io.Serializable;

public class Compiler implements Serializable {

    private final static String INPUT_PLACEHOLDER = "@inputFile";
    private final static String OUTPUT_PLACEHOLDER = "@outputFile";


    private String id;
    private String name;
    private String fileExtension;
    private String compileCommand;
    private String executeCommand;

    public Compiler(String id, String name, String fileExtension, String compileCommand, String executeCommand) {
        this.id = id;
        this.name = name;
        this.fileExtension = fileExtension;
        this.compileCommand = compileCommand;
        this.executeCommand = executeCommand;
    }

    public Compiler() {
    }

    public boolean isTwoStepCompileLanguage() {
        return !compileCommand.isEmpty();
    }

    public String[] getCompileCommand(String pathToCompileFile, String pathToOutputExeFile) {
        pathToCompileFile = pathToCompileFile.replaceAll("\\\\", "\\\\\\\\");
        pathToOutputExeFile = pathToOutputExeFile.replaceAll("\\\\", "\\\\\\\\");
        String command = compileCommand.replaceAll(INPUT_PLACEHOLDER, pathToCompileFile)
                .replaceAll(OUTPUT_PLACEHOLDER, pathToOutputExeFile);
        return command.trim().split(" ");
    }

    public String[] getExecutionCommand(String pathToExecutionFile) {
        String command = executeCommand.replaceAll(INPUT_PLACEHOLDER, pathToExecutionFile.replaceAll("\\\\", "\\\\\\\\"));
        return command.trim().split(" ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Compiler compiler = (Compiler) o;

        return id.equals(compiler.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getCompileCommand() {
        return compileCommand;
    }

    public void setExecuteCommand(String executeCommand) {
        this.executeCommand = executeCommand;
    }
}
