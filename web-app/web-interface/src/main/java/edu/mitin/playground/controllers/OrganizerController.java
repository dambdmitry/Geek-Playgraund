package edu.mitin.playground.controllers;

import edu.mitin.playground.users.admin.RequestService;
import edu.mitin.playground.users.admin.entity.OrganizerRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organizer")
public class OrganizerController {

    final RequestService service;

    public OrganizerController(RequestService service) {
        this.service = service;
    }

    @GetMapping("")
    public String organizerPage() {
        return "organizer/organizerController";
    }

    @PreAuthorize("hasAuthority('profile')")
    @GetMapping("/registration")
    public String registrationOrganizer(Model model) {
        model.addAttribute("OrganizerRequest", new OrganizerRequest());
        return "organizer/organizerRegistration";
    }

    @PreAuthorize("hasAuthority('profile')")
    @PostMapping("/registration")
    public String registrationOrganizer(OrganizerRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requesterName = authentication.getName();
        service.registerRequest(request, requesterName);
        return "redirect:/organizer";
    }
}
