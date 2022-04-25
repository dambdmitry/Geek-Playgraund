package edu.mitin.playground.tournaments.repository;

import edu.mitin.playground.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.mitin.playground.tournaments.entity.Tournament;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findTournamentsByOwner(User owner);

    Optional<Tournament> findTournamentByName(String name);

    Iterable<Tournament> findTournamentsByName(String name);

    List<Tournament> findTournamentsByNameContaining(String name);

    @Modifying
    @Query("update Tournament tour set tour.status = :status where tour.id = :id")
    int setTournamentStatus(@Param("status") String status, @Param("id") Long id);
}
