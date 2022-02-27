package edu.mitin.playground.tournaments.repository;

import edu.mitin.playground.users.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.mitin.playground.tournaments.model.PlayersTournamentsRelation;
import edu.mitin.playground.tournaments.model.Tournament;

import java.util.List;

public interface PlayersTournamentsRelationRepository extends JpaRepository<PlayersTournamentsRelation, Long> {
    List<PlayersTournamentsRelation> findPlayersTournamentsRelationsByTournament(Tournament tournament);

    List<PlayersTournamentsRelation> findPlayersTournamentsRelationsByPlayer(Player player);
}
