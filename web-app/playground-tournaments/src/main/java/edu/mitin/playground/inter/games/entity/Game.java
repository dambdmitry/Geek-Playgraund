package edu.mitin.playground.inter.games.entity;

import javax.persistence.*;

@Entity
@Table(name = "game_interface")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String rules;


    public Game(String name) {
        this.name = name;
    }


    public Game(Long id, String name, String description, String rules) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rules = rules;
    }

    public Game(String name, String description, String rules) {
        this.name = name;
        this.description = description;
        this.rules = rules;
    }

    public Game() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
