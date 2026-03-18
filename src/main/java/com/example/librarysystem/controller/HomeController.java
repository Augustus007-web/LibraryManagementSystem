package com.example.librarysystem.controller;

import com.example.librarysystem.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", loggedInUser.getName() != null && !loggedInUser.getName().isEmpty() ? loggedInUser.getName() : loggedInUser.getUsername());
        model.addAttribute("userRole", loggedInUser.getRole());
        return "index";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("username", loggedInUser.getName() != null && !loggedInUser.getName().isEmpty() ? loggedInUser.getName() : loggedInUser.getUsername());
        return "admin/dashboard";
    }
}