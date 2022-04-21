package edu.mitin.games.service.model;

public enum Language {
    JAVA("java", "java", ".java"),
    PYTHON("python", "python", ".py");

    private String name;
    private String commandToExecution;
    private String fileExtension;

    Language(String name, String commandToExecution, String fileExtension) {
        this.name = name;
        this.commandToExecution = commandToExecution;
        this.fileExtension = fileExtension;
    }

    public String getCommandToExecution() {
        return commandToExecution;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
