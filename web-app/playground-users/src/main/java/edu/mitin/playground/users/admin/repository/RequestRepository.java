package edu.mitin.playground.users.admin.repository;

import edu.mitin.playground.users.admin.model.OrganizerRequest;
import edu.mitin.playground.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<OrganizerRequest, Long> {
    Optional<OrganizerRequest> findByRequester(User user);
}
