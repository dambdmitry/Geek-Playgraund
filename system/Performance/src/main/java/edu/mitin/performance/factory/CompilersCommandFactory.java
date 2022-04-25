package edu.mitin.performance.factory;

import edu.mitin.games.service.model.Language;

public abstract class CompilersCommandFactory {
    public static String[] createCompileCommand(Language language, String pathToCompileFile) {
        switch (language) {
            case C: return new String[] {"gcc", "-o", pathToCompileFile.split("\\.")[0], pathToCompileFile};
            case PASCAL: return new String[] {"pabcnetcclear", pathToCompileFile};
            case CPP: return new String[] {"g++", "-o", pathToCompileFile.split("\\.")[0], pathToCompileFile};
            default: throw new RuntimeException(language.getName() + " - компилируется в 1 этап");
        }
    }

    public static String[] createExecuteCommand(Language language, String pathToExecuteFile) {
        switch (language) {
            case C:
            case PASCAL:
            case CPP:
                return new String[]{pathToExecuteFile.trim()};
            case JAVA: return new String[]{"java", pathToExecuteFile};
            case PYTHON: return new String[]{"python", pathToExecuteFile};
            case PHP: return new String[]{"php", "-f", pathToExecuteFile};
            default: return null;
        }
    }
}
