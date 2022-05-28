package edu.mitin.playground.users.impl;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.Role;
import edu.mitin.playground.users.entity.User;
import edu.mitin.playground.users.repository.PlayerRepository;
import edu.mitin.playground.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PlayerRepository playerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean registerUser(User user) {
        Optional<User> userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB.isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean registerPlayer(User user) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isEmpty()) {
            return false;
        }
        User playerAccount = byUsername.get();
        if (playerRepository.findByAccount(playerAccount).isPresent()) {
            return false;
        }
        Player player = new Player();
        player.setAccount(playerAccount);
        player.setPoints(0);
        playerRepository.save(player);

        playerAccount.setRole(Role.PLAYER);
        userRepository.save(playerAccount);
        return true;
    }

    @Override
    public Optional<Player> getPlayerByUserAccount(User user) {
        return playerRepository.findByAccount(user);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        UserDetails user = null;
        try {
            user = loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            System.out.println("Пользователя не существует");
        }
        return Optional.ofNullable((User)user);
    }

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll(Sort.by("points"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }
}
