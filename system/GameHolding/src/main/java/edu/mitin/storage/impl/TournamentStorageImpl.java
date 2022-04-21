package edu.mitin.storage.impl;

import edu.mitin.storage.TournamentStorage;
import edu.mitin.storage.entity.*;
import edu.mitin.storage.repository.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentStorageImpl implements TournamentStorage {

    private final SolutionRepository solutionRepository;
    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;
    private final RoundRepository roundRepository;
    private final RoundStepRepository roundStepRepository;
    private final FailureRepository failureRepository;

    public TournamentStorageImpl(SolutionRepository solutionRepository,
                                 TournamentRepository tournamentRepository,
                                 GameRepository gameRepository,
                                 RoundRepository roundRepository,
                                 RoundStepRepository roundStepRepository, FailureRepository failureRepository) {
        this.solutionRepository = solutionRepository;
        this.tournamentRepository = tournamentRepository;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
        this.roundStepRepository = roundStepRepository;
        this.failureRepository = failureRepository;
    }

    @Override
    public boolean saveSolution(Long tournamentId, String playerName, String code, String language) {
        Solution newSolution = new Solution();
        final Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) return false;
        newSolution.setTournament(tournament.get());

        final Optional<Solution> oldPlayerSolution = solutionRepository.findByPlayerNameAndTournament(playerName, tournament.get());
        oldPlayerSolution.ifPresent(solutionRepository::delete);

        newSolution.setPlayerName(playerName);
        newSolution.setCode(code);
        newSolution.setLanguage(language);
        solutionRepository.save(newSolution);
        return true;
    }

    @Override
    public boolean saveTournament(Long tournamentId, Long gameId) {
        final Optional<Game> tournamentGame = gameRepository.findById(gameId);
        if (tournamentRepository.findById(tournamentId).isPresent() || tournamentGame.isEmpty()) {
            return false;
        }
        final Tournament newTournament = new Tournament();
        newTournament.setId(tournamentId);
        newTournament.setGame(tournamentGame.get());
        tournamentRepository.save(newTournament);
        return true;
    }

    @Override
    public Optional<Game> getTournamentGame(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tournament.get().getGame());
    }

    @Override
    public List<Solution> getSolutionsByTournament(Long tournamentId) {
        final Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) {
            return Collections.emptyList();
        }
        return solutionRepository.findAllByTournament(tournament.get());
    }

    @Override
    public Long saveRound(Long tournamentId, String hostPlayerName, String guestPlayerName, String winnerName) {
        Tournament tournament = tournamentRepository.findById(tournamentId).get();
        Round round = new Round();
        round.setTournament(tournament);
        round.setHostName(hostPlayerName);
        round.setGuestName(guestPlayerName);
        round.setWinner(winnerName);
        Round save = roundRepository.save(round);
        return save.getId();
    }

    @Override
    public Long saveFailedRound(Long tournamentId, String leftPlayerName, String rightPlayerName, String author, String description) {
        Failure failure = new Failure(author, description);
        Failure savedFailure = failureRepository.save(failure);
        Round round = new Round();
        round.setFailure(savedFailure);
        round.setGuestName(leftPlayerName);
        round.setHostName(rightPlayerName);
        Round save = roundRepository.save(round);
        return save.getId();
    }

    @Override
    public void saveRoundStep(Long roundId, String playerName, String step, Long stepNumber) {
        Round round = roundRepository.findById(roundId).get();
        RoundStep roundStep = new RoundStep();
        roundStep.setRound(round);
        roundStep.setPlayerName(playerName);
        roundStep.setAction(step);
        roundStep.setStepNumber(stepNumber);
        roundStepRepository.save(roundStep);
    }

    @Override
    public Boolean isPresentTournament(Long tournamentId) {
        return tournamentRepository.findById(tournamentId).isPresent();
    }

}
