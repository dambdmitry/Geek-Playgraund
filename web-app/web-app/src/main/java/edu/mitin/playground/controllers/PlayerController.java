package edu.mitin.playground.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/player")
public class PlayerController {

    @GetMapping("")
    public String playerPage() {
        return "playerController";
    }
}
