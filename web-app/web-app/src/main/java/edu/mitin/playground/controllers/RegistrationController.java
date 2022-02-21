package edu.mitin.playground.controllers;

import edu.mitin.playground.UserService;
import edu.mitin.playground.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "auth/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String doRegistration(@ModelAttribute User user, Model model) {
        Optional<String> errorByUsername = validateUsername(user.getUsername());
        if (errorByUsername.isPresent()){
            model.addAttribute("errorMsg", errorByUsername.get());
            return "auth/registration";
        }
        if (user.getUsername().length() < 3) {

            model.addAttribute("hasError", true);
            model.addAttribute("errorMsg", "Введите логин от 3 символов");
            return "auth/registration";
        }
        boolean empty = model.asMap().isEmpty();
        return "auth/registration";
    }

    private Optional<String> validateUsername(String username) {
        String errorMsg = null;
        if (username.length() < 3) {
            errorMsg = "Логин должен содержать больше 3 символов";
        } else if (!username.matches("[A—Z\\-a-z]+\\w*")) {
            errorMsg = "Логин дожен начинаться с буквы и содержать латинский алфавит и цифры";
        } else if (userService.getUserByUsername(username).isPresent()){
            errorMsg = "Такой логин уже существует";
        }
        return Optional.ofNullable(errorMsg);
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login";
    }


}
