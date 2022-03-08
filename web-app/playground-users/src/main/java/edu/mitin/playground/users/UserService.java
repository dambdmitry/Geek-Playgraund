package edu.mitin.playground.users;

import edu.mitin.playground.users.model.Player;
import edu.mitin.playground.users.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    boolean registerUser(User user);

    User getUserById(Long id);

    boolean registerPlayer(User user);

    Optional<Player> getPlayerByUserAccount(User user);

    Optional<User> getUserByUsername(String username);

    List<Player> getAllPlayers();

}
