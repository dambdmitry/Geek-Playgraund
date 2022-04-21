package edu.mitin.playground.controllers;

import edu.mitin.playground.tournaments.TournamentService;
import edu.mitin.playground.tournaments.entity.Tournament;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.entity.Permission;
import edu.mitin.playground.users.entity.Player;
import edu.mitin.playground.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TournamentService tournamentService;

    @GetMapping("")
    public String userPage(Model model) {
        Optional<User> currentUser = getCurrentUser();
        if (currentUser.isEmpty()) {
            return "errors/noAccess";
        }
        User userAccount = currentUser.get();
        model.addAttribute("user", userAccount);
        if (hasUserPermission(userAccount, Permission.ORGANIZER_PROFILE.getPermission())){
            List<Tournament> userOrganizedTournaments = tournamentService.getTournamentsByOwner(userAccount.getUsername());
            model.addAttribute("organizedTournaments", userOrganizedTournaments);
        }
        if (hasUserPermission(userAccount, Permission.PLAYER_PROFILE.getPermission())) {
            Optional<Player> playerByUserAccount = userService.getPlayerByUserAccount(userAccount);
            if (playerByUserAccount.isPresent()) {
                Player player = playerByUserAccount.get();
                List<Tournament> tournamentsByPlayer = tournamentService.getTournamentsByPlayer(player);
                model.addAttribute("playerPoints", player.getPoints());
                model.addAttribute("playerTournaments", tournamentsByPlayer);
            }
        }
        return "user/userController";
    }

    private Optional<User> getCurrentUser() {
        return userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    boolean hasUserPermission(User user, String permission) {
        return user.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals(permission));
    }
}
