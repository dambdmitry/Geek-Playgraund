package edu.mitin.playground.controllers;

import edu.mitin.playground.users.admin.AdminService;
import edu.mitin.playground.users.admin.model.OrganizerRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        List<OrganizerRequest> requests = adminService.getRequests();
        model.addAttribute("requests", requests);
        return "admin/adminPanel";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/accept/{id}")
    public String acceptOrganizer(@PathVariable("id") Long id){
        adminService.acceptOrganizer(id);
        return "redirect:/admin/";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/reject/{id}")
    public String rejectOrganizer(@PathVariable("id") Long id){
        adminService.rejectOrganizer(id);
        return "redirect:/admin/";
    }
}
