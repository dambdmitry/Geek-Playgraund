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

    public Tournament(User owner, String name, Integer playersCount) {
        this.owner = owner;
        this.name = name;
        this.playersCount = playersCount;
    }

    public Tournament() {

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
