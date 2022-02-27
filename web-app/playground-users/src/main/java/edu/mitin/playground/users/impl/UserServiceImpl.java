package edu.mitin.playground.users.impl;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.model.Player;
import edu.mitin.playground.users.model.Role;
import edu.mitin.playground.users.model.User;
import edu.mitin.playground.users.repository.PlayerRepository;
import edu.mitin.playground.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager em;

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PlayerRepository playerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        initMockUsers();
    }

    private void initMockUsers(){
        List<User> mockUsers = List.of(createUser("Dmitry", passwordEncoder.encode("123456"), Role.ADMIN),
                createUser("IlonMask", passwordEncoder.encode("123456"), Role.USER),
                createUser("Tim", passwordEncoder.encode("123456"), Role.USER),
                createUser("Mary", passwordEncoder.encode("123456"), Role.USER),
                createUser("Simon", passwordEncoder.encode("123456"), Role.USER),
                createUser("Pumba", passwordEncoder.encode("123456"), Role.USER),
                createUser("Vadim", passwordEncoder.encode("123456"), Role.USER));
        userRepository.saveAll(mockUsers);
        for (int i = 2; i < mockUsers.size(); i++) {
            registerPlayer(mockUsers.get(i)); // Все игроки, а илон маск будет организааторос см. AdminServiceImpl
        }
    }

    private User createUser(String username, String password, Role role){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
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
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public boolean registerPlayer(User user) {
        Optional<User> byUsername = userRepository.findByUsername(user.getUsername());
        if (byUsername.isEmpty()) {
            return false;
        }
        User playerAccount = byUsername.get();
        if (playerRepository.findByUserAccount(playerAccount).isPresent()) {
            return false;
        }
        Player player = new Player();
        player.setUserAccount(playerAccount);
        player.setPoints(0);
        playerRepository.save(player);

        playerAccount.setRole(Role.PLAYER);
        userRepository.save(playerAccount);
        return true;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        UserDetails user = null;
        try {
            user = loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            System.out.println("ТАкого неет");
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
