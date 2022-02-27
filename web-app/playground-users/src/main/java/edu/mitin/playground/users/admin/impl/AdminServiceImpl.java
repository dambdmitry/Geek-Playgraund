package edu.mitin.playground.users.admin.impl;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.admin.AdminService;
import edu.mitin.playground.users.admin.model.OrganizerRequest;
import edu.mitin.playground.users.admin.repository.RequestRepository;
import edu.mitin.playground.users.model.Role;
import edu.mitin.playground.users.model.User;
import edu.mitin.playground.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    final RequestRepository requestRepository;

    final UserRepository userRepository;

    final
    UserService s;

    public AdminServiceImpl(RequestRepository requestRepository, UserRepository userRepository, UserService s) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.s = s;
        mockRequests();
        mockOrganizer();
    }

    void mockOrganizer(){
        Optional<User> ilonMask = userRepository.findByUsername("IlonMask");
        User mockOrganizer = ilonMask.get();
        mockOrganizer.setRole(Role.ORGANIZER);
        userRepository.save(mockOrganizer);
    }

    void mockRequests() {
        List<User> all = userRepository.findAll();
        for (User user : all) {
            requestRepository.save(new OrganizerRequest(user, user.getUsername() + user.getPassword()));
        }
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void acceptOrganizer(Long requestId) {
        Optional<OrganizerRequest> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            return;
        }
        User requester = request.get().getRequester();
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
        //todo
    }

    @Override
    public List<OrganizerRequest> getRequests() {
        return requestRepository.findAll();
    }
}
