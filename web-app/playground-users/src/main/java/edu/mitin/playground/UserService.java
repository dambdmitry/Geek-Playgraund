package edu.mitin.playground;

import edu.mitin.playground.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    boolean registerUser(String login, String password);

    User getUserById(Long id);


    Optional<User> getUserByUsername(String username);


}
