package edu.mitin.playground.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("tournaments", Tournaments.mockTours());
        return "index";
    }
}

class Tournaments{
    private Long id;
    private String name;
    private Integer playerCount;

    public static List<Tournaments> mockTours() {
        return List.of(new Tournaments(1L, "Example 1", 22),
                new Tournaments(2L, "Example 2", 212),
                new Tournaments(3L, "Example 3", 322),
                new Tournaments(4L, "Example 4", 242),
                new Tournaments(5L, "Example 5", 12),
                new Tournaments(6L, "Example 6", 26),
                new Tournaments(7L, "Example 7", 1));
    }

    public Tournaments(Long id, String name, Integer playerCount) {
        this.id = id;
        this.name = name;
        this.playerCount = playerCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }
}
