package edu.mitin.performance;

import org.springframework.stereotype.Service;

@Service
public interface PerformanceService {
    void executeTournament(Long tournamentId);
}
