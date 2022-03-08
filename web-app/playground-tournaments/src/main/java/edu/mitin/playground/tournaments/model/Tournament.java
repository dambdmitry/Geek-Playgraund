package edu.mitin.playground.tournaments.model;

import edu.mitin.playground.users.model.User;

import javax.persistence.*;

@Entity
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn()
    private User owner;
    private String name;
    private Integer playersCount;
    private String gameDescription;
    private String gameRules;

    public Tournament(User owner, String name, Integer playersCount) {
        this.owner = owner;
        this.name = name;
        this.playersCount = playersCount;
    }

    public Tournament() {

    }

    public String getGameRules() {
        return gameRules;
    }

    public void setGameRules(String gameRules) {
        this.gameRules = gameRules;
    }

    public void setPlayersCount(Integer playersCount) {
        this.playersCount = playersCount;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public Integer getPlayersCount() {
        return playersCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
