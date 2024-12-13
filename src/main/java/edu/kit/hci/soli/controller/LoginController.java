package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling login-related requests.
 */
@Controller
public class LoginController {
    private final UserService userService;

    /**
     * Constructs a LoginController with the specified {@link UserService}.
     *
     * @param userService the service for managing users
     */
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the login UI.
     *
     * @param request the HTTP request
     * @param model   the model to be used in the view
     * @return the view name
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        if (request.getParameter("error") != null) model.addAttribute("error", "Invalid username or password");
        if (request.getParameter("logout") != null) model.addAttribute("message", "You have been logged out");
        model.addAttribute("guestEnabled", userService.isGuestEnabled());

        return "login";
    }

    @Value("${soli.guest.marker}")
    private String guestMarker;

    /**
     * Displays the login UI for guests.
     *
     * @param model the model to be used in the view
     * @return the view name
     */
    @GetMapping("/login/guest")
    public String loginGuest(Model model) {
        model.addAttribute("guestMarker", guestMarker);
        return "login_guest";
    }
}
