package edu.mitin.playground.controllers;

import edu.mitin.playground.users.UserService;
import edu.mitin.playground.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "auth/registration";
    }


    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(value = "error", required = false, defaultValue = "false") Boolean error) {
        if (error) {
            model.addAttribute("errorMsg", "Неверный логин или пароль");
        }
        model.addAttribute("user", new User());
        return "auth/login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String doRegistration(@ModelAttribute User user, Model model, HttpServletRequest request) throws ServletException {
        Optional<String> errorByUsername = validateUsername(user.getUsername());
        if (errorByUsername.isPresent()){
            model.addAttribute("errorMsg", errorByUsername.get());
            return "auth/registration";
        }
        Optional<String> errorByPassword = validatePassword(user.getPassword(), user.getPasswordConfirm());
        if (errorByPassword.isPresent()) {
            model.addAttribute("errorMsg", errorByPassword.get());
            return "auth/registration";
        }
        String password = user.getPassword();
        userService.registerUser(user);
        request.logout();
        request.login(user.getUsername(), password);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        System.out.println("Зарегистрирован");
        return "index";
    }



    @PostMapping("/login")
    public String login(Model model, User user) {
        Optional<User> userByUsername = userService.getUserByUsername(user.getUsername());
        if (userByUsername.isEmpty()) {
            model.addAttribute("errorMsg", "Такого пользователя нет");
            return "auth/login";
        }
        User userFromSystem = userByUsername.get();
        String encode = passwordEncoder.encode(user.getPassword());

        if(!passwordEncoder.matches(user.getPassword(), userFromSystem.getPassword())) {
            model.addAttribute("errorMsg", "Неверный пароль");
            return "auth/login";
        }

        return "index";
    }

    private Optional<String> validatePassword(String password, String passwordConfirm) {
        String errorMsg = null;
        if (password.length() < 6) {
            errorMsg = "Пароль должен содержать нее менее 6 символов";
        } else if (!password.equals(passwordConfirm)) {
            errorMsg = "Пароли не совпадают";
        }
        return Optional.ofNullable(errorMsg);
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

}
