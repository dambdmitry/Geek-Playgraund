package edu.mitin.playground;

import edu.mitin.playground.model.Role;
import edu.mitin.playground.model.User;
import edu.mitin.playground.repository.RoleRepository;
import edu.mitin.playground.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service implements UserService{
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(String username, String password) {
        User userFromDB = userRepository.findByUsername(username);
        if (userFromDB != null) {
            return false;
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        return false;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}
