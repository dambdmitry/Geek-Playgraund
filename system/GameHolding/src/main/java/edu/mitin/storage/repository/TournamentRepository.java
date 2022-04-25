package edu.mitin.storage.repository;

import edu.mitin.storage.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @Modifying
    @Query("update Tournament tour set tour.status = :status where tour.id = :id")
    int setTournamentStatus(@Param("status") String status, @Param("id") Long id);
}
