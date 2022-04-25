package edu.mitin.playground.tournaments.entity;

import edu.mitin.playground.users.entity.Player;

import javax.persistence.*;

@Entity
@Table(name = "players_tournaments_relation")
public class PlayersTournamentsRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn()
    private Tournament tournament;

    @ManyToOne
    @JoinColumn()
    private Player player;

    private Integer points;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
