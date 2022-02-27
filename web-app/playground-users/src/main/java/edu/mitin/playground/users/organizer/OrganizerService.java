package edu.mitin.playground.users.organizer;

import edu.mitin.playground.users.admin.model.OrganizerRequest;
import org.springframework.stereotype.Service;

@Service
public interface OrganizerService {
    void registerRequest(OrganizerRequest request, String username);
}
