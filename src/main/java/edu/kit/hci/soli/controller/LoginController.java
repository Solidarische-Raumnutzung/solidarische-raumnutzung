package edu.kit.hci.soli.controller;

import edu.kit.hci.soli.config.SoliConfiguration;
import edu.kit.hci.soli.service.SystemConfigurationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling login-related requests.
 */
@Controller
public class LoginController {
    private final SystemConfigurationService systemConfigurationService;
    private final String guestMarker;

    /**
     * Constructs a LoginController with the specified {@link SystemConfigurationService}.
     *
     * @param systemConfigurationService the service for retrieving the system configuration
     * @param soliConfiguration          the configuration of the application
     */
    public LoginController(SystemConfigurationService systemConfigurationService, SoliConfiguration soliConfiguration) {
        this.systemConfigurationService = systemConfigurationService;
        this.guestMarker = soliConfiguration.getGuest().getMarker();
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
        model.addAttribute("guestEnabled", systemConfigurationService.isGuestLoginEnabled());

        return "auth/login";
    }

    /**
     * Displays the login UI for guests.
     *
     * @param model the model to be used in the view
     * @return the view name
     */
    @GetMapping("/login/guest")
    public String loginGuest(Model model) {
        model.addAttribute("guestMarker", guestMarker);
        return "auth/login_guest";
    }
}
