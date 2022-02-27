package edu.mitin.playground.tournaments.model;

import edu.mitin.playground.users.model.Player;

import javax.persistence.*;

@Entity
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
