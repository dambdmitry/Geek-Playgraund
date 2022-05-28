package edu.mitin.playground.inter.tournaments.repository;

import edu.mitin.playground.inter.tournaments.entity.PlayersTournamentsRelation;
import edu.mitin.playground.inter.tournaments.entity.Tournament;
import edu.mitin.playground.users.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Transactional
public interface PlayersTournamentsRelationRepository extends JpaRepository<PlayersTournamentsRelation, Long> {
    List<PlayersTournamentsRelation> findPlayersTournamentsRelationsByTournament(Tournament tournament);

    List<PlayersTournamentsRelation> findPlayersTournamentsRelationsByPlayer(Player player);

    PlayersTournamentsRelation findByTournamentAndPlayer(Tournament tournament, Player player);

    @Modifying
    @Query("update PlayersTournamentsRelation pt set pt.points = :points where pt.id = :id")
    void setPlayerPoints(@Param("points") Integer points, @Param("id") Long id);
}
