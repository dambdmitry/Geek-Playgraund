package edu.mitin.storage.repository;

import edu.mitin.storage.entity.Failure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailureRepository extends JpaRepository<Failure, Long> {
}
