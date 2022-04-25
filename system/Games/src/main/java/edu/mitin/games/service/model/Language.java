package edu.mitin.games.service.model;

import java.util.List;

/**
 * Если компиляция и выполнение в 2 шага то обязательно к заполнению поле commandToCompile, иначе ставим null
 * в fileExtension всегда пишем расширение файла с программой, exe писать не требуется
 */
public enum Language {
    JAVA("java", ".java"),
    PYTHON("python", ".py"),
    PASCAL("PascalABC.NET",".pas"),
    C("C",".c"),
    CPP("C++", ".cpp"),
    PHP("PHP", ".php");

    private String name;
    private String fileExtension;
    private static final List<Language> twoStepCompileLanguages = List.of(PASCAL, C, CPP);

    Language(String name, String fileExtension) {
        this.name = name;
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getName() {
        return name;
    }

    public boolean isTwoStepExecution(){
        return twoStepCompileLanguages.contains(this);
    }
}
