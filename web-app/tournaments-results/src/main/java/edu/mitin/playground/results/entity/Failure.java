package edu.mitin.playground.results.entity;

import javax.persistence.*;

@Entity
@Table(name = "failure")
public class Failure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorName;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Failure() {
    }

    public Failure(String authorName, String description) {
        this.authorName = authorName;
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDescription() {
        return description;
    }
}
