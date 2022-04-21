package edu.mitin.playground.users.admin;

import edu.mitin.playground.users.admin.entity.OrganizerRequest;
import edu.mitin.playground.users.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    void deleteUser(User user);

    void acceptOrganizer(Long requestId);

    void rejectOrganizer(Long requestId);

    List<OrganizerRequest> getRequests();
}
