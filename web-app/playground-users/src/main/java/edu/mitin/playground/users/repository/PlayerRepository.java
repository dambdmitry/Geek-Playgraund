package edu.mitin.playground.users.repository;

import edu.mitin.playground.users.model.Player;
import edu.mitin.playground.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUserAccount(User user);
}
