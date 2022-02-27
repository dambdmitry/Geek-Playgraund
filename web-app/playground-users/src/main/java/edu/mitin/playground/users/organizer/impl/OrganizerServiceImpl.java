package edu.mitin.playground.users.organizer.impl;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.admin.model.OrganizerRequest;
import edu.mitin.playground.users.admin.repository.RequestRepository;
import edu.mitin.playground.users.model.User;
import edu.mitin.playground.users.organizer.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizerServiceImpl implements OrganizerService {
    private final UserService service;

    private final RequestRepository requestRepository;

    @Autowired
    public OrganizerServiceImpl(UserService service, RequestRepository requestRepository) {
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
        request.setRequester(requester);
        requestRepository.save(request);
    }
}
