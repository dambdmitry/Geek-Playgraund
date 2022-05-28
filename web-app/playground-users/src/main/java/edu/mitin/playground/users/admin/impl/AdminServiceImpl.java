package edu.mitin.playground.users.admin.impl;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.admin.AdminService;
import edu.mitin.playground.users.admin.entity.OrganizerRequest;
import edu.mitin.playground.users.admin.repository.RequestRepository;
import edu.mitin.playground.users.entity.Role;
import edu.mitin.playground.users.entity.User;
import edu.mitin.playground.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    final RequestRepository requestRepository;
    final UserRepository userRepository;
    final UserService userService;

    public AdminServiceImpl(RequestRepository requestRepository, UserRepository userRepository, UserService userService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public void acceptOrganizer(Long requestId) {
        Optional<OrganizerRequest> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            return;
        }
        User requester = request.get().getAccount();
        Optional<User> byUsername = userRepository.findByUsername(requester.getUsername());
        if (byUsername.isEmpty()) {
            return;
        }
        User organizerAccount = byUsername.get();
        organizerAccount.setRole(Role.ORGANIZER);
        userRepository.save(organizerAccount);
        requestRepository.deleteById(requestId);
    }

    @Override
    public void rejectOrganizer(Long requestId) {
        requestRepository.deleteById(requestId);
    }

    @Override
    public List<OrganizerRequest> getRequests() {
        return requestRepository.findAll();
    }
}
