package edu.mitin.playground.results.impl;

import edu.mitin.playground.inter.tournaments.TournamentService;
import edu.mitin.playground.inter.tournaments.entity.Tournament;
import edu.mitin.playground.results.entity.RoundStep;
import edu.mitin.playground.results.model.TournamentTableRow;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import edu.mitin.playground.results.ResultsStorages;
import edu.mitin.playground.results.entity.Round;
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
            result.add(hostPlayerSteps.get(hostPlayerSteps.size() - 1));
        }
        return result;
    }

    @Override
    public void clearResults(Long tournamentId) {
        final Tournament tournament = tournamentService.getTournamentById(tournamentId).get();
        final List<Round> tournamenRounds = roundRepository.findAllByTournament(tournament);
        for (Round round : tournamenRounds) {
            final List<RoundStep> roundSteps = roundStepRepository.findAllByRound(round);
            for (RoundStep step : roundSteps) {
                roundStepRepository.deleteById(step.getId());
            }
            roundRepository.deleteById(round.getId());
        }
    }

    private void setTablePositions(List<TournamentTableRow> sortedTable) {
        for (int i = 0; i < sortedTable.size(); i++) {
            sortedTable.get(i).setPosition(i + 1);
        }
    }

    private List<TournamentTableRow> sortTableByPoints(List<TournamentTableRow> table) {
        return table
                .stream()
                .sorted(Comparator.comparingInt(TournamentTableRow::getPoints).reversed())
                .collect(Collectors.toList());
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
        final int drawsCount = (int) rounds.stream().filter(round -> {
            return (round.getGuestName().equals(username)
                    || round.getHostName().equals(username))
                    && round.getWinner() == null;
        }).count();
        int winsCount = (int) rounds
                .stream()
                .filter(round -> round.getWinner() != null && round.getWinner().equals(username))
                .count();
        tournamentTableRow.setUsername(username);
        tournamentTableRow.setWinsCount(winsCount);
        tournamentTableRow.setDrawCount(drawsCount);
        tournamentTableRow.setLosesCount(rounds.size() - winsCount - drawsCount);
        final int points = winsCount * 3 + drawsCount;
        tournamentTableRow.setPoints(points); // победа - 3 очка, ничья 1 очко
        Player playerByUserName = tournamentService.getPlayerByUserName(username);
        if (!rounds.isEmpty()) {
            Tournament tournament = rounds.get(0).getTournament();
            tournamentService.savePlayerTournamentPoints(playerByUserName.getId(), tournament.getId(), points);
        }
        return tournamentTableRow;
    }
}
