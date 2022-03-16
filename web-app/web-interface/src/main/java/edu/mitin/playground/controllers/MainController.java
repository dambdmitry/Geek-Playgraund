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

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @GetMapping("/test")
    public String testConnection(Model model) throws IOException, InterruptedException {
        String s = SearchWeb();
        model.addAttribute("test", s);
        return "test";
    }

    public static String SearchWeb () throws IOException, InterruptedException {
        HttpClient httpclient = HttpClient.newBuilder().build();
        HttpResponse<String> re = httpclient.send(HttpRequest.newBuilder().uri(URI.create("http://localhost:9000/test/123")).GET().build(), HttpResponse.BodyHandlers.ofString());
        return re.body();


    }
}


