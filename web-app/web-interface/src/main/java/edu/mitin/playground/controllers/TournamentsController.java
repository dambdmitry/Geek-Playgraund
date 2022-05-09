package edu.mitin.playground.controllers;

import com.google.gson.Gson;
import edu.mitin.playground.communicate.ApiRequests;
import edu.mitin.playground.dto.Response;
import edu.mitin.playground.dto.Solution;
import edu.mitin.playground.games.GameService;
import edu.mitin.playground.games.entity.Game;
import edu.mitin.playground.games.entity.ProgramTemplate;
import edu.mitin.playground.results.entity.Round;
import edu.mitin.playground.results.entity.RoundStep;
import edu.mitin.playground.tournaments.TournamentService;
import edu.mitin.playground.tournaments.entity.Tournament;
import edu.mitin.playground.tournaments.entity.model.SecretKey;
import edu.mitin.playground.tournaments.entity.model.TournamentStatus;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.entity.Permission;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import edu.mitin.playground.results.ResultsStorages;
import edu.mitin.playground.results.model.TournamentTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tournaments")
public class TournamentsController {

    private final String ERROR_ATTRIBUTE_NAME = "errorMsg";

    private final TournamentService tournamentService;
    private final UserService userService;
    private final GameService gameService;
    private final ResultsStorages resultsStorages;
    private final ApiRequests requests;

    @Autowired
    public TournamentsController(TournamentService tournamentService, UserService userService, GameService gameService, ResultsStorages resultsStorages, ApiRequests requests) {
        this.tournamentService = tournamentService;
        this.userService = userService;
        this.gameService = gameService;
        this.resultsStorages = resultsStorages;
        this.requests = requests;
    }

    @GetMapping("")
    public String tournamentPage(Model model, @RequestParam(value = "searchParam", required = false) String searchParam) {
        model.addAttribute("isOrganizer", hasCurrentUserPermission(Permission.ORGANIZER_PROFILE));
        if (searchParam != null) {
            List<Tournament> tournamentsBySearch = tournamentService.getByPartTournamentName(searchParam);
            model.addAttribute("search", tournamentsBySearch.size() > 10 ? tournamentsBySearch.subList(0, 10) : tournamentsBySearch);
        }
        List<Tournament> firstTenTournaments = tournamentService.getAllTournaments();
        model.addAttribute("allTours", firstTenTournaments.size() > 10 ? firstTenTournaments.subList(0, 10) : firstTenTournaments);
        return "tournaments/tournaments";
    }

    @PreAuthorize("hasAuthority('ORGANIZER')")
    @GetMapping("/create")
    public String createTournamentPage(Model model) {
        model.addAttribute("tournament", new Tournament());
        final List<Game> allGames = gameService.getAllGames();
        model.addAttribute("games", allGames);
        return "tournaments/createTournament";
    }

    @PreAuthorize("hasAuthority('ORGANIZER')")
    @PostMapping("/create")
    public String createTournament(Model model, Tournament tournament) {
        if (isTournamentNameExists(tournament.getName())) {
            model.addAttribute(ERROR_ATTRIBUTE_NAME, "Турнир с таким названием уже существует");
            return "tournaments/createTournament";
        }
        final Optional<Game> selectedGame = gameService.getGameByName(tournament.getGame().getName());
        if (selectedGame.isEmpty()) {
            model.addAttribute(ERROR_ATTRIBUTE_NAME, "Игра турнира не определена");
            return "tournaments/createTournament";
        }

        final Game game = selectedGame.get();
        if (hasCurrentUserPermission(Permission.ORGANIZER_PROFILE)) {
            User ownerAccount = getCurrentUser().get();
            tournament.setOwner(ownerAccount);
            tournament.setGame(game);
            tournament.setPlayersCount(0);
            tournament.setStatus(TournamentStatus.OPEN.name());
            if (tournament.getSecretKey().isBlank()) {
                tournament.setSecretKey(null);
            }
            Long tournamentId = tournamentService.registerTournament(tournament);
            return "redirect:/tournaments/tournament/" + tournamentId;
        }
        return "errors/noAccess";
    }

