package edu.mitin.rest;

import edu.mitin.games.service.model.Response;
import edu.mitin.performance.PerformanceService;
import edu.mitin.storage.TournamentStorage;
import edu.mitin.storage.entity.Game;
import edu.mitin.storage.entity.Tournament;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tournament")
public class TournamentController {

    private final TournamentStorage storage;

    private final PerformanceService performanceService;

    public TournamentController(@Qualifier("tournamentStorageImpl") TournamentStorage storage, PerformanceService performanceService) {
        this.storage = storage;
        this.performanceService = performanceService;
    }

    @PostMapping(value = "/start-tournament", produces = "application/json", consumes = "application/json")
    public Response startTournament(@RequestParam String tournamentId) {
        Long id = Long.valueOf(tournamentId);
        if (storage.isPresentTournament(id)) {
            performanceService.executeTournament(id);
            return new Response(true, "");
        }
        return new Response(false, "Такого турнира нет");
    }
}
