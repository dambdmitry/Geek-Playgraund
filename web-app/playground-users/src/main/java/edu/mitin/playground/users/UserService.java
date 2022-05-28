package edu.mitin.playground.users;

import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    boolean registerUser(User user);

    boolean registerPlayer(User user);

    Optional<Player> getPlayerByUserAccount(User user);

    Optional<User> getUserByUsername(String username);

    List<Player> getAllPlayers();

}
