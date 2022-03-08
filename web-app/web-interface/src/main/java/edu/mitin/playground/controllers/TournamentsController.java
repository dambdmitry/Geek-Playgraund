package edu.mitin.playground.controllers;

import edu.mitin.playground.tournaments.TournamentService;
import edu.mitin.playground.tournaments.model.Tournament;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.model.Permission;
import edu.mitin.playground.users.model.Player;
import edu.mitin.playground.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tournaments")
public class TournamentsController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String tournamentPage(Model model, @RequestParam(value = "search1", required = false) String search1){
        model.addAttribute("isOrganizer", isOrganizer());
        if (search1 != null) {
            model.addAttribute("search", tournamentService.getByPartTournamentName(search1));
        }
        model.addAttribute("allTours", tournamentService.getAllTournaments());
        return "tournaments/tournamentsController";
    }

    @PreAuthorize("hasAuthority('ORGANIZER')")
    @GetMapping("/create")
    public String createTournamentPage(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "tournaments/createTournament";
    }

    @PreAuthorize("hasAuthority('ORGANIZER')")
    @PostMapping("/create")
    public String createTournament(Model model, Tournament tournament) {
        if (tournamentService.findTournamentByName(tournament.getName()).isPresent()) {
            model.addAttribute("errorMsg", "Турнир с таким названием уже существует");
            return "tournaments/createTournament";
        }
        Optional<User> owner = getCurrentUser();
        if (owner.isPresent()) {
            User ownerAccount = owner.get();
            if (ownerAccount.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(Permission.ORGANIZER_PROFILE.getPermission()))) {
                tournament.setOwner(ownerAccount);
                tournament.setPlayersCount(0);
                tournamentService.registerTournament(tournament);

                Long tourId = tournamentService.findTournamentByName(tournament.getName()).get().getId();
                return "redirect:/tournaments/tournament/"+tourId;
            }
            return "errors/noAccess";
        }

        model.addAttribute("errorMsg", "Ты кто?");
        return "tournaments/createTournament";

    }

    @GetMapping("tournament/{id}")
    public String showTournament(@PathVariable String id, Model model) {
        long idTour = Long.parseLong(id);

        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            User owner = tournament.getOwner();
            List<Player> playersByTournament = tournamentService.getPlayersByTournament(tournament);
            model.addAttribute("tournament", tournament);
            model.addAttribute("isPlayer", isThePlayerOfTheTournament(tournament));
            model.addAttribute("players", playersByTournament);
            model.addAttribute("owner", owner);
            return "tournaments/Tournament";
        } else {
            return "tournaments/tourNotFound";
        }
    }

    @GetMapping("tournament/{id}/solution")
    public String sendPlayerSolutionPage(@PathVariable String id, Model model){
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            if (isThePlayerOfTheTournament(tournament)) {
                return "tournaments/gameSolution";
            } else {
                return "errors/noAccess";
            }
        }else {
            return  "errors/notFound";
        }
    }

//    @GetMapping("tournament/{id}/register")
//    public String registerToTournamentPage(@PathVariable String id, Model model) {
//        long idTour = Long.parseLong(id);
//        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
//        if (tournamentById.isPresent()) {
//            Tournament tournament = tournamentById.get();
//            if (isThePlayerOfTheTournament(tournament)){
//                return "errors/notFound";
//            }
//            model.addAttribute("tournament", tournament);
//            return "tournaments/tournamentRegistration";
//        }
//        return "errors/notFound";
//    }

    @PostMapping("tournament/{id}/register")
    public String registerToTournament(@PathVariable String id, Model model) {
        long idTour = Long.parseLong(id);
        Optional<Tournament> tournamentById = tournamentService.getTournamentById(idTour);
        if (tournamentById.isPresent()) {
            Tournament tournament = tournamentById.get();
            if (isThePlayerOfTheTournament(tournament)){
                return "errors/notFound";
            }
            User user = getCurrentUser().get();
            if (tournamentService.registerPlayerToTournament(tournament, user)){
                return "redirect:/tournaments/tournament/" + tournament.getId().toString();
            }
        }
        return "errors/notFound";
    }

    private boolean isOrganizer() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).anyMatch(auth -> auth.equals("ORGANIZER"));
    }

    private Optional<User> getCurrentUser() {
        return userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private boolean isThePlayerOfTheTournament(Tournament tournament) {
        Optional<User> currentUser = getCurrentUser();
        return currentUser.filter(user -> tournamentService.getPlayersByTournament(tournament)
                .stream()
                .anyMatch(player -> player.getUserAccount().getId().equals(user.getId()))).isPresent();
    }
}
