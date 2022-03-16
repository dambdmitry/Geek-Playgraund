package edu.mitin.demoFORTEST;

public enum Language {
    JAVA_8("Java 1.8", "java"),
    PYTHON_3("Python 3", "python");

    String name;

    String commandToRun;

    Language(String name, String commandToRun) {
        this.name = name;
        this.commandToRun = commandToRun;
    }

    public String getName() {
        return name;
    }
}
