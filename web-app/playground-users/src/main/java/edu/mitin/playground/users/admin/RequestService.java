package edu.mitin.playground.users.admin;

import edu.mitin.playground.users.admin.model.OrganizerRequest;
import org.springframework.stereotype.Service;

@Service
public interface RequestService {
    void registerRequest(OrganizerRequest request, String username);
}
