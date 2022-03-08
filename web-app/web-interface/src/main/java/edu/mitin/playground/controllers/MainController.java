package edu.mitin.playground.controllers;

import edu.mitin.playground.tournaments.TournamentService;
import edu.mitin.playground.tournaments.model.Tournament;
import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.model.Player;
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
        //TODO сделать сортировку чтоб выводить только топ

        List<Tournament> tournaments = tournamentService.getAllTournaments();
        List<Player> topPlayes = userService.getAllPlayers();
        model.addAttribute("tournaments", tournaments);
        model.addAttribute("topUsers", topPlayes);
        return "index";
    }
}


