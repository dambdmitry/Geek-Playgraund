package edu.mitin.storage.repository;

import edu.mitin.storage.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository extends JpaRepository<Round, Long> {
}
