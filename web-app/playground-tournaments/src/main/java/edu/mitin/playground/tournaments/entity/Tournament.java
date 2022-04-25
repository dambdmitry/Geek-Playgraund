package edu.mitin.playground.tournaments.entity;

import edu.mitin.playground.games.entity.Game;
import edu.mitin.playground.users.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn()
    private User owner;

    @ManyToOne
    @JoinColumn()
    private Game game;

    private String name;
    private Integer playersCount;
    private String status;
    private String secretKey;


    public Tournament(User owner, String name, Integer playersCount, String status) {
        this.owner = owner;
        this.name = name;
        this.playersCount = playersCount;
        this.status = status;
    }

    public Tournament(User owner, Game game, String name, String status) {
        this.owner = owner;
        this.game = game;
        this.name = name;
        this.status = status;
    }

    public Tournament() {

    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayersCount(Integer playersCount) {
        this.playersCount = playersCount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
