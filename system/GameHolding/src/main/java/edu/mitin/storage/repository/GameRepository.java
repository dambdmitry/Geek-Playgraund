package edu.mitin.storage.repository;

import edu.mitin.storage.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