    @GetMapping("tournament/{id}")
    public String showTournament(@PathVariable String id, Model model) {
        if (!isCorrectId(id)) {
            return "errors/notFound";
        }
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            User owner = tournament.getOwner();
            List<Player> playersByTournament = tournamentService.getPlayersByTournament(tournament);
            var tournamentSolutions = tournamentService.getTournamentSolutions(tournament.getId());
            tournamentSolutions.forEach(solution -> {
                solution.setCode(solution.getCode().replaceAll("<", "&lt;"));
                solution.setCode(solution.getCode().replaceAll(">", "&gt;"));
                solution.setCode(solution.getCode().replaceAll("\"", "&quot;"));
                solution.setCode(solution.getCode().replaceAll("'", "&#39;"));
                solution.setCode(solution.getCode().replaceAll(" ", "&nbsp;"));
                solution.setCode(solution.getCode().replaceAll("\n", "<br>"));
            });
            model.addAttribute("gameDescription", tournament.getGame().getDescription().replaceAll("\n", "<br>"));
            model.addAttribute("solutions", tournamentSolutions);
            model.addAttribute("tournament", tournament);
            model.addAttribute("isPlayer", isThePlayerOfTheTournament(tournament));
            model.addAttribute("hasSolution", hasPlayerSolution(tournament));
            model.addAttribute("isOrganizer", hasCurrentUserPermission(Permission.ORGANIZER_PROFILE));
            model.addAttribute("tournamentId", new Tournament());
            model.addAttribute("players", playersByTournament);
            model.addAttribute("owner", owner);
            int solutionsCount = tournamentSolutions.size();
            boolean isReadyTournament =  tournament.getStatus().equals(TournamentStatus.OPEN.name()) && solutionsCount > 1;
            boolean isClosedTournament = tournament.getStatus().equals(TournamentStatus.CLOSED.name());
            boolean isInProcess = tournament.getStatus().equals(TournamentStatus.IN_PROCESS.name());
            model.addAttribute("isReadyTournament", isReadyTournament);
            model.addAttribute("isClosedTournament", isClosedTournament);
            model.addAttribute("isInProcess", isInProcess);
            return "tournaments/Tournament";
        }
        return "errors/notFound";
    }

    @GetMapping("tournament/{id}/solution")
    public String sendPlayerSolutionPage(@PathVariable String id, Model model) {
        if (!isCorrectId(id)) {
            return "errors/notFound";
        }
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            final List<ProgramTemplate> templatesByGame = gameService.getGameProgramTemplates(tournament.getGame().getName()).get();
            final List<String> gameLanguages = templatesByGame.stream().map(ProgramTemplate::getLanguage).collect(Collectors.toList());
            model.addAttribute("rules", tournament.getGame().getRules().replaceAll("\n", "<br>"));
            model.addAttribute("templates", templatesByGame);
            model.addAttribute("languages", gameLanguages);
            model.addAttribute("tournament", tournament);
            model.addAttribute("solution", new Solution("", "", "", "", 0L));
            if (isThePlayerOfTheTournament(tournament) && !hasPlayerSolution(tournament)) {
                return "tournaments/solutions/gameSolution";
            } else {
                return "errors/noAccess";
            }
        }
        return "errors/notFound";
    }

    @PostMapping("/sendSolution")
    public String sendPlayerSolutionPage(Solution solution, Model model) {
        final String jsonSolution = createSolutionJson(solution);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(solution.getTournamentId());
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            if (!isThePlayerOfTheTournament(tournament) || hasPlayerSolution(tournament)) {
                return "errors/noAccess";
            }
        } else {
            return "errors/notFound";
        }
        try {
            final String responseBody = requests.sendPost(ApiRequests.ActionRequestBody.VERIFY, jsonSolution);
            final Response response = new Gson().fromJson(responseBody, Response.class);
            var resultMsg = response.isSuccess() ? "Успешно" : "Программа не прошла проверку";
            model.addAttribute("result", "Результат: " + resultMsg);
            model.addAttribute("failure", !response.isSuccess());
            if (!response.isSuccess()) {
                model.addAttribute("tournament", solution.getTournamentId().toString());
                model.addAttribute("error", response.getDescription());
            }
            System.out.println(response);
            return "tournaments/solutions/solutionResult";
        } catch (IOException | InterruptedException e) {
            return "errors/serverError";
        }
    }

    @GetMapping("tournament/{id}/register")
    public String registerToTournamentPage(@PathVariable String id, Model model) {
        if (!isCorrectId(id)) {
            return "errors/notFound";
        }
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            if (isThePlayerOfTheTournament(tournament) || !tournament.getStatus().equals("OPEN")) {
                return "errors/notFound";
            }
            if (tournament.getSecretKey() != null) {
                User owner = tournament.getOwner();
                List<Player> playersByTournament = tournamentService.getPlayersByTournament(tournament);
                model.addAttribute("players", playersByTournament);
                model.addAttribute("owner", owner);
                model.addAttribute("id", id);
                model.addAttribute("secret", new SecretKey());
                return "tournaments/registerTournament";
            }
            User user = getCurrentUser().get();
            if (tournamentService.registerPlayerToTournament(tournament, user)) {
                return "redirect:/tournaments/tournament/" + tournament.getId().toString();
            }
        }
        return "errors/notFound";
    }

    @PostMapping("tournament/{id}/register")
    public String registerToTournament(@PathVariable String id, SecretKey secretKey, Model model) {
        if (!isCorrectId(id)) {
            return "errors/notFound";
        }
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            if (isThePlayerOfTheTournament(tournament) || !tournament.getStatus().equals("OPEN")) {
                return "errors/notFound";
            }
            if (tournament.getSecretKey().equals(secretKey.getSecretKey())) {
                User user = getCurrentUser().get();
                if (tournamentService.registerPlayerToTournament(tournament, user)) {
                    return "redirect:/tournaments/tournament/" + tournament.getId().toString();
                }
            }
            model.addAttribute(ERROR_ATTRIBUTE_NAME, "Неверный ключ");
            User owner = tournament.getOwner();
            List<Player> playersByTournament = tournamentService.getPlayersByTournament(tournament);
            model.addAttribute("players", playersByTournament);
            model.addAttribute("owner", owner);
            model.addAttribute("id", id);
            model.addAttribute("secret", new SecretKey());
            return "tournaments/registerTournament";
        }
        return "errors/notFound";
    }


    @PreAuthorize("hasAuthority('ORGANIZER')")
    @PostMapping(value = "/start/{id}")
    public String startTournament(@PathVariable String id, Model model) {
        if (!isCorrectId(id)) {
            return "errors/notFound";
        }
        long idTour = Long.parseLong(id);
        if (!tournamentService.getTournamentById(idTour).get().getOwner().getId().equals(getCurrentUser().get().getId())) {
            return "errors/noAccess";
        }
        try {
            String responseBody = requests.sendPostRequestParams(ApiRequests.ActionRequestParams.START_TOURNAMENT, List.of(id));
            final Response response = new Gson().fromJson(responseBody, Response.class);
            if (response.isSuccess()) {
                tournamentService.setTournamentStatus(idTour, TournamentStatus.IN_PROCESS);
                return "redirect:/tournaments/tournament/" + id + "/results";
            } else {
                model.addAttribute(ERROR_ATTRIBUTE_NAME, response.getDescription());
                return "errors/error";
            }
        } catch (IOException | InterruptedException e) {
            return "errors/serverError";
        }
    }

    @GetMapping("/tournament/{id}/results")
    public String getTournamentResults(@PathVariable String id, Model model) {
        if (!isCorrectId(id)) {
            return "errors/notFound";
        }
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isEmpty()) {
            return "errors/notFound";
        }
        Tournament tournament = tournamentById.get();
        model.addAttribute("tournament", tournament);
        if (resultsStorages.hasTournamentFailedRounds(tournament.getId())) {
            model.addAttribute(ERROR_ATTRIBUTE_NAME, "Турнир завершен с ошибкой, посмотреть результаты можно в отчете");
            return "tournaments/results";
        }
        model.addAttribute("status", TournamentStatus.valueOf(tournament.getStatus()).getStatus());
        List<TournamentTableRow> tournamentTable = resultsStorages.createSortedTournamentTable(idTour);
        model.addAttribute("table", tournamentTable);
        return "tournaments/results";
    }

    @GetMapping("/tournament/{id}/report")
    public String getTournamentReport(@PathVariable String id, Model model) {
        if (!isCorrectId(id)) {
            return "errors/notFound";
        }
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isEmpty()) {
            return "errors/notFound";
        }

        Tournament tournament = tournamentById.get();
        model.addAttribute("tournament", tournament);
        if (resultsStorages.hasTournamentFailedRounds(tournament.getId())) {
            model.addAttribute("result", "Турнир завершен с ошибкой");
            List<Round> failedRounds = resultsStorages.getFailedRounds(tournament.getId());
            model.addAttribute("failedRound", failedRounds);
        } else {
            model.addAttribute("result", "Турнир завершен успешно");
        }
        return "tournaments/report";
    }

    @GetMapping("/tournament/{id}/{playerName}/result")
    public String getPlayerResult(@PathVariable Long id,
                                  @PathVariable String playerName,
                                  Model model) {
        model.addAttribute("username", playerName);
        List<Round> rounds = resultsStorages.createUserRounds(id, playerName);
        model.addAttribute("rounds", rounds);
        return "tournaments/playerResult";
    }

    @GetMapping("/tournament/round/{id}")
    public String getPlayerResult(@PathVariable Long id,
                                  Model model) {

        Round round = resultsStorages.getRound(id);
        if (round == null) {
            return "errors/notFound";
        }
        List<RoundStep> roundSteps = resultsStorages.getSortedRoundSteps(id, round.getHostName(), round.getGuestName());
        List<String> hostGoal = List.of(round.getHostGoal().split("\n"));
        List<String> guestGoal = List.of(round.getHostGoal().split("\n"));
        model.addAttribute("round", round);
        model.addAttribute("roundName", round.getHostName() + " vs " + round.getGuestName());
        model.addAttribute("hostGoal", hostGoal);
        model.addAttribute("guestGoal", guestGoal);
        String winner = round.getWinner() == null ? "Ничья" : "Победитель: " + round.getWinner();
        model.addAttribute("winner", winner);
        model.addAttribute("steps", roundSteps);
        return "tournaments/round";
    }

    private String createSolutionJson(Solution solution) {
        solution.setPlayerName(getCurrentUser().get().getUsername());
        return new Gson().toJson(solution);
    }

    private boolean isTournamentNameExists(String name) {
        return tournamentService.findTournamentByName(name).isPresent();
    }

    private boolean isCorrectId(String id) {
        return id.matches("^[0-9]+$");
    }

    private boolean hasCurrentUserPermission(Permission permission) {
        final Optional<User> currentUser = getCurrentUser();
        if (currentUser.isPresent()) {
            return currentUser.get().getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(permission.getPermission()));
        }
        return false;
    }

    private Optional<User> getCurrentUser() {
        return userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private boolean isThePlayerOfTheTournament(Tournament tournament) {
        Optional<User> currentUser = getCurrentUser();
        return currentUser.filter(user -> tournamentService.getPlayersByTournament(tournament)
                .stream()
                .anyMatch(player -> player.getAccount().getId().equals(user.getId()))).isPresent();
    }

    private boolean hasPlayerSolution(Tournament tournament) {
        User currentUser = getCurrentUser().get();
        return tournamentService.getTournamentSolutions(tournament.getId()).stream().anyMatch(solution -> solution.getPlayerName().equals(currentUser.getUsername()));
    }
}
