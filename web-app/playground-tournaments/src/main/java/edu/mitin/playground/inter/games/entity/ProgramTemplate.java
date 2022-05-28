package edu.mitin.playground.inter.games.entity;

import javax.persistence.*;

@Entity
@Table(name = "program_template")
public class ProgramTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn()
    private Game game;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String template;

    public ProgramTemplate(Game game, String language, String template) {
        this.game = game;
        this.language = language;
        this.template = template;
    }

    public ProgramTemplate() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
