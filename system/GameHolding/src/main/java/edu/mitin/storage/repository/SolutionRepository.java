package edu.mitin.storage.repository;

import edu.mitin.storage.entity.Solution;
import edu.mitin.storage.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Optional<Solution> findByPlayerNameAndTournament(String playerName, Tournament tournament);

    List<Solution> findAllByTournament(Tournament tournament);
}
