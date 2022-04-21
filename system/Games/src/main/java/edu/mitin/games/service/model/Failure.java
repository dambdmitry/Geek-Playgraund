package edu.mitin.games.service.model;

public class Failure {
    private String author;
    private String description;

    public Failure(String author, String description) {
        this.author = author;
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }
}
