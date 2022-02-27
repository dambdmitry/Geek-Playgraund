package edu.mitin.playground.tournaments.impl;

import edu.mitin.playground.tournaments.TournamentService;
import edu.mitin.playground.tournaments.model.PlayersTournamentsRelation;
import edu.mitin.playground.tournaments.model.Tournament;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.model.Player;
import edu.mitin.playground.users.model.User;
import edu.mitin.playground.tournaments.repository.PlayersTournamentsRelationRepository;
import edu.mitin.playground.tournaments.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private TournamentRepository tournamentRepository;
    private PlayersTournamentsRelationRepository ptRelationRepository;
    private UserService userService;

    @Autowired
    public TournamentServiceImpl(TournamentRepository tournamentRepository, PlayersTournamentsRelationRepository ptRelationRepository, UserService userService) {
        this.tournamentRepository = tournamentRepository;
        this.ptRelationRepository = ptRelationRepository;
        this.userService = userService;
        registerMockTournaments();
    }


    private void registerMockTournaments() {
        Optional<User> dmitry = userService.getUserByUsername("Dmitry");
        List<Tournament> tours = List.of(new Tournament(dmitry.get(), "Example1", 12),
                new Tournament(dmitry.get(), "Example2", 12),
                new Tournament(dmitry.get(), "Example3", 1),
                new Tournament(dmitry.get(), "Example4", 22),
                new Tournament(dmitry.get(), "Example5", 111),
                new Tournament(dmitry.get(), "Example6", 4),
                new Tournament(dmitry.get(), "Example7", 9),
                new Tournament(dmitry.get(), "Example8", 0),
                new Tournament(dmitry.get(), "Example9", 12),
                new Tournament(dmitry.get(), "Example10", 222));
        tournamentRepository.saveAll(tours);
    }

    @Override
    public boolean registerTournament(Tournament tournament) {
        Optional<Tournament> tourFromDB = tournamentRepository.findTournamentByName(tournament.getName());
        if (tourFromDB.isEmpty()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> owner = userService.getUserByUsername(auth.getName());
            owner.ifPresent(tournament::setOwner);
            tournamentRepository.save(tournament);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    @Override
    public List<Tournament> getTournamentsByOwner(String ownerUsername) {
        Optional<User> owner = userService.getUserByUsername(ownerUsername);
        if (owner.isPresent()){
            return tournamentRepository.findTournamentsByOwner(owner.get());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Player> getPlayersByTournament(Tournament tournament) {
        List<PlayersTournamentsRelation> relationsByTour = ptRelationRepository.findPlayersTournamentsRelationsByTournament(tournament);
        return relationsByTour
                .stream()
                .map(PlayersTournamentsRelation::getPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tournament> getTournamentsByPlayer(Player player) {
        List<PlayersTournamentsRelation> relationsByPlayer = ptRelationRepository.findPlayersTournamentsRelationsByPlayer(player);
        return relationsByPlayer
                .stream()
                .map(PlayersTournamentsRelation::getTournament)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll(Sort.by("id"));
    }
}
