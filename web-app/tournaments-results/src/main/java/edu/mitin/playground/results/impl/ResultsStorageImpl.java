package edu.mitin.playground.results.impl;

import edu.mitin.playground.results.entity.RoundStep;
import edu.mitin.playground.tournaments.TournamentService;
import edu.mitin.playground.tournaments.entity.Tournament;
import edu.mitin.playground.users.entity.User;
import edu.mitin.playground.results.ResultsStorages;
import edu.mitin.playground.results.entity.Round;
import edu.mitin.playground.results.model.TournamentTableRow;
import edu.mitin.playground.results.repository.RoundRepository;
import edu.mitin.playground.results.repository.RoundStepRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResultsStorageImpl implements ResultsStorages {

    private final RoundRepository roundRepository;
    private final RoundStepRepository roundStepRepository;
    private final TournamentService tournamentService;

    public ResultsStorageImpl(RoundRepository roundRepository, RoundStepRepository roundStepRepository, TournamentService tournamentService) {
        this.roundRepository = roundRepository;
        this.roundStepRepository = roundStepRepository;
        this.tournamentService = tournamentService;
    }

    @Override
    public List<TournamentTableRow> createSortedTournamentTable(Long idTournament) {
        List<User> playersAccounts = tournamentService.getAccountsByTournament(idTournament);
        List<TournamentTableRow> table = initTournamentTable(playersAccounts, idTournament);

        List<TournamentTableRow> sortedTable = sortTableByPoints(table);
        setTablePositions(sortedTable);
        return sortedTable;
    }

    @Override
    public List<Round> createUserRounds(Long tournamentId, String username) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId).get();
        List<Round> guestsRounds = roundRepository.findAllByGuestNameAndTournament(username, tournament);
        List<Round> hostsRounds = roundRepository.findAllByHostNameAndTournament(username, tournament);
        List<Round> rounds = new LinkedList<>();
        for (Round hostRound : hostsRounds) {
            rounds.add(hostRound);
            rounds.add(guestsRounds.stream().filter(r -> r.getHostName().equals(hostRound.getGuestName())).findFirst().get());
        }
        return rounds;
    }

    @Override
    public Round getRound(Long roundId) {
        return roundRepository.getById(roundId);
    }

    @Override
    public Boolean hasTournamentFailedRounds(Long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId).get();
        List<Round> failedRounds = roundRepository.findAllByTournamentAndFailureIsNotNull(tournament);
        return !failedRounds.isEmpty();
    }

    @Override
    public List<Round> getFailedRounds(Long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId).get();
        return roundRepository.findAllByTournamentAndFailureIsNotNull(tournament);
    }

    @Override
    public List<RoundStep> getSortedRoundSteps(Long roundId, String hostPlayerName, String guestPlayerName) {
        Round round = roundRepository.getById(roundId);
        List<RoundStep> result = new LinkedList<>();
        List<RoundStep> roundSteps = roundStepRepository.findAllByRound(round);
        List<RoundStep> hostPlayerSteps = roundSteps.stream()
                .filter(r -> r.getPlayerName().equals(hostPlayerName))
                .sorted(Comparator.comparingLong(RoundStep::getStepNumber))
                .collect(Collectors.toList());
        List<RoundStep> guestPlayerSteps = roundSteps.stream()
                .filter(r -> r.getPlayerName().equals(guestPlayerName))
                .sorted(Comparator.comparingLong(RoundStep::getStepNumber))
                .collect(Collectors.toList());
        for (int i = 0; i < guestPlayerSteps.size(); i++) {
            result.add(hostPlayerSteps.get(i));
            result.add(guestPlayerSteps.get(i));
        }
        if (hostPlayerSteps.size() > guestPlayerSteps.size()) {
            result.add(hostPlayerSteps.get(hostPlayerSteps.size()-1));
        }
        return result;
    }

    @Override
    public boolean isAllGamesPlayed(Long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId).get();
        List<Round> rounds = roundRepository.findAllByTournament(tournament);
        List<String> winners = rounds.stream().map(Round::getWinner).filter(Objects::nonNull).collect(Collectors.toList());
        Integer playersCount = tournament.getPlayersCount();
        return (playersCount*(playersCount-1)) == winners.size();
    }

    private void setTablePositions(List<TournamentTableRow> sortedTable) {
        for (int i = 0; i < sortedTable.size(); i++) {
            sortedTable.get(i).setPosition(i+1);
        }
    }

    private List<TournamentTableRow> sortTableByPoints(List<TournamentTableRow> table) {
        return table.stream().sorted(Comparator.comparingInt(TournamentTableRow::getPoints).reversed()).collect(Collectors.toList());
    }

    private List<TournamentTableRow> initTournamentTable(List<User> users, Long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId).get();
        List<TournamentTableRow> table = new LinkedList<>();
        for (User user : users) {
            String username = user.getUsername();
            List<Round> games = roundRepository.findAllByGuestNameAndTournament(username, tournament);
            games.addAll(roundRepository.findAllByHostNameAndTournament(username, tournament));
            table.add(createTableRow(username, games));
        }
        return table;
    }

    private TournamentTableRow createTableRow(String username, List<Round> rounds) {
        TournamentTableRow tournamentTableRow = new TournamentTableRow();
        int winsCount = (int) rounds.stream().filter(round -> round.getWinner().equals(username)).count();
        tournamentTableRow.setUsername(username);
        tournamentTableRow.setWinsCount(winsCount);
        tournamentTableRow.setLosesCount(rounds.size() - winsCount);
        tournamentTableRow.setPoints(winsCount); // победа - 1 очко
        return tournamentTableRow;
    }
}
