package edu.mitin.performance.factory;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompilerFactory {
    private final Parser jsonCompilersParser;

    public CompilerFactory(Parser jsonCompilersParser) {
        this.jsonCompilersParser = jsonCompilersParser;
    }

    public Compiler getCompiler(String compilerId) {
        final List<Compiler> compilersFromFile = jsonCompilersParser.getCompilersFromFile();
        return compilersFromFile.stream().filter(c -> c.getId().equals(compilerId)).findFirst().get();
    }
}
