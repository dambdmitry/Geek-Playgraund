package edu.mitin.playground.tournaments.impl;

import edu.mitin.playground.tournaments.TournamentService;
import edu.mitin.playground.tournaments.entity.PlayersTournamentsRelation;
import edu.mitin.playground.tournaments.entity.Solution;
import edu.mitin.playground.tournaments.entity.Tournament;
import edu.mitin.playground.games.repository.GameRepository;
import edu.mitin.playground.games.repository.ProgramTemplateRepository;
import edu.mitin.playground.tournaments.entity.model.TournamentStatus;
import edu.mitin.playground.tournaments.repository.SolutionRepository;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import edu.mitin.playground.tournaments.repository.PlayersTournamentsRelationRepository;
import edu.mitin.playground.tournaments.repository.TournamentRepository;
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

    private GameRepository gameRepository;
    private ProgramTemplateRepository templateRepository;
    private final PlayerRepository playerRepository;
    private final SolutionRepository solutionRepository;


    @Autowired
    public TournamentServiceImpl(TournamentRepository tournamentRepository,
                                 PlayersTournamentsRelationRepository ptRelationRepository,
                                 UserService userService,
                                 GameRepository gameRepository,
                                 ProgramTemplateRepository templateRepository, PlayerRepository playerRepository, SolutionRepository solutionRepository) {
        this.tournamentRepository = tournamentRepository;
        this.ptRelationRepository = ptRelationRepository;
        this.userService = userService;
        this.gameRepository = gameRepository;
        this.templateRepository = templateRepository;
//        registryMockGames();
//        registryMockTemplates();
//        registerMockTournaments();
        this.playerRepository = playerRepository;
        this.solutionRepository = solutionRepository;
    }


//    private void registerMockTournaments() {
//        Optional<User> dmitry = userService.getUserByUsername("Dmitry");
//        List<Tournament> tours = List.of(new Tournament(dmitry.get(), "Example1", 12),
//                new Tournament(dmitry.get(), "Example2", 12),
//                new Tournament(dmitry.get(), "HenkaleyHochu", 1),
//                new Tournament(dmitry.get(), "Example4", 22),
//                new Tournament(dmitry.get(), "Putin", 111),
//                new Tournament(dmitry.get(), "Example6", 4),
//                new Tournament(dmitry.get(), "OurUkraine", 9),
//                new Tournament(dmitry.get(), "Example8", 0),
//                new Tournament(dmitry.get(), "fuck", 12),
//                new Tournament(dmitry.get(), "Example10", 222));
//        tournamentRepository.saveAll(tours);
//    }

//    private void registryMockGames() {
//        List<Game> games = List.of(new Game("RANDOMIZE", "test", "test"), new Game("TEST", "test", "test"));
//        gameRepository.saveAll(games);
//    }
//    private void registryMockTemplates() {
//        final Game randomize = gameRepository.findGameByName("RANDOMIZE");
//        List<ProgramTemplate> templates = List.of(new ProgramTemplate(randomize, "PYTHON", "firstStep = \"\"\n" +
//                "print(firstStep)#Первый ход\n" +
//                "\n" +
//                "#Цикл игры\n" +
//                "while True:\n" +
//                "    answer = int(input()) #Получение ответа от системы\n" +
//                "    \n" +
//                "    #Логика игры:\n" +
//                "    \n" +
//                "    #Ваш ход\n" +
//                "    response = \"\"\n" +
//                "    print(response) #Отправка хода"),
//                new ProgramTemplate(randomize, "JAVA", "import java.util.Scanner;\n" +
//                        "\n" +
//                        "public class Main{\n" +
//                        "    static Scanner scanner = new Scanner(System.in);\n" +
//                        "    public static void main(String[] args) {\n" +
//                        "        String firstAction = \"\";\n" +
//                        "        System.out.println(firstAction); //Первый ход\n" +
//                        "        System.out.flush();\n" +
//                        "        //Цикл игры\n" +
//                        "        while (true) {\n" +
//                        "            int answer = Integer.parseInt(scanner.nextLine()); //Получение ответа от системы\n" +
//                        "\n" +
//                        "            //Логика игры:\n" +
//                        "\n" +
//                        "            //Ваш ход\n" +
//                        "            String response = \"\";\n" +
//                        "            System.out.println(response); //Отправка хода\n" +
//                        "            System.out.flush();\n" +
//                        "        }\n" +
//                        "    }\n" +
//                        "}"));
//        templateRepository.saveAll(templates);
//    }


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
        return userService.getPlayerByUser(user);
    }

    @Override
    public Iterable<Tournament> getByTournamentName(String tournamentName) {
        return tournamentRepository.findTournamentsByName(tournamentName);
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
    public void savePlayerTournamentPoints(Long playerId, Long tournamentId, int winsCount) {
        Tournament tournament = tournamentRepository.getById(tournamentId);
        Player player = playerRepository.getById(playerId);
        PlayersTournamentsRelation ptRelation = ptRelationRepository.findByTournamentAndPlayer(tournament, player);
        ptRelation.setPoints(winsCount);
        ptRelationRepository.save(ptRelation);
    }

}
