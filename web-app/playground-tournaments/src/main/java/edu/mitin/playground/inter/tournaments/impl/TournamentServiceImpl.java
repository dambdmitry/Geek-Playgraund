package edu.mitin.playground.inter.tournaments.impl;

import edu.mitin.playground.inter.tournaments.TournamentService;
import edu.mitin.playground.inter.tournaments.entity.PlayersTournamentsRelation;
import edu.mitin.playground.inter.tournaments.entity.Solution;
import edu.mitin.playground.inter.tournaments.entity.Tournament;
import edu.mitin.playground.inter.tournaments.entity.model.TournamentStatus;
import edu.mitin.playground.inter.tournaments.repository.PlayersTournamentsRelationRepository;
import edu.mitin.playground.inter.tournaments.repository.SolutionRepository;
import edu.mitin.playground.inter.tournaments.repository.TournamentRepository;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import edu.mitin.playground.users.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private TournamentRepository tournamentRepository;
    private PlayersTournamentsRelationRepository ptRelationRepository;
    private UserService userService;
    private final PlayerRepository playerRepository;
    private final SolutionRepository solutionRepository;

    @Autowired
    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 PlayersTournamentsRelationRepository ptRelationRepository,
                                 UserService userService,
                                 PlayerRepository playerRepository,
                                 SolutionRepository solutionRepository) {
        this.tournamentRepository = tournamentRepository;
        this.ptRelationRepository = ptRelationRepository;
        this.userService = userService;

        this.playerRepository = playerRepository;
        this.solutionRepository = solutionRepository;
    }

    @Override
    public Long registerTournament(Tournament tournament) {
        Optional<Tournament> tourFromDB = tournamentRepository.findTournamentByName(tournament.getName());
        if (tourFromDB.isEmpty()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> owner = userService.getUserByUsername(auth.getName());
            owner.ifPresent(tournament::setOwner);
            final Tournament savedTournament = tournamentRepository.save(tournament);
            return savedTournament.getId();
        }
        return -1L;
    }

    @Override
    public void setTournamentStatus(Long tournamentId, TournamentStatus status) {
        Tournament tournament = tournamentRepository.findById(tournamentId).get();
        tournament.setStatus(status.name());
        tournamentRepository.save(tournament);
    }

    @Override
    public boolean registerPlayerToTournament(Tournament tournament, User user) {
        userService.registerPlayer(user);
        Optional<Player> playerByUserAccount = userService.getPlayerByUserAccount(user);
        if (playerByUserAccount.isEmpty()) {
            return false;
        }
        Player player = playerByUserAccount.get();
        PlayersTournamentsRelation relation = new PlayersTournamentsRelation();
        relation.setPlayer(player);
        relation.setTournament(tournament);
        relation.setPoints(0);
        tournament.setPlayersCount(tournament.getPlayersCount() + 1);
        ptRelationRepository.save(relation);
        tournamentRepository.save(tournament);
        return true;
    }

    @Override
    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    @Override
    public Optional<Tournament> findTournamentByName(String tournamentName) {
        return tournamentRepository.findTournamentByName(tournamentName);
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
        List<Player> tournamentPlayers = new LinkedList<>();
        for (PlayersTournamentsRelation ptRelation : relationsByTour) {
            Player player = ptRelation.getPlayer();
            player.setPoints(ptRelation.getPoints());
            tournamentPlayers.add(player);
        }
        return tournamentPlayers.stream().sorted(Comparator.comparingInt(Player::getPoints).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Player> getTopPlayers() {
        List<Player> allPlayers = userService.getAllPlayers();
        for (Player player : allPlayers) {
            List<PlayersTournamentsRelation> ptRelations = ptRelationRepository.findPlayersTournamentsRelationsByPlayer(player);
            int points = 0;
            for (PlayersTournamentsRelation relation : ptRelations) {
                points += relation.getPoints();
            }
            player.setPoints(points);
        }
        return allPlayers.stream().sorted(Comparator.comparingInt(Player::getPoints).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Solution> getTournamentSolutions(Long tournamentId) {
        return solutionRepository.findAllByTournament(tournamentRepository.getById(tournamentId));
    }

    @Override
    public void clearPlayersPoints(Long tournamentId) {
        final Tournament tournament = tournamentRepository.getById(tournamentId);
        final List<PlayersTournamentsRelation> ptRelations = ptRelationRepository.findPlayersTournamentsRelationsByTournament(tournament);
        for (PlayersTournamentsRelation relation : ptRelations) {
            ptRelationRepository.setPlayerPoints(0, relation.getId());
        }
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
    public Player getPlayerByUserName(String playerName) {
        User user = userService.getUserByUsername(playerName).get();
        return userService.getPlayerByUserAccount(user).get();
    }

    @Override
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll(Sort.by("id"));
    }

    @Override
    public List<Tournament> getByPartTournamentName(String partName) {
        return tournamentRepository.findTournamentsByNameContaining(partName);
    }

    @Override
    public List<User> getAccountsByTournament(Long tournamentId) {
        List<Player> playersByTournament = getPlayersByTournament(tournamentRepository.getById(tournamentId));
        return playersByTournament.stream().map(Player::getAccount).collect(Collectors.toList());
    }

    @Override
    public void savePlayerTournamentPoints(Long playerId, Long tournamentId, int points) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        Player player = playerRepository.getById(playerId);
        PlayersTournamentsRelation ptRelation = ptRelationRepository.findByTournamentAndPlayer(tournament, player);
        ptRelation.setPoints(points);
        ptRelationRepository.save(ptRelation);
    }
}
