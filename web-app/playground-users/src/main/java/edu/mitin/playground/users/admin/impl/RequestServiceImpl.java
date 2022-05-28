package edu.mitin.playground.users.admin.impl;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.admin.RequestService;
import edu.mitin.playground.users.admin.entity.OrganizerRequest;
import edu.mitin.playground.users.admin.repository.RequestRepository;
import edu.mitin.playground.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final UserService service;
    private final RequestRepository requestRepository;

    @Autowired
    public RequestServiceImpl(UserService service, RequestRepository requestRepository) {
        this.service = service;
        this.requestRepository = requestRepository;
    }

    @Override
    public void registerRequest(OrganizerRequest request, String username) {
        Optional<User> userByUsername = service.getUserByUsername(username);
        if (userByUsername.isEmpty()) {
            return;
        }
        User requester = userByUsername.get();
        request.setAccount(requester);
        requestRepository.save(request);
    }
}
