package edu.mitin.playground.users.admin.repository;

import edu.mitin.playground.users.admin.entity.OrganizerRequest;
import edu.mitin.playground.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<OrganizerRequest, Long> {
}
