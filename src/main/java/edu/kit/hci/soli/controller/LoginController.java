package edu.kit.hci.soli.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        if (request.getParameter("error") != null) model.addAttribute("error", "Invalid username or password");
        if (request.getParameter("logout") != null) model.addAttribute("message", "You have been logged out");

        return "login";
    }
}