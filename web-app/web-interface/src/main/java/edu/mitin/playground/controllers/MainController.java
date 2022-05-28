package edu.mitin.playground.controllers;

import edu.mitin.playground.inter.tournaments.TournamentService;
import edu.mitin.playground.inter.tournaments.entity.Tournament;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    TournamentService tournamentService;
    UserService userService;

    @Autowired
    public MainController(TournamentService tournamentService, UserService userService) {
        this.tournamentService = tournamentService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        List<Player> topPlayes = tournamentService.getTopPlayers();
        model.addAttribute("tournaments", tournaments);
        model.addAttribute("topUsers", topPlayes);
        return "index";
    }
}


